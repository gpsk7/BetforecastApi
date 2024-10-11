package net.javaguides.springboot.model.master;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.javaguides.springboot.model.AbstractEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@SQLDelete(sql = "UPDATE trainer_master SET delete = true WHERE id=?")
//@Where(clause = "deleted=false")
@Table(name = "trainer_master")
public class Trainer extends AbstractEntity {
    private String trainerName;
    private Integer trainerCount;
    private Integer trainerRegistrationId;
    private String trainerCountry;
    private String trainerStable;
    private Integer trainerAge;
    private String trainerGender;
//    private boolean deleted = Boolean.FALSE;
}
