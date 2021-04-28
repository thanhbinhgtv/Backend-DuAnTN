package duantn.backend.dao;

import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.Comment;
import duantn.backend.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 28/04/2021
 * Time: 5:08 CH
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findByArticleAndCustomer(Article article, Customer customer);
}
