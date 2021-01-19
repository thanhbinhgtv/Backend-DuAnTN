package duantn.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(FavoriteArticlesCompositeKey.class)
public class FavoriteArticles implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "articleId")
    private Articles article;

    @Id
    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customers customer;
}
