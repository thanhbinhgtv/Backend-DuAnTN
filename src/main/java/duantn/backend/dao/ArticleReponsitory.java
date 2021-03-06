
package duantn.backend.dao;

import duantn.backend.model.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface ArticleReponsitory extends JpaRepository<Article, Integer> {


    // tìm kiếm theo id
    Article findByArticleId(Integer articleId);
    /// tìm kiếm bài viết theo thời gian giảm dần
    Page<Article> findByDeletedFalseOrderByPostTimeDesc(Pageable pageable);
    List<Article> findByDeletedFalseOrderByPostTimeDesc();

    List<Article> findByStatus(Boolean status);
    List<Article> findByPostTimeGreaterThanEqualAndPostTimeIsLessThanEqual
            (Date minDate, Date maxDate);

    List<Article> findByWard_District_City_CityIdAndWard_District_DistrictIdAndWard_WardId
            (Integer cityId, Integer districtId, Integer wardId);

    List<Article> findByWard_District_City_CityIdAndWard_District_DistrictId
            (Integer cityId, Integer districtId);

    List<Article> findByWard_District_City_CityId
            (Integer cityId);

    List<Article> findByTitleLikeOrPhoneLike(String title, String phone);
}
