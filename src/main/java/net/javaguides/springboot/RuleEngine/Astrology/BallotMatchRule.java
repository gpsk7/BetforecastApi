package net.javaguides.springboot.RuleEngine.Astrology;

import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.model.Race;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.List;

@Rule(priority = 2, name = "BallotMatchRule", description = "Check if horse ballot matches astrology planet number")
public class BallotMatchRule {

    @Condition
    public boolean when(@Fact("raceDto") RaceDto raceDto, @Fact("planetNumbers") List<String> planetNumbers) {
        return planetNumbers.contains(String.valueOf(raceDto.getSdHorseBallot()));
    }

    @Action
    public void then(@Fact("raceDto") RaceDto raceDtoBuilder) {
        raceDtoBuilder.setPredictionColor("Green");
    }
}
