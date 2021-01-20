package duantn.backend.entity;

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
public class Roommates implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roommateId;

    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = true)
    private String description;
}
