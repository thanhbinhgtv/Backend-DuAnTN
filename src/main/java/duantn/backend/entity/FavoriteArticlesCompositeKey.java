package duantn.backend.entity;

import lombok.*;

import javax.persistence.ManyToOne;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArticlesCompositeKey implements Serializable {
    public Customers customer;
    public Articles article;
}
