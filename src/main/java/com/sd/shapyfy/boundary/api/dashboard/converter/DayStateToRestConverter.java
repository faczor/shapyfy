package com.sd.shapyfy.boundary.api.dashboard.converter;

import com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract;
import com.sd.shapyfy.domain.plan.model.StateForDate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.StateType.*;

@Component
public class DayStateToRestConverter {

    public UserDashboardContract convertToDashboard(List<StateForDate> stateForDates) {

        List<UserDashboardContract.DayState> dayStates = stateForDates.stream().map(stateForDate -> new UserDashboardContract.DayState(
                        stateForDate.date(),
                        Optional.ofNullable(stateForDate.configurationDay()).map(day -> day.isTrainingDay() ? TRAINING_DAY : REST_DAY).orElse(NO_TRAINING)
                ))
                .toList();

        return new UserDashboardContract(dayStates);
    }
}
