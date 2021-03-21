package duantn.backend.model.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StaffOutputDTO {
    private Integer staffId;

    private String email;

    private String name;

    private String cardId;

    private Long dob;

    private Boolean gender;

    private Boolean role;

    private String address;

    private String phone;

    private String image;
}
