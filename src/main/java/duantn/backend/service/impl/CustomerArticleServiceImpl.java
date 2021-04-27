package duantn.backend.service.impl;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import duantn.backend.authentication.CustomException;
import duantn.backend.config.paypal.PaypalPaymentIntent;
import duantn.backend.config.paypal.PaypalPaymentMethod;
import duantn.backend.config.paypal.PaypalService;
import duantn.backend.dao.*;
import duantn.backend.helper.Helper;
import duantn.backend.helper.VariableCommon;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.input.RoommateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.*;
import duantn.backend.service.CustomerArticleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CustomerArticleServiceImpl implements CustomerArticleService {
    final
    ArticleRepository articleRepository;
    final
    StaffArticleRepository staffArticleRepository;
    final
    CustomerRepository customerRepository;
    final
    WardRepository wardRepository;
    final
    Helper helper;
    final
    TransactionRepository transactionRepository;
    final
    FavoriteArticleRepository favoriteArticleRepository;
    final
    PaypalService paypalService;

    public CustomerArticleServiceImpl(ArticleRepository articleRepository, StaffArticleRepository staffArticleRepository, CustomerRepository customerRepository, WardRepository wardRepository, Helper helper, TransactionRepository transactionRepository, FavoriteArticleRepository favoriteArticleRepository, PaypalService paypalService) {
        this.articleRepository = articleRepository;
        this.staffArticleRepository = staffArticleRepository;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.helper = helper;
        this.transactionRepository = transactionRepository;
        this.favoriteArticleRepository = favoriteArticleRepository;
        this.paypalService = paypalService;
    }

    @Override
    public List<ArticleOutputDTO> listArticle(String email, String sort, Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String status, Boolean vip, String search, Integer minAcreage, Integer maxAcreage, Integer page, Integer limit) {
        List<Article> articleList =
                articleRepository.findCustomAndEmail(email, sort, start, end, ward, district, city,
                        roommate, status, vip, search, minAcreage, maxAcreage, page, limit);
        Map<String, Long> countMap=articleRepository.findCustomAndEmailCount(
                email, start, end, ward, district, city,
                roommate, status, vip, search, minAcreage, maxAcreage,limit
        );
        List<ArticleOutputDTO> articleOutputDTOList = new ArrayList<>();
        if (articleList.size() > 0) {
            for (Article article : articleList) {
                ArticleOutputDTO articleOutputDTO=helper.convertToOutputDTO(article);
                articleOutputDTO.setElements(countMap.get("elements"));
                articleOutputDTO.setPages(countMap.get("pages"));
                articleOutputDTOList.add(articleOutputDTO);
            }
        }
        return articleOutputDTOList;
    }

    @Override
    public ArticleOutputDTO insertArticle(String email, ArticleInsertDTO articleInsertDTO)
            throws CustomException {
        System.out.println("email: " + email);
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new CustomException("Khách hàng đăng bài không hợp lệ");

        //kiểm tra và trừ tiền
        Integer money = null;
        int priceDay = articleInsertDTO.getVip() ? 10000 : 2000;
        int priceWeek = articleInsertDTO.getVip() ? 63000 : 12000;
        int priceMonth = articleInsertDTO.getVip() ? 240000 : 48000;
        if (articleInsertDTO.getType().equals("day")) {
            money = articleInsertDTO.getNumber() * priceDay;
        } else if (articleInsertDTO.getType().equals("week")) {
            money = articleInsertDTO.getNumber() * priceWeek;
        } else if (articleInsertDTO.getType().equals("month")) {
            money = articleInsertDTO.getNumber() * priceMonth;
        } else throw new CustomException("Type của thời gian không hợp lệ");
        if (customer.getAccountBalance() < money)
            throw new CustomException("Số tiền trong tài khoản không đủ");
        customer.setAccountBalance(customer.getAccountBalance() - money);

        String description = "Thanh toán đăng bài: " + money + " VNĐ cho bài đăng: " + articleInsertDTO.getTitle();

        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Article article = modelMapper.map(articleInsertDTO, Article.class);

            duantn.backend.model.entity.Service service = new duantn.backend.model.entity.Service();
            service.setElectricPrice(articleInsertDTO.getElectricPrice());
            service.setWaterPrice(articleInsertDTO.getWaterPrice());
            service.setWifiPrice(articleInsertDTO.getWifiPrice());
            article.setService(service);

            RoommateDTO roommateDTO = articleInsertDTO.getRoommateDTO();
            Roommate roommate = null;
            if (roommateDTO != null)
                roommate = modelMapper.map(roommateDTO, Roommate.class);
            article.setRoommate(roommate);

            Optional<Ward> wardOptional = wardRepository.findById(articleInsertDTO.getWardId());
            if (!wardOptional.isPresent()) throw new CustomException("Ward Id không hợp lệ");
            article.setWard(wardOptional.get());

            article.setUpdateTime(new Date());
            article.setStatus(VariableCommon.CHUA_DUYET);

            Customer newCustomer = customerRepository.save(customer);
            article.setCustomer(newCustomer);
            creatTransactionPay(money, newCustomer, description);
            return helper.convertToOutputDTO(articleRepository.save(article));
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Đăng bài thất bại");
        }
    }

    public void creatTransactionPay(Integer amount, Customer customer, String description) {
        Transaction transaction = new Transaction();
        transaction.setType(false);
        transaction.setAmount(amount);
        transaction.setTimeCreated(new Date());
        transaction.setCustomer(customer);
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    @Override
    public ArticleOutputDTO updateArticle(String email, ArticleUpdateDTO articleUpdateDTO,
                                          Integer id) throws CustomException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new CustomException("Không tìm thấy khách hàng");
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);

            Article article = articleRepository.findByArticleId(id);
            if (article == null) throw new CustomException("Bài đăng id không hợp lệ");
            if (customer != article.getCustomer())
                throw new CustomException("Khách hàng không hợp lệ");

            article.setTitle(articleUpdateDTO.getTitle());
            article.setImage(articleUpdateDTO.getImage());
            article.setRoomPrice(articleUpdateDTO.getRoomPrice());
            article.setDescription(articleUpdateDTO.getDescription());

            article.setAddress(articleUpdateDTO.getAddress());
            article.setAcreage(articleUpdateDTO.getAcreage());
            article.setVideo(articleUpdateDTO.getVideo());

            duantn.backend.model.entity.Service service = article.getService();
            service.setElectricPrice(articleUpdateDTO.getElectricPrice());
            service.setWaterPrice(articleUpdateDTO.getWaterPrice());
            service.setWifiPrice(articleUpdateDTO.getWifiPrice());
            article.setService(service);

            RoommateDTO roommateDTO = articleUpdateDTO.getRoommateDTO();
            if (roommateDTO != null) {
                Roommate roommate = article.getRoommate();
                roommate.setDescription(roommateDTO.getDescription());
                roommate.setGender(roommateDTO.getGender());
                roommate.setQuantity(roommateDTO.getQuantity());
                article.setRoommate(roommate);
            } else article.setRoommate(null);


            Optional<Ward> wardOptional = wardRepository.findById(articleUpdateDTO.getWardId());
            if (!wardOptional.isPresent()) throw new CustomException("Ward Id không hợp lệ");
            article.setWard(wardOptional.get());

            article.setUpdateTime(new Date());
            if (article.getStatus().equals(VariableCommon.DANG_DANG) || article.getStatus().equals(VariableCommon.SUA_LAI))
                article.setStatus(VariableCommon.CHUA_DUYET);

            return helper.convertToOutputDTO(articleRepository.save(article));
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Cập nhật bài thất bại");
        }
    }

    @Override
    public Message hiddenArticle(String email, Integer id) throws CustomException {
        Article article = articleRepository.findByArticleId(id);
        if (article == null)
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");

        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        article.setStatus(VariableCommon.BI_AN);

        articleRepository.save(article);
        return new Message("Ẩn bài đăng id: " + id + " thành công");
    }

    @Override
    public Message deleteArticle(String email, Integer id) throws CustomException {
        Article article = articleRepository.findByArticleId(id);
        if (article == null)
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");

        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");

        //xóa bỏ article trong favorite_article
        List<FavoriteArticle> favoriteArticleList=
                favoriteArticleRepository.findByArticle(article);
        if(favoriteArticleList.size()>0){
            for (FavoriteArticle favoriteArticle: favoriteArticleList){
                favoriteArticle.setArticle(null);
                favoriteArticleRepository.save(favoriteArticle);
            }
        }

        //xóa bỏ article trong staff_article
        List<StaffArticle> staffArticleList=
                staffArticleRepository.findByArticle(article);
        if(staffArticleList.size()>0){
            for (StaffArticle staffArticle: staffArticleList){
                staffArticle.setArticle(null);
                staffArticleRepository.save(staffArticle);
            }
        }

        articleRepository.delete(article);
        return new Message("Xóa bài đăng id: " + id + " thành công");
    }

    @Override
    public Message extensionExp(String email, Integer id, Integer days, String type) throws CustomException {
        Article article = articleRepository.findByArticleId(id);
        if (article == null)
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
        else if (!article.getStatus().equals(VariableCommon.DANG_DANG))
            throw new CustomException("Gia hạn chỉ áp dụng với bài đăng đã được duyệt");

        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");

        //kiểm tra và trừ tiền
        Integer money = null;
        int priceDay = article.getVip() ? 10000 : 2000;
        int priceWeek = article.getVip() ? 63000 : 12000;
        int priceMonth = article.getVip() ? 240000 : 48000;
        if (type.equals("day")) {
            money = days * priceDay;
        } else if (type.equals("week")) {
            money = days * priceWeek;
        } else if (type.equals("month")) {
            money = days * priceMonth;
        } else throw new CustomException("Type của thời gian không hợp lệ");
        Customer customer = article.getCustomer();
        if (customer.getAccountBalance() < money)
            throw new CustomException("Số tiền trong tài khoản không đủ");
        customer.setAccountBalance(customer.getAccountBalance() - money);

        String description = "Thanh toán gia hạn: " + money + " VNĐ cho bài đăng: " + article.getTitle();

        //tạo thời hạn
        if (article.getType().equals("day")) {
            article.setExpTime(helper.addDayForDate(days, article.getExpTime()));
        } else if (article.getType().equals("week") || article.getType().equals("month")) {
            Integer numberDay = helper.calculateDays(days, type, article.getExpTime());
            article.setExpTime(helper.addDayForDate(numberDay, article.getExpTime()));
        } else throw new CustomException("Type của bài đăng bị sai");

        Customer newCustomer = customerRepository.save(customer);
        creatTransactionPay(money, newCustomer, description);
        articleRepository.save(article);
        return new Message("Gia hạn bài đăng id: " + id + " thành công");
    }

    @Override
    public Message postOldArticle(String email, Integer id, Integer days, String type,
                                  Boolean vip) throws CustomException {
        Article article = articleRepository.findByArticleId(id);
        if (article == null)
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        if(article.getStatus().equals(VariableCommon.DANG_DANG)||
                article.getStatus().equals(VariableCommon.SUA_LAI)||
                article.getStatus().equals(VariableCommon.CHUA_DUYET))
            throw new CustomException("Đăng lại bài cũ chỉ áp dụng với bài bị ẩn hoặc hết hạn");
        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");

        article.setVip(vip);

        //kiểm tra và trừ tiền
        Integer money = null;
        int priceDay = article.getVip() ? 10000 : 2000;
        int priceWeek = article.getVip() ? 63000 : 12000;
        int priceMonth = article.getVip() ? 240000 : 48000;
        if (type.equals("day")) {
            money = days * priceDay;
        } else if (type.equals("week")) {
            money = days * priceWeek;
        } else if (type.equals("month")) {
            money = days * priceMonth;
        } else throw new CustomException("Type của thời gian không hợp lệ");
        Customer customer = article.getCustomer();
        if (customer.getAccountBalance() < money)
            throw new CustomException("Số tiền trong tài khoản không đủ");
        customer.setAccountBalance(customer.getAccountBalance() - money);

        String description = "Thanh toán đăng lại bài: " + money + " VNĐ cho bài đăng: " + article.getTitle();

        article.setStatus(VariableCommon.CHUA_DUYET);

        article.setNumber(days);
        article.setType(type);

        Customer newCustomer = customerRepository.save(customer);
        creatTransactionPay(money, newCustomer, description);
        articleRepository.save(article);
        return new Message("Đăng lại bài đăng đã ẩn id: " + id + " thành công");
    }

    @Override
    public ArticleOutputDTO detailArticle(String email, Integer id) throws CustomException {
        Article article = articleRepository.findByArticleId(id);
        if (article == null)
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");

        System.out.println("email: " + email);
        System.out.println("customer: " + article.getCustomer().getEmail());
        if (!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");

        return helper.convertToOutputDTO(article);
    }

    private String URL_PAYPAL_SUCCESS = "pay/success";
    private String URL_PAYPAL_CANCEL = "pay/cancel";
    public Message pay(HttpServletRequest request,
                       double price,
                       String description)
            throws CustomException {
        String cancelUrl = helper.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = helper.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        HttpSession session=request.getSession();
        try {
            Double value = Math.round(price * 100.0) / 100.0;
            session.setAttribute("value", value);
            String email= (String) request.getAttribute("email");
            session.setAttribute("email", email);

            if (description == null || description.trim().equals("")) description = "Nạp " + value + " USD";
            session.setAttribute("description", description);

            if (email == null || email.trim().equals("")) throw new CustomException("Token không hợp lệ");

            Customer customer = customerRepository.findByEmail(email);
            if (customer == null)
                throw new CustomException("Người dùng không hợp lệ");
            session.setAttribute("customer", customer);

            Payment payment = paypalService.createPayment(
                    value,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    description,
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    //response.sendRedirect(links.getHref());
                    return new Message(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            throw new CustomException("Thanh toán thất bại");
        }
        //return "redirect:/";
        throw new CustomException("Không xác định");
    }
}
