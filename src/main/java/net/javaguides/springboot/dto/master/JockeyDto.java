package net.javaguides.springboot.dto.master;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JockeyDto {
    private Long id;
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
}
