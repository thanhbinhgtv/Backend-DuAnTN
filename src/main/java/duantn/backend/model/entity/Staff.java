package duantn.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@EnableAutoConfiguration
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Staff extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer staffId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pass;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cardId;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private boolean role;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<Newspaper> news;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<Advertisement> advertisements;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<Article> articles;


}
