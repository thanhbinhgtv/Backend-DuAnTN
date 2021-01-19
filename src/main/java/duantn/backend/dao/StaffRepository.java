package duantn.backend.dao;

import duantn.backend.entity.Staffs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staffs, Integer> {

}
