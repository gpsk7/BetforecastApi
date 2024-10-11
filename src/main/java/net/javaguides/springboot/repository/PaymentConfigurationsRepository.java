package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.PaymentConfigurations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentConfigurationsRepository extends JpaRepository<PaymentConfigurations, Long> {
    PaymentConfigurations findFirstByOrderByIdAsc();
}
