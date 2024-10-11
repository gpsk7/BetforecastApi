package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.NatureOfDayAndValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NatureDayValueRepository extends JpaRepository<NatureOfDayAndValue, Long> {

    Optional<NatureOfDayAndValue> findByDate(String date);

    @Query("SELECT n.nakshatraNumber FROM NatureOfDayAndValue n WHERE n.nakshatra = :nakshatra")
    List<String> findByNakshatra(@Param("nakshatra") String nakshatra);

}
