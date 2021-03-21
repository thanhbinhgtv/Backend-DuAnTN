package duantn.backend.dao;

import duantn.backend.helper.Helper;
import duantn.backend.model.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Page<Staff> findByNameLikeAndDeletedFalseOrEmailLikeAndDeletedFalseOrPhoneLikeAndDeletedFalse(
            String name, String email, String phone, Pageable pageable
    );
    Page<Staff> findByNameLikeAndDeletedTrueOrEmailLikeAndDeletedTrueOrPhoneLikeAndDeletedTrue(
            String name, String email, String phone, Pageable pageable
    );
    Page<Staff> findByNameLikeOrEmailLikeOrPhoneLike(
            String name, String email, String phone, Pageable pageable
    );

    Staff findByStaffIdAndDeletedFalse(Integer staffId);

    List<Staff> findByDeletedTrue();

    Staff findByEmail(String email);

    Staff findByToken(String token);
}
