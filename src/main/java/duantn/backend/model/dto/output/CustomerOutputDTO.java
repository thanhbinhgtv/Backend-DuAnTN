package duantn.backend.model.dto.output;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class CustomerOutputDTO {

    private Integer customerId;
    private String name;
    private boolean gender;
    private String email;
    private String pass;
    private String address;
    private String phone;
    private String cardId;
    private int accountBalance;
}
