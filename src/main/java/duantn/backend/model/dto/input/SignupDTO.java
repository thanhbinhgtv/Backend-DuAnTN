package duantn.backend.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    @Size(min = 3, max=50, message = "NAME_MUST_BE_3_TO_50_CHARACTER")
    private String name;

    @NotNull(message = "GENDER_IS_NOT_NULL")
    private Boolean gender;

    @Email(message = "EMAIL_IS_NOT_VALID")
    @NotBlank(message = "EMAIL_IS_NOT_NULL")
    private String email;

    @Size(min = 6, max = 15, message = "PASSWORD_MUST_BE_3_TO_15_CHARACTER")
    private String pass;

    @Size(min = 9, max = 11, message = "PHONE_MUST_BE_9_TO_11_NUMBER")
    private String phone;
}
