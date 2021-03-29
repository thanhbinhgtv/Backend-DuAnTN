package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import io.swagger.models.auth.In;

import java.util.List;

public interface CustomerArticleService {
//    list bài đăng cá nhân	/customer/article
//    lọc bài đăng theo trạng thái: chưa duyệt, đang đăng, đã ẩn	/customer/article?status={uncheck/active/hidden}
//    tìm kiếm bài đăng theo title	/customer/article?title={title}
    List<ArticleOutputDTO> listArticle(String email, String sort, Long start, Long end,
                                       Integer ward, Integer district, Integer city,
                                       Boolean roommate,
                                       String status, Boolean vip, String search,
                                       Integer page, Integer limit);

    //    đăng bài	/customer/article
    ArticleOutputDTO insertArticle(String email, ArticleInsertDTO articleInsertDTO)
            throws CustomException;

    //    sửa bài đăng	/customer/article
    ArticleOutputDTO updateArticle(String email, ArticleUpdateDTO articleUpdateDTO,
                                   Integer id)
            throws CustomException;

    //    ẩn bài đăng	/customer/article/hidden/{id}
    Message hiddenArticle(String email, Integer id) throws CustomException;

    //    xóa bài đăng	/customer/article/{id}
    Message deleteArticle(String email, Integer id) throws CustomException;

    //    gia hạn bài đăng	/customer/article/extension/{id}?date={int}
    Message extensionExp(String email, Integer id, Integer date) throws CustomException;

    //đăng lại bài đăng đã ẩn	/customer/article/post/{id}?days={int}
    Message postOldArticle(String email, Integer Id, Integer date) throws CustomException;

    //chi tiết bài đăng	/customer/article/{id}
    ArticleOutputDTO detailArticle(String email, Integer id) throws CustomException;
}