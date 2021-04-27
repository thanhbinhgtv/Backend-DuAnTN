package duantn.backend.dao;

import duantn.backend.model.entity.CountRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 28/04/2021
 * Time: 1:19 SA
 */

@Repository
public interface CountRequestRepository extends JpaRepository<CountRequest, Date> {
    CountRequest findFirstBy(Sort sort);
}
