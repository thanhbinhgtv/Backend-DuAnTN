package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerUpdateDTO {
    @NotNull(message = "Customer id không được trống")
    private Integer customerId;

    @Size(min = 3, max=50, message = "Tên phải có từ 3 đến 50 kí tự")
    private String name;

    @NotNull(message = "Giới tính không được trống")
    private boolean gender;

    private String address;

    @Size(min=9,max = 11,message = "SĐT phải có 9-11 kí tự")
    private String phone;

    private String cardId;

    @NotNull(message = "Ngày sinh không được trống")
    private long birthday;

    @NotBlank(message = "Ảnh không được trống")
    private String image;
}
