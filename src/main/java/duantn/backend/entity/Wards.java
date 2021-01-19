package duantn.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wards implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wardId;

    @Column(nullable = false)
    private String wardName;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private Districts district;

    @OneToMany(mappedBy = "ward", fetch = FetchType.EAGER)
    private Set<Articles> articles;
}
