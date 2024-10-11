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
//@SQLDelete(sql = "UPDATE jockey_master SET delete = true WHERE id=?")
//@Where(clause = "deleted=false")
@Table(name = "jockey_master")
public class Jockey extends AbstractEntity {
    private Integer jockeyRegistrationId;
    private String jockeyFirstName;
    private String jockeyLastName;
    private Integer jockeyAge;
    private String jockeyGender;
    private Integer jockeyCount;
    private String jockeyCountry;
    private Integer jockeyExperienceYears;
    private Integer jockeyWins;
    private Integer jockeyLosses;
//    private boolean deleted = Boolean.FALSE;
}
