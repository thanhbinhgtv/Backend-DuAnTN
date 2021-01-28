package duantn.backend.dao;

import duantn.backend.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<Article, Integer> {

}

