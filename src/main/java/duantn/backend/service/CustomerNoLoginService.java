package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerNoLoginService {
    List<ArticleOutputDTO> listArticle(
            Long start, Long end,
            Integer ward,
            Integer district,
            Integer city,
            Boolean roommate,
            String search,
            Integer minAcreage, Integer maxAcreage,
            Integer page,
            Integer limit
    );

    ArticleOutputDTO findOneArticle(Integer id) throws CustomException;
}
