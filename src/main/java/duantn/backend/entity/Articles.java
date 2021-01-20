package duantn.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Articles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int articleId;

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
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date postTime;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date expiryDate;

    @Column(nullable = false)
    private boolean isVip;


    @OneToOne
    @JoinColumn(name = "serviceId")
    private Services service;

    @OneToOne
    @JoinColumn(name = "roommateId")
    private Roommates roommate;


    @ManyToOne
    @JoinColumn(name = "staffId")
    private Staffs staff;


    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customers customer;


    @ManyToOne
    @JoinColumn(name = "wardId")
    private Wards ward;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    private Set<FavoriteArticles> favoriteArticles;
}
