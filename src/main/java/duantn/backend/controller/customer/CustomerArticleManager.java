package duantn.backend.controller.customer;

import duantn.backend.authentication.CustomException;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.service.CustomerArticleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerArticleManager {
    final
    CustomerArticleService customerArticleService;

    public CustomerArticleManager(CustomerArticleService customerArticleService) {
        this.customerArticleService = customerArticleService;
    }

    //    list bài đăng cá nhân	/customer/article
//    lọc bài đăng theo trạng thái: chưa duyệt, đang đăng, đã ẩn	/customer/article?status={uncheck/active/hidden}
//    xếp bài đăng theo thời gian (updateTime) tăng/giảm dần	/admin/article?sort={asc/desc}
//    lọc bài đang theo loại: thuê phòng/ ở ghép	/admin/article?roomate={true/false}
//    lọc bài đăng theo thành phố / huyện / phường	/admin/article?city= hoặc district= hoặc ward=
//    lọc bài đăng theo khoảng thời gian (updateTime)	/admin/article?start={millisecond}&end={millisecond}
//    lọc bài đăng theo isVip	/admin/article?vip={true/false}
//    tìm kiếm bài đăng theo title	/customer/article?title={title}
    @GetMapping("/article")
    public List<ArticleOutputDTO> listArticle(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long start, @RequestParam(required = false) Long end,
            @RequestParam(required = false) Integer ward,
            @RequestParam(required = false) Integer district,
            @RequestParam(required = false) Integer city,
            @RequestParam(required = false) Boolean roommate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean vip,
            @RequestParam(required = false) String search,
            @RequestParam Integer page,
            @RequestParam Integer limit,
            HttpServletRequest request
    ) {
        String email = (String) request.getAttribute("email");
        return customerArticleService.listArticle(email, sort, start, end, ward, district, city,
                roommate, status, vip, search, page, limit);
    }

    //    chi tiết bài đăng	/customer/article/{id}
    @GetMapping("/article/{id}")
    public ArticleOutputDTO detailArticle(@PathVariable Integer id,
                                          HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.detailArticle(email, id);
    }

    //    đăng bài	/customer/article
    @PostMapping("/article")
    public ArticleOutputDTO insertArticle(@Valid @RequestBody ArticleInsertDTO articleInsertDTO,
                                          HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.insertArticle(email, articleInsertDTO);
    }

    //    sửa bài đăng	/customer/article
    @PutMapping("/article/{id}")
    public ArticleOutputDTO updateArticle(@Valid @RequestBody ArticleUpdateDTO articleUpdateDTO,
                                          Integer id,
                                          HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.updateArticle(email, articleUpdateDTO, id);
    }

    //    ẩn bài đăng	/customer/article/hidden/{id}
    @GetMapping("/article/hidden/{id}")
    public Message hiddenArticle(@PathVariable Integer id,
                                 HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.hiddenArticle(email, id);
    }

    //    xóa bài đăng	/customer/article/{id}
    @DeleteMapping("/article/{id}")
    public Message deleteArticle(@PathVariable Integer id,
                                 HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.deleteArticle(email, id);
    }

    //    gia hạn bài đăng	/customer/article/extension/{id}?days={int}
    @GetMapping("/article/extension/{id}")
    public Message extensionExp(@PathVariable Integer id,
                                @RequestParam Integer days,
                                HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.extensionExp(email, id, days);
    }

    //    đăng lại bài đăng đã ẩn	/customer/article/post/{id}?days={int}
    @GetMapping("/article/post/{id}")
    public Message postOldArticle(@PathVariable Integer id,
                                  @RequestParam Integer days,
                                  HttpServletRequest request) throws CustomException {
        String email = (String) request.getAttribute("email");
        return customerArticleService.postOldArticle(email, id, days);
    }
}
