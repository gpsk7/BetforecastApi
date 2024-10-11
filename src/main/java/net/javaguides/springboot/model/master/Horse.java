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
//@SQLDelete(sql = "UPDATE horse_master SET delete = true WHERE id=?")
//@Where(clause = "deleted=false")
@Table(name = "horse_master")
public class Horse extends AbstractEntity {
    private String name;
    private String breed;
    private String gender;
    private String color;
    private Integer age;
    private Float height;
    private Float weight;
    private Integer registrationNumber;
    private Integer count;
    private String country;
    private Integer speed;
    private String dam;
    private String sire;
//    private boolean deleted = Boolean.FALSE;
}
