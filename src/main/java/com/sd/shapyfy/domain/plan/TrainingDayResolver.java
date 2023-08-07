package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.configuration.TrainingLookup;
import com.sd.shapyfy.domain.plan.model.StateForDate;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

//Maybe this should be moved to the Lookup?
//Maybe this should be moved to the Converter?
@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingDayResolver {

    private final TrainingLookup trainingLookup;

    public List<StateForDate> resolveForDates(LocalDate from, LocalDate to, UserId userId) {
        List<Training> trainings = trainingLookup.trainingsFor(userId);
        //TODO select proper training?
        if (trainings.isEmpty()) {
            return List.of();
        }

        Training training = trainings.get(0);


        return new DateRange(from, to).datesWithinRange().map(training::stateFor).toList();
    }

}
