package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.FavoriteArticleRepository;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.Customer;
import duantn.backend.model.entity.FavoriteArticle;
import duantn.backend.service.ArticleFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleFavoriteServiceImpl implements ArticleFavoriteService {
    final
    FavoriteArticleRepository favoriteArticleRepository;
    final
    CustomerRepository customerRepository;
    final
    ArticleRepository articleRepository;

    public ArticleFavoriteServiceImpl(FavoriteArticleRepository favoriteArticleRepository, CustomerRepository customerRepository, ArticleRepository articleRepository) {
        this.favoriteArticleRepository = favoriteArticleRepository;
        this.customerRepository = customerRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Message addArticle(String email, Integer id) throws CustomException {
        Customer customer=customerRepository.findByEmail(email);
        if(customer==null) throw new CustomException("Khách hàng không tồn tại");
        Optional<Article> optionalArticle=articleRepository.findById(id);
        if(!optionalArticle.isPresent()) throw new CustomException("Bài đăng không tồn tại");
        Article article=optionalArticle.get();
        FavoriteArticle oldArticle=
                favoriteArticleRepository.findByCustomer_EmailAndArticle_ArticleId(email, id);
        if(oldArticle!=null){
            return new Message("Bài đăng đã được lưu");
        }else {
            FavoriteArticle favoriteArticle=new FavoriteArticle();
            favoriteArticle.setArticle(article);
            favoriteArticle.setCustomer(customer);
            favoriteArticleRepository.save(favoriteArticle);
            return new Message("Lưu bài đăng thành công");
        }
    }

    @Override
    public List<Map<String, String>> listArticle(String email,
                                                 Integer page, Integer limit) throws CustomException {
        Page<FavoriteArticle> favoriteArticlePage=
                favoriteArticleRepository.findByCustomer_Email(email,
                        PageRequest.of(page,limit));
        List<FavoriteArticle> favoriteArticleList=favoriteArticlePage.toList();
        List<Map<String, String>> mapList=new ArrayList<>();
        for (FavoriteArticle favoriteArticle: favoriteArticleList){
            Map<String, String> map=new HashMap<>();
            map.put("id", favoriteArticle.getArticle().getArticleId().toString());
            map.put("title", favoriteArticle.getArticle().getTitle());
            map.put("timeUpdate", String.valueOf(favoriteArticle.getArticle().getUpdateTime().getTime()));
            map.put("price", String.valueOf(favoriteArticle.getArticle().getRoomPrice()));
            map.put("ward", favoriteArticle.getArticle().getWard().getWardName());
            map.put("status", favoriteArticle.getArticle().getDeleted()?"Đang đăng":"Đã ẩn/Đợi duyệt");
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public Message deleteArticle(String email, Integer id) throws CustomException {
        FavoriteArticle oldArticle=
                favoriteArticleRepository.findByCustomer_EmailAndArticle_ArticleId(email, id);
        if(oldArticle==null){
            throw new CustomException("Bài lưu không tồn tại");
        }else {
            favoriteArticleRepository.delete(oldArticle);
            return new Message("Xóa bài lưu thành công");
        }
    }
}
