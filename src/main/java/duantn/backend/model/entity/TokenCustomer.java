package duantn.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenCustomer extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private Boolean type;
}
