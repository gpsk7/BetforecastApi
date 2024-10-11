package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.MoonDay;
import net.javaguides.springboot.model.NatureOfDayAndValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoonDayRepository extends JpaRepository<MoonDay, Long> {
    Optional<MoonDay> findByDate(String date);

}
