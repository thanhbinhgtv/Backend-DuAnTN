package duantn.backend.dao;

import duantn.backend.model.entity.TokenCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenCustomerRepository extends JpaRepository<TokenCustomer, Integer> {
    TokenCustomer findByToken(String token);
}
