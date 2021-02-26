package duantn.backend.model.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import duantn.backend.model.entity.Advertisement;
import duantn.backend.model.entity.Staff;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Getter
@Setter
public class AdvertisementOutputDTO {
    public AdvertisementOutputDTO(Advertisement advertisement){
        this.advertisementId=advertisement.getAdvertisementId();
        this.title=advertisement.getTitle();
        this.content=advertisement.getContent();
        this.image=advertisement.getImage();
        this.staffId=advertisement.getStaff().getStaffId();
    }
    private Integer advertisementId;
    private String title;
    private String content;
    private String image;
    private Integer staffId;
}
