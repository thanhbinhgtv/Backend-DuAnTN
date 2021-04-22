package duantn.backend.component;

import duantn.backend.dao.ArticleRepository;
import duantn.backend.model.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class AppSchedule {
    final
    ArticleRepository articleRepository;

    final
    MailSender mailSender;

    public AppSchedule(ArticleRepository articleRepository, MailSender mailSender) {
        this.articleRepository = articleRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void AutoArticle() {
        List<Article> articleList=articleRepository.findByDeletedTrue();
        if(articleList.size()>0){
            for (Article article: articleList){
                //ẩn bài đăng hết hạn
                if(article.getExpTime().before(new Date())) article.setDeleted(false);

                //gửi mail bài đăng sắp hết hạn
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                if(new Date().getTime() - article.getExpTime().getTime() < 50*3600*1000){
                    if(article.getCustomer()!=null){
                        mailSender.send(
                                article.getCustomer().getEmail(),
                                "Bài đăng số: "+article.getArticleId()+" sắp hết hạn",
                                "<p><strong>Chúng tôi xin trân trọng thông báo:</strong></p>\n" +
                                        "<p>Bài đăng số: "+article.getArticleId()+"</p>\n" +
                                        "<p>Tiêu đề: "+article.getTitle()+"</p>\n" +
                                        "<p>Link: xxxx</p>\n" +
                                        "<p>Sẽ hết hạn vào ngày: <span style=\"color: #0000ff;\">"+sdf.format(article.getExpTime())+"</span></p>\n" +
                                        "<p>Nếu bạn muốn bài đăng tiếp tục được đăng vui lòng gia hạn bài đăng trước thời hạn trên.</p>",
                                "Xin cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi."
                        );
                    }
                }
                articleRepository.save(article);
            }
        }
    }


}
