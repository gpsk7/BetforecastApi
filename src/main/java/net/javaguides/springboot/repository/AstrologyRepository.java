package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.master.Horse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstrologyRepository extends JpaRepository<Astrology, Long> {
    Astrology findByDateAndTime(String date, String time);

    Page<Astrology> findAll(Pageable pageRequest);

}
