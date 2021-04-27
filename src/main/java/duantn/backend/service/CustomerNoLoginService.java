package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerNoLoginService {
    List<ArticleOutputDTO> listArticle(String email,
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

    ArticleOutputDTO findOneArticle(String email, Integer id) throws CustomException;
}
