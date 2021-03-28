package duantn.backend.model.dto.input;

import duantn.backend.helper.DateHelper;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class ArticleUpdateDTO {
    @Size(min = 3, max = 220, message = "Tiêu đề phải có từ 3-220 kí tự")
    @NotNull(message = "Tiêu đề không được trống")
    private String title;

    @Size(min = 3, max = 65000, message = "Nội dung phải có ít nhất 3 kí tự")
    @NotNull(message = "Nội dung không được trống")
    private String content;

    @NotBlank(message = "Ảnh không được trống")
    private String image;

    @Min(value = 1000, message = "Giá phòng phải lớn hơn 1000 đồng")
    @NotNull(message = "Giá phòng không được trống")
    private Integer roomPrice;

    private String description;

    @NotNull(message = "Vip không được trống")
    private Boolean vip;

    //numberDate

    @Min(value = 1000, message = "Giá nước phải lớn hơn 1000 đồng")
    private Integer waterPrice;

    @Min(value = 1000, message = "Giá điện phải lớn hơn 1000 đồng")
    private Integer electricPrice;

    @Min(value = 1000, message = "Giá wifi phải lớn hơn 1000 đồng")
    private Integer wifiPrice;

    //lưu ý
    private RoommateUpdateDTO roommateUpdateDTO;

    //token => customer (hoac cho nhap truc tiep)

    @NotNull(message = "Phường không được trống")
    private Integer wardId;
}
