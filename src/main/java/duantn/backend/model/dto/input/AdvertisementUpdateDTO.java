package duantn.backend.model.dto.input;

import duantn.backend.model.entity.Staff;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertisementUpdateDTO {
    private Integer advertisementId;
    private String title;
    private String content;
    private String image;
}
