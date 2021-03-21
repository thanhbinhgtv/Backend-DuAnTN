package duantn.backend.model.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được trống")
    String email;

    @Size(min = 6, max = 15, message = "Mật khẩu phải có 6-15 kí tự")
    String pass;
}
