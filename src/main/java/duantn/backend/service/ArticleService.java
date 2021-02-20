package duantn.backend.service;

import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleService {
    //danh sách bài viêt
    List<ArticleOutputDTO> listArticle(Integer page, Integer limit);

    //thêm bài viết
    ResponseEntity<?> insertArticle(ArticleInsertDTO articleInsertDTO);

    //cập nhật bài viết
    ResponseEntity<?> updateArticle(ArticleUpdateDTO articleUpdateDTO);

    //xóa bài viết
    ResponseEntity<String> deleteArticle(Integer id);

    //duyệt bài viết
    ResponseEntity<String> activeArticle(Integer id);

    //xem bài viết
    ResponseEntity<?> findOneArticle(Integer id);

    // tìm kiếm bài viêt theo nội dung, giá phòng, diện tích
    List<ArticleOutputDTO> findArticleByTitleAndPhone(String search, Integer page, Integer limit);

    // articles/post-time-desc"
    List<ArticleOutputDTO> findArticleByPostTimeDESC(Integer page, Integer limit);

    // articles/post-time-asc"
    List<ArticleOutputDTO> findArticleByPostTimeAsc(Integer page, Integer limit);
    // danh sach bài đăng theo trạng thái GET/admin/articles/status-true
    List<ArticleOutputDTO> ListAriticleStatusTrue( Integer page, Integer limit);

}
