package duantn.backend.model.dto.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MessageInputDTO {
    @NotNull(message = "Tin nhắn không được null")
    @Size(min = 3, message = "Tin nhắn phải có ít nhất 3 kí tự")
    String mess;
}
