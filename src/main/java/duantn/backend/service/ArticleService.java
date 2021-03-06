package duantn.backend.service;

import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleService {

    ResponseEntity<?> insertArticle(ArticleInsertDTO articleInsertDTO);

    //cập nhật bài viết Put/admin/articles
    ResponseEntity<?> updateArticle(ArticleUpdateDTO articleUpdateDTO);

    //xóa bài viết Delete/admin/articles/{id}
    ResponseEntity<String> deleteArticle(Integer id);

    //duyệt bài viết  Get/admin/articles/active/{id}
    ResponseEntity<String> activeArticle(Integer id);

    //xem bài viết Get/admin/articles/{id}
    ResponseEntity<?> findOneArticle(Integer id);

    // bộ lọc kết hợp
    List<ArticleOutputDTO> filterArticle(Boolean status,
                                        Long start, Long end,
                                        Integer wardId, Integer districtId, Integer cityId,
                                        String sort,
                                         Integer page, Integer limit);

}
