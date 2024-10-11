package net.javaguides.springboot.RuleEngine.Numerology;

import net.javaguides.springboot.RuleEngine.Util.RuleEngineeUtil;
import net.javaguides.springboot.dto.RaceDto;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(priority = 1,name = "DrawnStallConditionRule", description = "Check if drawn stall matches criteria after horse ballot is even")

public class RuleHorseBallot {

    @Condition
    public boolean when(Facts facts) {
        return true;
    }

    @Action
    public void then(Facts facts) {
        RaceDto raceDto = facts.get("raceDto");

        // Get the horseBallot and drawnStall values
        int ballotNumber = Integer.parseInt(raceDto.getHorseBallot());
        int drawnStall = raceDto.getDrawnStall();

        // Reduce horseBallot and drawnStall to single digits if greater than 9
        if (ballotNumber > 9) {
            ballotNumber = RuleEngineeUtil.reduceToSingleDigit(ballotNumber);
        }

        if (drawnStall > 9) {
            drawnStall = RuleEngineeUtil.reduceToSingleDigit(drawnStall);
        }

        raceDto.setSdHorseBallot((ballotNumber));
        raceDto.setSdDrawnStall(drawnStall);

        System.out.println(raceDto.getHorseBallot() +" "+ raceDto.getDrawnStall()  + " orginal "
                + raceDto.getSdHorseBallot() + " and " + raceDto.getSdDrawnStall());}

}