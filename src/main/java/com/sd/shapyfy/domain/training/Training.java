package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.training.TrainingAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.trainingDay.TrainingDayType;
import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

import java.time.DayOfWeek;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Value
public class Training {
    TrainingId id;

    UserId userId;

    String name;

    List<TrainingDay> trainingDays;

    public static Training initialize(UserId userId, TrainingAdapter.TrainingInitialConfiguration trainingInitialConfiguration) {
        return new Training(
                null,
                userId,
                trainingInitialConfiguration.getName().orElse(trainingInitialConfiguration.getSessionDayConfigurations().stream().map(SessionDayConfiguration::getName).collect(joining(","))),
                trainingInitialConfiguration.getSessionDayConfigurations().stream().map(configuration -> TrainingDay.create(configuration.getName(), configuration.getDayOfWeek(), configuration.getDayType())).toList()
        );
    }

    @Value
    public static class TrainingDay {

        TrainingDayId id;

        String name;

        DayOfWeek day;

        TrainingDayType dayType;

        private static TrainingDay create(String name, DayOfWeek day, TrainingDayType trainingDayType) {
            return new TrainingDay(null, name, day, trainingDayType);
        }
    }
}

