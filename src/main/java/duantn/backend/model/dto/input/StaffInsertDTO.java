package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
public class StaffInsertDTO {

    private String email;

    private String pass;

    private String name;

    private String cardId;

    private long birthday;

    private boolean gender;

    private boolean role;

    private String address;

    private String phone;

    private String image;
}
