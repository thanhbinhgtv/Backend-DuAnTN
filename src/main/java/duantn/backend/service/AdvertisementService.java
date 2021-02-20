package duantn.backend.service;

import duantn.backend.model.dto.input.AdvertisementInsertDTO;
import duantn.backend.model.dto.input.AdvertisementUpdateDTO;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.AdvertisementOutputDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdvertisementService{
    //danh sách bài viêt
    List<AdvertisementOutputDTO> listAdvertisement(Integer page, Integer limit);

    //thêm bài viết
    ResponseEntity<?> insertAdvertisement(AdvertisementInsertDTO advertisementInsertDTO);

    //cập nhật bài viết
    ResponseEntity<?> updateAdvertisement(AdvertisementUpdateDTO advertisementUpdateDTO);

    //xóa bài viết
    ResponseEntity<String> deleteAdvertisement(Integer id);
//
//    //duyệt bài viết
//    ResponseEntity<String> activeArticle(Integer id);
//
//    //xem bài viết
//    ResponseEntity<?> findOneArticle(Integer id);

}
