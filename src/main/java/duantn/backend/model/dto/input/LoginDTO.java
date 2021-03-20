package duantn.backend.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Email(message = "EMAIL_IS_NOT_VALID")
    String email;

    @Size(min = 6, max = 15, message = "PASSWORD_MUST_BE_3_TO_15_CHARACTER")
    String pass;
}
