package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
    List<UserSubscription> findByUserIdOrderByValidFrom(String userId);
    Optional<UserSubscription> findTopByUserIdOrderByValidFromDesc(String userId);
}


