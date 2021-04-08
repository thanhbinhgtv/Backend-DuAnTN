package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.output.Message;

import java.util.List;
import java.util.Map;

public interface ArticleFavoriteService {
//    lưu bài quan tâm	/customer/favorite-article/add?id={id}
    Message addArticle(String email,Integer id) throws CustomException;
//    danh sách bài viết quan tâm của người dung	/customer/favorite-article
    List<Map<String, String>> listArticle(String email,
                                          Integer page, Integer limit) throws CustomException;
//    xóa bài viết quan tâm	/customer/favorite-article/{id}
    Message deleteArticle(String email, Integer id) throws CustomException;
}
