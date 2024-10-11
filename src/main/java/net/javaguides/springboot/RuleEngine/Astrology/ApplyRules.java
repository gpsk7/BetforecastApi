package net.javaguides.springboot.RuleEngine.Astrology;

import net.javaguides.springboot.RuleEngine.Numerology.RuleHorseBallot;
import net.javaguides.springboot.RuleEngine.Util.RuleEngineeUtil;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.Race;
import net.javaguides.springboot.service.AstrologyService;
import net.javaguides.springboot.service.NatureDayValueService;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplyRules {

    @Autowired
    private AstrologyService astrologyService;

    @Autowired
    private NatureDayValueService natureDayValueService;

    private static final Map<String, String> planetNumber = Map.of(
            "saturn", "8", "jupiter", "3", "rahu", "4",
            "mars", "9", "sun", "1", "venus", "6",
            "ketu", "7", "mercury", "5", "moon", "2"
    );

    public List<RaceDto> prediction(List<Race> races, String date, String time) throws ApiException {
        Astrology astrology = astrologyService.findByDateAndTime(date, time);
        if (astrology == null) {
            throw new ApiException("Dasha or AntarDasha not found for this date", HttpStatus.BAD_REQUEST);
        }

        List<String> planetNumbers = getPlanetNumber(astrology);

        // Create rules engine and rules
        Rules rules = new Rules();
        rules.register(new BallotMatchRule());
        rules.register(new RuleHorseBallot());

        RulesEngine rulesEngine = new DefaultRulesEngine();

        // Create race DTOs with predictions
        List<RaceDto> raceDtos = races.stream().map(race -> {

            RaceDto raceDtoBuilder = RuleEngineeUtil.mapToRaceDto(race,"Red");

            // Create facts
            Facts facts = new Facts();
            facts.put("planetNumbers", planetNumbers);
            facts.put("raceDto", raceDtoBuilder);

            // Fire rules
            rulesEngine.fire(rules, facts);

            // Set default prediction color if no rule matches
            return raceDtoBuilder;

        }).collect(Collectors.toList());

        return raceDtos;
    }

    public List<String> getPlanetNumber(Astrology astrology) {
        try {
            return List.of(
                    planetNumber.get(astrology.getDasha().toLowerCase()),
                    planetNumber.get(astrology.getAntarDasha().toLowerCase()),
                    planetNumber.get(astrology.getOwnHouse())
            );
        } catch (NullPointerException e) {
            throw new ApiException("ASTROLOGY DATA IS MISSING FOR GIVEN DATE " + astrology.getDate() + " AND TIME " + astrology.getTime(), HttpStatus.BAD_REQUEST);
        }
    }
}
