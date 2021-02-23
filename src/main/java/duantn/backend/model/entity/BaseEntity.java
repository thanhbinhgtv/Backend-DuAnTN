package duantn.backend.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private boolean deleted =true;

    private Date timeCreated=new Date();

}
