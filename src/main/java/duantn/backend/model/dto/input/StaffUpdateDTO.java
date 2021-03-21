package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StaffUpdateDTO {
    @NotNull(message = "Staff id không được trống")
    private Integer staffId;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được trống")
    private String email;

    @Size(min = 3, max=50, message = "Tên phải có từ 3 đến 50 kí tự")
    private String name;

    @Size(min = 9, max = 12, message = "Số CMND phải có từ 9-12 kí tự")
    private String cardId;

    @NotNull(message = "Ngày sinh không được trống")
    private long birthday;

    @NotNull(message = "Giới tính không được trống")
    private boolean gender;

    @NotNull(message = "Vai trò không được trống")
    private boolean role;

    @NotBlank(message = "Địa chỉ không được trống")
    private String address;

    @Size(min=9,max = 11,message = "SĐT phải có 9-11 kí tự")
    private String phone;

    private String image;
}
