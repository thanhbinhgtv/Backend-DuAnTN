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
public class Districts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int districtId;

    @Column(nullable = false)
    private String districtName;


    @ManyToOne
    @JoinColumn(name = "cityId")
    private Cities city;

    @OneToMany(mappedBy = "district", fetch = FetchType.EAGER)
    private Set<Wards> wards;
}
