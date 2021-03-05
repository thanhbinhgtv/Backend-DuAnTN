package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateDTO {
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
