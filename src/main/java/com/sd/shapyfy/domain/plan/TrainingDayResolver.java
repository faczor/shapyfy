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
import java.util.Optional;

//Maybe this should be moved to the Lookup?
//Maybe this should be moved to the Converter?
@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingDayResolver {

    private final TrainingLookup trainingLookup;

    public List<StateForDate> resolveForDates(LocalDate from, LocalDate to, UserId userId) {
        List<Training> trainings = trainingLookup.trainingsFor(userId);

        Optional<Training> possibleTraining = trainings.stream().filter(Training::isActive).findFirst();

        return possibleTraining.map(training -> new DateRange(from, to).datesWithinRange().map(training::stateFor).toList()).orElse(empty(from, to));

    }


    private List<StateForDate> empty(LocalDate from, LocalDate to) {
        return new DateRange(from, to).datesWithinRange().map(StateForDate::noTraining).toList();
    }
}
