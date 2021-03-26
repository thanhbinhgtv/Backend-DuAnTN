package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.ContactCustomerDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ArticleService {
    List<ArticleOutputDTO> listArticle(
            String sort,
            Long start, Long end,
            Integer ward,
            Integer district,
            Integer city,
            Boolean roommate,
            String status,
            Boolean vip,
            String search,
            Integer page,
            Integer limit
    );

//    contact với khách hàng (gửi mail cho khách hàng về bài viết này)	/admin/article/contact/{id}
    Message contactToCustomer(Integer id, ContactCustomerDTO contactCustomerDTO,
                              HttpServletRequest request) throws CustomException;
//    duyệt bài đăng (hiện) (gửi mail)	/admin/article/active/{id}
    Message activeArticle(Integer id, HttpServletRequest request) throws CustomException;
//    ẩn bài đăng (gửi mail)	/admin/article/block/{id}
    Message hiddenArticle(Integer id, String reason, HttpServletRequest request) throws CustomException;
}
