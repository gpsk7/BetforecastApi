package net.javaguides.springboot.repository.master;

import net.javaguides.springboot.model.master.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {
    Trainer findByTrainerName(String trainerName);

    Page<Trainer> findAll(Pageable pageRequest);

    List<Trainer> findAll();
}
