package duantn.backend.dao;

import duantn.backend.entity.Staffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staffs, Integer> {


    @Query("FROM Staffs s WHERE s.email LIKE %:phone%")
    public List<Staffs> findByPhone(@Param("phone") String phone);

    @Query("FROM Staffs s WHERE s.email LIKE %:email%")
    public List<Staffs> findByEmail(@Param("email") String email);
}
