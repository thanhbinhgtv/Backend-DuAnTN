package duantn.backend.dao;

import duantn.backend.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findByCustomer_Email(String email, Pageable pageable);
    Page<Transaction> findByCustomer_EmailAndType(String email, Boolean type, Pageable pageable);
}
