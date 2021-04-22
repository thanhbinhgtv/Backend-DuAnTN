package duantn.backend.controller.customer;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.output.Message;
import duantn.backend.service.ArticleFavoriteService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class FavoriteArticleManager {
    final
    ArticleFavoriteService articleFavoriteService;

    public FavoriteArticleManager(ArticleFavoriteService articleFavoriteService) {
        this.articleFavoriteService = articleFavoriteService;
    }

    //    lưu bài quan tâm	/customer/favorite-article/add?id={id}
    @GetMapping("/favorite-article/add")
    Message addArticle(HttpServletRequest request,
                       @RequestParam Integer id) throws CustomException {
        String email = (String) request.getAttribute("email");
        return articleFavoriteService.addArticle(email, id);
    }

    //    danh sách bài viết quan tâm của người dung	/customer/favorite-article
    @GetMapping("/favorite-article")
    List<Map<String, String>> listArticle(HttpServletRequest request,
                                    @RequestParam Integer page,
                                    @RequestParam Integer limit) throws CustomException {
        String email = (String) request.getAttribute("email");
        return articleFavoriteService.listArticle(email, page, limit);
    }

    //    xóa bài viết quan tâm	/customer/favorite-article/{id}
    @DeleteMapping("/favorite-article/{id}")
    Message deleteArticle(HttpServletRequest request,
                          @PathVariable Integer id) throws CustomException {
        String email = (String) request.getAttribute("email");
        return articleFavoriteService.deleteArticle(email, id);
    }
}
