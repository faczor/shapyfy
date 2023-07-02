package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.trainingDay.TrainingDayType;
import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface TrainingAdapter {

    Training createTraining(TrainingInitialConfiguration trainingInitialConfiguration, UserId userId);

    @Value(staticConstructor = "of")
    class TrainingInitialConfiguration {
        String name;

        List<SessionDayConfiguration> sessionDayConfigurations;

        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Value(staticConstructor = "of")
        public static class SessionDayConfiguration {
            String name;
            DayOfWeek dayOfWeek;
            TrainingDayType dayType;
        }
    }
}
