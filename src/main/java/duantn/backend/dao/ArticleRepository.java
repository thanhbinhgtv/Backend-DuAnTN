
package duantn.backend.dao;

import duantn.backend.model.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>, CustomArticleRepository{
    Article findByDeletedFalseAndArticleId(Integer id);
    Article findByArticleId(Integer id);
}