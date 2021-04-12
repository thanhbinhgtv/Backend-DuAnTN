package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.component.MailSender;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.StaffArticleRepository;
import duantn.backend.dao.StaffRepository;
import duantn.backend.helper.Helper;
import duantn.backend.model.dto.input.ContactCustomerDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.Staff;
import duantn.backend.model.entity.StaffArticle;
import duantn.backend.service.ArticleService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpI implements ArticleService {
    final
    ArticleRepository articleRepository;

    final
    StaffArticleRepository staffArticleRepository;

    final
    MailSender mailSender;

    final
    JwtUtil jwtUtil;

    final
    StaffRepository staffRepository;

    final
    Helper helper;


    public ArticleServiceImpI(ArticleRepository articleRepository, StaffArticleRepository staffArticleRepository, MailSender mailSender, JwtUtil jwtUtil, StaffRepository staffRepository, Helper helper) {
        this.articleRepository = articleRepository;
        this.staffArticleRepository = staffArticleRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
        this.staffRepository = staffRepository;
        this.helper = helper;
    }

    @Override
    public List<ArticleOutputDTO> listArticle(String sort, Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String status, Boolean vip, String search, Integer minAcreage, Integer maxAcreage, Integer page, Integer limit) {
        List<ArticleOutputDTO> articleOutputDTOList = new ArrayList<>();
        List<Article> articleList =
                articleRepository.findCustom(sort, start, end, ward, district, city,
                        roommate, status, vip, search, minAcreage, maxAcreage, page, limit);
        for (Article article : articleList) {
            articleOutputDTOList.add(helper.convertToOutputDTO(article));
        }
        return articleOutputDTOList;
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public Message contactToCustomer(Integer id,
                                     ContactCustomerDTO contactCustomerDTO,
                                     HttpServletRequest request) throws CustomException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Staff staff;
            try {
                staff = findStaffByJWT(request);
                if (staff == null) throw new Exception();
            } catch (ExpiredJwtException e) {
                throw new CustomException("JWT hết hạn");
            } catch (Exception e) {
                throw new CustomException("JWT không hợp lệ");
            }


            String to = articleOptional.get().getCustomer().getEmail();

            String note = "Nhân viên liên hệ: " + staff.getName() + "<br/>"
                    + "Email: " + staff.getEmail() + "<br/>"
                    + "Thời gian: " + simpleDateFormat.format(new Date());

            mailSender.send(to, contactCustomerDTO.getTitle(), contactCustomerDTO.getContent(), note);
            return new Message("Gửi mail thành công");
        } else throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
    }

    @Override
    public Message activeArticle(Integer id, HttpServletRequest request) throws CustomException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Staff staff;
            try {
                staff = findStaffByJWT(request);
                if (staff == null) throw new Exception();
            } catch (ExpiredJwtException e) {
                throw new CustomException("JWT hết hạn");
            } catch (Exception e) {
                throw new CustomException("JWT không hợp lệ");
            }

            //duyệt bài
            //chuyển deleted thành true
            Article article = articleOptional.get();
            if (article.getDeleted() != null)
                throw new CustomException("Chỉ được duyệt bài có trạng thái là chưa duyệt");
            article.setDeleted(true);

            //tạo thời hạn
            Integer days = null;
            if (article.getType().equals("day")) {
                days = article.getNumber();
                article.setExpTime(helper.addDayForDate(days, new Date()));
                article.setType(null);
                article.setNumber(null);
            } else if (article.getType().equals("week")) {
                days = helper.calculateDays(article.getNumber(), article.getType(),
                        new Date());
                article.setExpTime(helper.addDayForDate(days, new Date()));
                article.setType(null);
                article.setNumber(null);
            } else if (article.getType().equals("month")) {
                days = helper.calculateDays(article.getNumber(), article.getType(),
                        new Date());
                article.setExpTime(helper.addDayForDate(days, new Date()));
                article.setType(null);
                article.setNumber(null);
            } else throw new CustomException("Type của bài đăng bị sai");

            //tạo bản ghi staffArticle
            StaffArticle staffArticle = new StaffArticle();
            staffArticle.setTime(new Date());
            staffArticle.setStaff(staff);
            staffArticle.setArticle(article);
            staffArticle.setAction(true);
            //lưu
            staffArticleRepository.save(staffArticle);
            ArticleOutputDTO articleOutputDTO = helper.convertToOutputDTO(articleRepository.save(article));

            //gửi thư
            String to = article.getCustomer().getEmail();
            String note = "Nhân viên duyệt bài: " + staff.getName() + "<br/>"
                    + "Email: " + staff.getEmail() + "<br/>"
                    + "Thời gian: " + simpleDateFormat.format(new Date());
            String title = "Bài đăng số: " + article.getArticleId() + " đã được duyệt";
            String content = "<p>Bài đăng số: " + article.getArticleId() + "</p>\n" +
                    "\n" +
                    "<p>Tiêu đề: " + article.getTitle() + "</p>\n" +
                    "\n" +
                    "<p>Người đăng: " + article.getCustomer().getName() + "</p>\n" +
                    "\n" +
                    "<p>Email: " + article.getCustomer().getEmail() + "</p>\n" +
                    "\n" +
                    "<p>SĐT: " + article.getCustomer().getPhone() + "</p>\n" +
                    "\n" +
                    "<p>Thời gian đăng: " + simpleDateFormat.format(article.getTimeCreated()) + "</p>\n" +
                    "\n" +
                    "\n" +
                    "<p>Thời gian duyệt bài: " + simpleDateFormat.format(new Date()) + "</p>\n" +
                    "\n" +
                    "<p>Trạng thái: <strong><span style=\"color:#2980b9\">đã được duyệt</span></strong></p>\n" +
                    "\n" +
                    "<p>Thời gian hết hạn (ước tính): <span style=\"color:#c0392b\">" + simpleDateFormat.format(new Date(articleOutputDTO.getExpDate())) + "</span></p>\n" +
                    "\n" +
                    "<p>Bài đăng của bạn đã được nhân viên <em><strong>" + staff.getName() + " </strong></em>(email: <em><strong>" + staff.getEmail() + "</strong></em>) duyệt vào lúc <em><strong>" + simpleDateFormat.format(new Date()) + "</strong></em>.</p>\n" +
                    "\n" +
                    "<p>Bạn có thể vào theo đường dẫn sau để xem bài viết của mình:</p>\n" +
                    "\n" +
                    "<p> xxxx </p>\n";
            mailSender.send(to, title, content, note);
            return new Message("Duyệt bài thành công");
        } else throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
    }

    @Override
    public Message hiddenArticle(Integer id, String reason, HttpServletRequest request) throws CustomException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            Staff staff;
            try {
                staff = findStaffByJWT(request);
                if (staff == null) throw new Exception();
            } catch (ExpiredJwtException e) {
                throw new CustomException("JWT hết hạn");
            } catch (Exception e) {
                throw new CustomException("JWT không hợp lệ");
            }

            //duyệt bài
            //chuyển deleted thành true
            Article article = articleOptional.get();
            if (article.getDeleted() == false)
                throw new CustomException("Không thể ẩn bài viết đang ẩn");

            article.setDeleted(false);

            article.setExpTime(null);

            //tạo bản ghi staffArticle
            StaffArticle staffArticle = new StaffArticle();
            staffArticle.setTime(new Date());
            staffArticle.setStaff(staff);
            staffArticle.setArticle(article);
            staffArticle.setAction(false);
            //lưu
            staffArticleRepository.save(staffArticle);
            ArticleOutputDTO articleOutputDTO = helper.convertToOutputDTO(articleRepository.save(article));

            //gửi thư
            if (reason == null || reason.trim().equals("")) reason = "không có lý do cụ thể";
            String to = article.getCustomer().getEmail();
            String note = "Nhân viên duyệt bài: " + staff.getName() + "<br/>"
                    + "Email: " + staff.getEmail() + "<br/>"
                    + "Thời gian: " + simpleDateFormat.format(new Date());
            String title = "Bài đăng số: " + article.getArticleId() + " đã bị ẩn";
            String content = "<p>Bài đăng số: " + article.getArticleId() + "</p>\n" +
                    "\n" +
                    "<p>Tiêu đề: " + article.getTitle() + "</p>\n" +
                    "\n" +
                    "<p>Người đăng: " + article.getCustomer().getName() + "</p>\n" +
                    "\n" +
                    "<p>Email: " + article.getCustomer().getEmail() + "</p>\n" +
                    "\n" +
                    "<p>SĐT: " + article.getCustomer().getPhone() + "</p>\n" +
                    "\n" +
                    "<p>Thời gian đăng: " + simpleDateFormat.format(article.getTimeCreated()) + "</p>\n" +
                    "\n" +
                    "\n" +
                    "<p>Thời gian ẩn bài: " + simpleDateFormat.format(new Date()) + "</p>\n" +
                    "\n" +
                    "<p>Trạng thái: <strong><span style=\"color:red\">đã bị ẩn</span></strong></p>\n" +
                    "\n" +
                    "<p>Lý do: <strong><span style=\"color:blue\">" + reason + "</span></strong></p>\n" +
                    "\n" +
                    "<p>Bài đăng của bạn đã bị nhân viên <em><strong>" + staff.getName() + " </strong></em>(email: <em><strong>" + staff.getEmail() + "</strong></em>) ẩn vào lúc <em><strong>" + simpleDateFormat.format(new Date()) + "</strong></em>.</p>\n" +
                    "\n" +
                    "<p>Chúng tôi rất tiếc về điều này, bạn vui lòng xem lại bài đăng của mình đã phù hợp với nội quy website chưa. Mọi thắc mắc xin liên hệ theo email nhân viên đã duyệt bài.</p>\n";
            mailSender.send(to, title, content, note);
            return new Message("Ẩn bài thành công");
        } else throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
    }

    @Override
    public ArticleOutputDTO detailArticle(Integer id) throws CustomException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (!articleOptional.isPresent())
            throw new CustomException("Bài đăng với id: " + id + " không tồn tại");
        Article article = articleOptional.get();
        return helper.convertToOutputDTO(article);
    }

    private Staff findStaffByJWT(HttpServletRequest request) throws Exception {
        String jwt = extractJwtFromRequest(request);
        if (jwt == null || jwt.trim().equals("")) throw new CustomException("Không có JWT");
        String email = jwtUtil.getUsernameFromToken(jwt);
        if (email == null || email.trim().equals("")) throw new CustomException("JWT không hợp lệ");
        Staff staff = staffRepository.findByEmail(email);
        if (staff == null) throw new CustomException("JWT không hợp lệ");
        return staff;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
