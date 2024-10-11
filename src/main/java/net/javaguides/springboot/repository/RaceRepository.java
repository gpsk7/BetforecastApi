package net.javaguides.springboot.repository;

import net.javaguides.springboot.dto.RaceInfo;
import net.javaguides.springboot.model.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    List<Race> findByDate(String date);
    List<Race> findByCountryAndDate(String country, String date);
    @Query("SELECT DISTINCT new net.javaguides.springboot.dto.RaceInfo(r.raceNumber, r.time) " +
            "FROM Race r " +
            "WHERE r.country = :country AND r.date = :date AND r.course = :course")
    List<RaceInfo> findDistinctRaceNumberAndTimeByCountryAndDateAndCourse(
            @Param("country") String country,
            @Param("date") String date,
            @Param("course") String course
    );
    List<Race> findByCountryAndDateAndCourseAndRaceNumber(String country, String date, String course, String raceNumber);

    Race findByHorseName(String horseName);


    Page<Race> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT DISTINCT r.country, r.course FROM Race r WHERE SUBSTRING(r.date, 4, 2) = :month AND SUBSTRING(r.date, 7, 4) = :year")
    List<String> findDistinctCountriesByMonthAndYear(@Param("month") String month, @Param("year") String year);


}
