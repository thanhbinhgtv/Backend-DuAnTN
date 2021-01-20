package duantn.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Staffs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pass;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cardId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date dob;

    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<News> news;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<Advertisements> advertisements;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private Set<Articles> articles;


}
