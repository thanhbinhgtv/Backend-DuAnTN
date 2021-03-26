package duantn.backend.model.dto.output;

import duantn.backend.model.entity.Roommate;
import duantn.backend.model.entity.Service;
import duantn.backend.model.entity.Ward;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ArticleOutputDTO {
    private Integer articleId;

    private String title;

    private String content;

    private String image;

    private Integer roomPrice;

    private String description;

    private Long createTime;

    private Long lastUpdateTime;

    private Long expDate;

    private Boolean vip;

    private String status;

    private Service service;

    private Roommate roommate;

    private Map<String, String> customer;

    private Map<String, String> moderator;

    private Map<String, String> address;
    //private Ward ward;
}
