package duantn.backend.service;

import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleService {
    //thêm bài viết Post/admin/articles
    // lưa ý các bài viết được thêm vào thì mặc ddinjhj của nó là true có nghĩa là chưa được hiển thị
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

    List<ArticleOutputDTO> searchArticle(String search,
                                         Integer page, Integer limit);
}
