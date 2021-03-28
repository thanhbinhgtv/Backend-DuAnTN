package duantn.backend.model.dto.input;


import duantn.backend.model.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class ArticleInsertDTO {
    @Size(min = 3, max = 220, message = "Tiêu đề phải có từ 3-220 kí tự")
    @NotNull(message = "Tiêu đề không được trống")
    private String title;

    @Size(min = 3, max = 65000, message = "Nội dung phải có ít nhất 3 kí tự")
    @NotNull(message = "Nội dung không được trống")
    private String content;

    @NotBlank(message = "Ảnh không được trống")
    private String image;

    @Min(value = 1000, message = "Giá phòng phải lớn hơn 1000 đồng")
    @NotNull(message = "Giá phòng đăng không được trống")
    private int roomPrice;

    private String description;

    @Min(value = 1, message = "Số ngày đăng phải lớn hơn 1")
    @NotNull(message = "Số ngày đăng không được trống")
    private Integer numberDate;

    @NotNull(message = "Vip không được trống")
    private Boolean vip;

    @Min(value = 1000, message = "Giá nước phải lớn hơn 1000 đồng")
    private Integer waterPrice;

    @Min(value = 1000, message = "Giá điện phải lớn hơn 1000 đồng")
    private Integer electricPrice;

    @Min(value = 1000, message = "Giá wifi phải lớn hơn 1000 đồng")
    private Integer wifiPrice;

    //lưu ý
    private RoommateInsertDTO roommateInsertDTO;

    //token => customer (hoac cho nhap truc tiep)

    @NotNull(message = "Phường không được trống")
    private Integer wardId;
}