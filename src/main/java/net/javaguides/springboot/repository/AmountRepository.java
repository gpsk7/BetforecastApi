package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountRepository extends JpaRepository<Amount, Long> {

}
