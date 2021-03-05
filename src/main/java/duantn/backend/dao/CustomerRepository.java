package duantn.backend.dao;

import duantn.backend.model.entity.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByDeletedFalse(Sort sort);

    List<Customer> findByDeletedFalse();
    List<Customer> findByNameLikeAndDeletedFalse(String name);
    Customer findByCustomerIdAndDeletedFalse (Integer customerId);
}
