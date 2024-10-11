package net.javaguides.springboot.repository;

import net.javaguides.springboot.dto.InplayDto;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.Inplay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InplayRepository extends JpaRepository<Inplay, Long> {
    Page<Inplay> findAll(Pageable pageRequest);
    Inplay findByDateAndTime(String date, double time);

    @Query("SELECT e FROM Inplay e WHERE " +
            "e.time LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "e.date LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.course) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.country) LIKE LOWER(CONCAT('%', :name, '%'))") // Exact match on date string
    List<Inplay> search(String name);
}
