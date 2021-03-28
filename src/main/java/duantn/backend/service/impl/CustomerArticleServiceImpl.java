package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.StaffArticleRepository;
import duantn.backend.dao.WardRepository;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.input.RoommateInsertDTO;
import duantn.backend.model.dto.input.RoommateUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.*;
import duantn.backend.service.CustomerArticleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    public CustomerArticleServiceImpl(ArticleRepository articleRepository, StaffArticleRepository staffArticleRepository, CustomerRepository customerRepository, WardRepository wardRepository) {
        this.articleRepository = articleRepository;
        this.staffArticleRepository = staffArticleRepository;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
    }

    @Override
    public List<ArticleOutputDTO> listArticle(String email, String sort, Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String status, Boolean vip, String search, Integer page, Integer limit) {
        List<Article> articleList=
                articleRepository.findCustomAndEmail(email,sort,start,end,ward,district,city,
                        roommate,status,vip,search,page,limit);
        List<ArticleOutputDTO> articleOutputDTOList=new ArrayList<>();
        if(articleList.size()>0){
            for(Article article:articleList){
                articleOutputDTOList.add(convertToOutputDTO(article));
            }
        }
        return articleOutputDTOList;
    }

    @Override
    public ArticleOutputDTO insertArticle(String email, ArticleInsertDTO articleInsertDTO)
    throws CustomException{
        System.out.println("email: "+email);
        Customer customer=customerRepository.findByEmail(email);
        if(customer==null) throw new CustomException("Khách hàng đăng bài không hợp lệ");
        try{
            ModelMapper modelMapper=new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Article article=modelMapper.map(articleInsertDTO, Article.class);

            article.setCustomer(customer);

            duantn.backend.model.entity.Service service=new duantn.backend.model.entity.Service();
            service.setElectricPrice(articleInsertDTO.getElectricPrice());
            service.setWaterPrice(articleInsertDTO.getWaterPrice());
            service.setWifiPrice(articleInsertDTO.getWifiPrice());
            article.setService(service);

            RoommateInsertDTO roommateInsertDTO= articleInsertDTO.getRoommateInsertDTO();
            Roommate roommate=null;
            if(roommateInsertDTO!=null)
                roommate=modelMapper.map(roommateInsertDTO, Roommate.class);
            article.setRoommate(roommate);

            Optional<Ward> wardOptional=wardRepository.findById(articleInsertDTO.getWardId());
            if(!wardOptional.isPresent()) throw new CustomException("Ward Id không hợp lệ");
            article.setWard(wardOptional.get());

            article.setUpdateTime(new Date());
            article.setDeleted(null);

            return convertToOutputDTO(articleRepository.save(article));
        } catch (CustomException e){
            throw new CustomException(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Đăng bài thất bại");
        }
    }

    @Override
    public ArticleOutputDTO updateArticle(String email, ArticleUpdateDTO articleUpdateDTO) throws CustomException {
        Customer customer=customerRepository.findByEmail(email);
        if(customer==null) throw new CustomException("Khách hàng không hợp lệ");
        try{
            ModelMapper modelMapper=new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);

            Optional<Article> articleOptional=articleRepository.findById(articleUpdateDTO.getArticleId());
            if(!articleOptional.isPresent()) throw new CustomException("Article id không hợp lệ");
            Article article=articleOptional.get();

            article.setTitle(articleUpdateDTO.getTitle());
            article.setContent(articleUpdateDTO.getContent());
            article.setImage(articleUpdateDTO.getImage());
            article.setRoomPrice(articleUpdateDTO.getRoomPrice());
            article.setDescription(articleUpdateDTO.getDescription());
            article.setVip(articleUpdateDTO.getVip());

            article.setCustomer(customer);

            duantn.backend.model.entity.Service service=new duantn.backend.model.entity.Service();
            service.setElectricPrice(articleUpdateDTO.getElectricPrice());
            service.setWaterPrice(articleUpdateDTO.getWaterPrice());
            service.setWifiPrice(articleUpdateDTO.getWifiPrice());
            article.setService(service);

            RoommateUpdateDTO roommateUpdateDTO= articleUpdateDTO.getRoommateUpdateDTO();
            Roommate roommate=null;
            if(roommateUpdateDTO!=null)
                roommate=modelMapper.map(roommateUpdateDTO, Roommate.class);
            article.setRoommate(roommate);

            Optional<Ward> wardOptional=wardRepository.findById(articleUpdateDTO.getWardId());
            if(!wardOptional.isPresent()) throw new CustomException("Ward Id không hợp lệ");
            article.setWard(wardOptional.get());

            article.setUpdateTime(new Date());
            article.setDeleted(null);

            return convertToOutputDTO(articleRepository.save(article));
        } catch (CustomException e){
            throw new CustomException(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Cập nhật bài thất bại");
        }
    }

    @Override
    public Message hiddenArticle(String email, Integer id) throws CustomException{
        Optional<Article> articleOptional=articleRepository.findById(id);
        if(!articleOptional.isPresent())
            throw new CustomException("Bài đăng với id: "+id+" không tồn tại");
        Article article=articleOptional.get();
        if(!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        article.setDeleted(false);
        articleRepository.save(article);
        return new Message("Ẩn bài đăng id: "+id+" thành công");
    }

    @Override
    public Message deleteArticle(String email, Integer id) throws CustomException {
        Optional<Article> articleOptional=articleRepository.findById(id);
        if(!articleOptional.isPresent())
            throw new CustomException("Bài đăng với id: "+id+" không tồn tại");
        Article article=articleOptional.get();
        if(!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        articleRepository.delete(article);
        return new Message("Xóa bài đăng id: "+id+" thành công");
    }

    @Override
    public Message extensionExp(String email, Integer id, Integer days) throws CustomException {
        Optional<Article> articleOptional=articleRepository.findById(id);
        if(!articleOptional.isPresent())
            throw new CustomException("Bài đăng với id: "+id+" không tồn tại");
        Article article=articleOptional.get();
        if(!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        article.setNumberDate(article.getNumberDate()+days);
        articleRepository.save(article);
        return new Message("Gia hạn bài đăng id: "+id+" thành công");
    }

    @Override
    public Message postOldArticle(String email, Integer id, Integer days) throws CustomException {
        Article article=articleRepository.findByDeletedFalseAndArticleId(id);
        if(article==null)
            throw new CustomException("Bài đăng với id: "+id+" không tồn tại, hoặc đang không bị ẩn");
        if(!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");
        article.setDeleted(null);
        article.setNumberDate(days);
        articleRepository.save(article);
        return new Message("Đăng lại bài đăng đã ẩn id: "+id+" thành công");
    }

    @Override
    public ArticleOutputDTO detailArticle(String email, Integer id) throws CustomException {
        Optional<Article> articleOptional=articleRepository.findById(id);
        if(!articleOptional.isPresent())
            throw new CustomException("Bài đăng với id: "+id+" không tồn tại");
        Article article=articleOptional.get();
        System.out.println("email: "+email);
        System.out.println("customer: "+article.getCustomer().getEmail());
        if(!email.equals(article.getCustomer().getEmail()))
            throw new CustomException("Khách hàng không hợp lệ");

        return convertToOutputDTO(article);
    }

    public ArticleOutputDTO convertToOutputDTO(Article article){
        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        ArticleOutputDTO articleOutputDTO=modelMapper.map(article, ArticleOutputDTO.class);
        articleOutputDTO.setCreateTime(article.getTimeCreated().getTime());
        articleOutputDTO.setLastUpdateTime(article.getUpdateTime().getTime());
        if(article.getDeleted()!=null){
            if(article.getDeleted()) articleOutputDTO.setStatus("Đang đăng");
            else articleOutputDTO.setStatus("Đã ẩn");
        } else articleOutputDTO.setStatus("Chưa duyệt");

        StaffArticle staffArticle=staffArticleRepository.
                findFirstByArticle_ArticleId(article.getArticleId(), Sort.by("time").descending());


        if(staffArticle!=null){
            Map<String, String> moderator=new HashMap<>();
            moderator.put("staffId", staffArticle.getStaff().getStaffId()+"");
            moderator.put("name", staffArticle.getStaff().getName());
            moderator.put("email", staffArticle.getStaff().getEmail());
            articleOutputDTO.setModerator(moderator);
        }

        Map<String, String> customer=new HashMap<>();
        customer.put("customerId", article.getCustomer().getCustomerId()+"");
        customer.put("name", article.getCustomer().getName());
        customer.put("email", article.getCustomer().getEmail());
        articleOutputDTO.setCustomer(customer);

        if(article.getDeleted()!=null && article.getDeleted()==true && staffArticle!=null){
            articleOutputDTO.
                    setExpDate(staffArticle.getTime().getTime()+article.getNumberDate()*24*3600*1000);
        }

        Map<String, String> address=new HashMap<>();
        address.put("wardId", article.getWard().getWardId()+"");
        address.put("wardName", article.getWard().getWardName());
        address.put("districtId", article.getWard().getDistrict().getDistrictId()+"");
        address.put("districtName", article.getWard().getDistrict().getDistrictName());
        address.put("cityId", article.getWard().getDistrict().getCity().getCityId()+"");
        address.put("cityName", article.getWard().getDistrict().getCity().getCityName());
        articleOutputDTO.setAddress(address);

        return articleOutputDTO;
    }
}
