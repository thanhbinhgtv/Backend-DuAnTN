package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.helper.Helper;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.entity.Article;
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

    public CustomerNoLoginServiceImpl(ArticleRepository articleRepository, Helper helper) {
        this.articleRepository = articleRepository;
        this.helper = helper;
    }

    @Override
    public List<ArticleOutputDTO> listArticle(Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String search, Integer minAcreage, Integer maxAcreage, Integer page, Integer limit) {
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
            articleOutputDTOList.add(articleOutputDTO);
        }
        return articleOutputDTOList;
    }

    @Override
    public ArticleOutputDTO findOneArticle(Integer id) throws CustomException {
        Article article=articleRepository.findByArticleIdAndDeletedTrue(id);
        if(article== null) throw new CustomException("Bài đăng không tồn tại hoặc chưa được duyệt");
        return helper.convertToOutputDTO(article);
    }
}
