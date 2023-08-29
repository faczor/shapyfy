package com.sd.shapyfy.boundary.api.dashboard.converter;

import com.sd.shapyfy.boundary.api.dashboard.contact.TrainingState;
import com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract;
import com.sd.shapyfy.domain.plan.model.StateForDate;
import com.sd.shapyfy.domain.plan.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.DayType.*;

@Component
public class DayStateToRestConverter {

    public UserDashboardContract convertToDashboard(List<Training> trainings, List<StateForDate> stateForDates) {

        List<UserDashboardContract.DayState> dayStates = stateForDates.stream().map(stateForDate -> new UserDashboardContract.DayState(
                        Optional.ofNullable(stateForDate.session()).map(c -> c.sessionId().getValue().toString()).orElse(null),
                        Optional.ofNullable(stateForDate.planId()).map(id -> id.getValue().toString()).orElse(null),
                        stateForDate.date(),
                        Optional.ofNullable(stateForDate.configurationDay()).map(day -> day.isTrainingDay() ? TRAINING_DAY : REST_DAY).orElse(NO_TRAINING)
                ))
                .toList();

        TrainingState trainingState = resolveState(trainings);

        return new UserDashboardContract(
                new UserDashboardContract.MetaDataDocument(trainingState),
                dayStates);
    }

    private TrainingState resolveState(List<Training> trainings) {
        return trainings.stream().filter(Training::isActive).findFirst().map(x -> TrainingState.ACTIVE).orElse(TrainingState.NO_TRAINING);
    }
}
