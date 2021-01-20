package duantn.backend.controller.admin;

import duantn.backend.dao.ArticlesRepository;
import duantn.backend.entity.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ArticlesManager {
    final
    ArticlesRepository articlesRepository;

    @Autowired
    public ArticlesManager(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    //    list bài đăng	GET/admin/articles
    public List<Articles> listArticles(){
        return articlesRepository.findAll();
    }

//    xếp bài đăng theo ngày tăng dần	GET/admin/articles?post-time=asc

//    xếp bài đăng theo ngày giảm dần	GET/admin/articles?post-time=desc
//    lọc bài đăng theo trạng thái	GET/admin/articles?status={status}
//    lọc bài đăng theo tháng	GET/admin/articles?year={year}&month={month}
//    lọc bài đăng theo quận huyện	GET/admin/articles?district={district}
//    tìm kiếm bài đăng theo title	GET/admin/articles?title={title}
//    tìm kiếm bài đăng sđt	GET/admin/articles?phone={phone}
//    duyệt bài đăng (hiện)	GET/admin/articles/disable
//    ẩn bài đăng	GET/admin/articles/enable

}
