package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoommateUpdateDTO {
    @NotNull(message = "Roommate Id không được trống")
    private  Integer roommateId;

    @NotNull(message = "Giới tính không được trống")
    private Boolean gender;

    @NotNull(message = "Số lượng người không được trống")
    private Integer quantity;

    private String description;
}
