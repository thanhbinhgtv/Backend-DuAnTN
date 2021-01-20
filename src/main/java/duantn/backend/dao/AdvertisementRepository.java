package duantn.backend.dao;

import duantn.backend.entity.Advertisements;
import duantn.backend.entity.Staffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisements, Integer> {

    @Query("FROM Advertisements a WHERE a.title LIKE %:title%")
    public List<Advertisements> findByTitle(@Param("title") String title);
}
