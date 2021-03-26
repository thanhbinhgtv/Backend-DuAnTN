package duantn.backend.dao;

import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.StaffArticle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffArticleRepository extends JpaRepository<StaffArticle, Integer> {
    StaffArticle findFirstByArticle_ArticleId(Integer id, Sort sort);
}
