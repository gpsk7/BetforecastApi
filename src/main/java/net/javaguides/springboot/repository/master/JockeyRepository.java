package net.javaguides.springboot.repository.master;

import net.javaguides.springboot.model.master.Jockey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JockeyRepository extends JpaRepository<Jockey, Long> {
    Jockey findByJockeyFirstName(String JockeyFirstName);

    Page<Jockey> findAll(Pageable pageRequest);

    List<Jockey> findAll();
}
