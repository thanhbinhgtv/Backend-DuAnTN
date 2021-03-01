package duantn.backend.model.entity;

import duantn.backend.helper.DateHeper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int roomPrice;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Date postTime = DateHeper.now();

    @Column(nullable = false)
    private Date expiryDate;

    @Column(nullable = false)
    private boolean isVip;


    @OneToOne
    @JoinColumn(name = "serviceId")
    private Service service;

    @OneToOne
    @JoinColumn(name = "roommateId")
    private Roommate roommate;


    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staff staff;


    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "wardId")
    private Ward ward;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<FavoriteArticle> favoriteArticles;
}
