package net.javaguides.springboot.repository.master;

import net.javaguides.springboot.model.master.Horse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorseRepository extends CrudRepository<Horse, Long> {
    Horse findByName(String name);

    Page<Horse> findAll(Pageable pageRequest);

    List<Horse>findAll();

    @Query("select h from Horse h where h.name like %?1% or h.country like %?1%")
    List<Horse> findByLike(String name);
}
