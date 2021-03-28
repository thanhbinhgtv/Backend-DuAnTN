package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoommateInsertDTO {
    @NotNull(message = "Giới tính không được trống")
    private Boolean gender;

    @NotNull(message = "Số lượng người không được trống")
    private Integer quantity;

    private String description;
}
