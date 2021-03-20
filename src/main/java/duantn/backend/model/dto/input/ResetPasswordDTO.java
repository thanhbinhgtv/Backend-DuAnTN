package duantn.backend.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    String token;
    String email;

    @Size(min = 6, max = 15, message = "PASSWORD_MUST_BE_3_TO_15_CHARACTER")
    String password;
}
