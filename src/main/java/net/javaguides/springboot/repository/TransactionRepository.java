package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails,Long> {
}
