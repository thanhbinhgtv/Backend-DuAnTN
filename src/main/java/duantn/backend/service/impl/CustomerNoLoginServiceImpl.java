package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.FavoriteArticleRepository;
import duantn.backend.helper.Helper;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.FavoriteArticle;
import duantn.backend.service.CustomerNoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerNoLoginServiceImpl implements CustomerNoLoginService {
    final
    ArticleRepository articleRepository;
    final
    Helper helper;
    final
    FavoriteArticleRepository favoriteArticleRepository;

    public CustomerNoLoginServiceImpl(ArticleRepository articleRepository, Helper helper, FavoriteArticleRepository favoriteArticleRepository) {
        this.articleRepository = articleRepository;
        this.helper = helper;
        this.favoriteArticleRepository = favoriteArticleRepository;
    }

    @Override
    public List<ArticleOutputDTO> listArticle(String email, Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String search, Integer minAcreage, Integer maxAcreage, Integer page, Integer limit) {
        List<Article> articleList= articleRepository.findCustomShow(start,end,ward,district,city,roommate,"active",
                search,minAcreage,maxAcreage,page,limit);
        List<ArticleOutputDTO> articleOutputDTOList = new ArrayList<>();
        Map<String, Long> countMap=articleRepository.findCustomShowCount(
                start,end,ward,district,city,roommate,"active",
                search,minAcreage,maxAcreage,limit
        );
        for (Article article : articleList) {
            ArticleOutputDTO articleOutputDTO=helper.convertToOutputDTO(article);
            articleOutputDTO.setElements(countMap.get("elements"));
            articleOutputDTO.setPages(countMap.get("pages"));
            if(email!=null){
                if(favoriteArticleRepository.findByCustomer_EmailAndArticle_ArticleId(email, article.getArticleId())!=null)
                    articleOutputDTO.setLiked(true);
            }
            articleOutputDTOList.add(articleOutputDTO);
        }
        return articleOutputDTOList;
    }

    @Override
    public ArticleOutputDTO findOneArticle(String email, Integer id) throws CustomException {
        Article article=articleRepository.findByArticleIdAndDeletedTrue(id);
        if(article== null) throw new CustomException("Bài đăng không tồn tại hoặc chưa được duyệt");

        ArticleOutputDTO articleOutputDTO=helper.convertToOutputDTO(article);

        if(email!=null){
            if(favoriteArticleRepository.findByCustomer_EmailAndArticle_ArticleId(email, article.getArticleId())!=null)
                articleOutputDTO.setLiked(true);
        }

        return articleOutputDTO;
    }
}
