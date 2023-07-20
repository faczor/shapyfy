package com.sd.shapyfy.domain.model;

import com.sd.shapyfy.domain.PlanManagementAdapter;
import com.sd.shapyfy.domain.PlanManagementAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import lombok.Value;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Value
public class Training {

    PlanId id;

    UserId userId;

    String name;

    List<TrainingDay> trainingDays;

    public static Training initialize(UserId userId, PlanManagementAdapter.TrainingInitialConfiguration trainingInitialConfiguration) {
        return new Training(
                null,
                userId,
                trainingInitialConfiguration.getName().orElse(trainingInitialConfiguration.getSessionDayConfigurations().stream().map(SessionDayConfiguration::getName).collect(joining(","))),
                trainingInitialConfiguration.getSessionDayConfigurations().stream().map(configuration -> TrainingDay.create(configuration.getName(), configuration.getDayOfWeek(), configuration.getDayType())).toList()
        );
    }

    public boolean isActive() {
        return trainingDays.stream().map(TrainingDay::getSessions)
                .flatMap(Collection::stream)
                .anyMatch(Session::isActive);
    }

    public boolean isDraft() {
        return trainingDays.stream().map(TrainingDay::getSessions)
                .flatMap(Collection::stream)
                .anyMatch(Session::isDraft);
    }

    public int restDaysAfterTraining() {
        TrainingDay lastTrainingDay = lastTrainingDay();
        int indexOfLastTrainingDay = trainingDays.indexOf(lastTrainingDay);

        return trainingDays.size() - 1 - indexOfLastTrainingDay;
    }

    public int restDaysBeforeTraining() {
        TrainingDay firstTrainingDay = firstTrainingDay();
        return trainingDays.indexOf(firstTrainingDay);
    }

    public TrainingDay firstTrainingDay() {
        return trainingDays.stream().filter(TrainingDay::isTrainingDay).findFirst().orElseThrow(() -> new IllegalStateException("Training has no training days"));
    }

    public TrainingDay lastTrainingDay() {
        for (int i = trainingDays.size() - 1; i >= 0; i--) {
            TrainingDay trainingDay = trainingDays.get(i);
            if (trainingDay.isTrainingDay()) {
                return trainingDay;
            }
        }

        //TODO proper exception
        throw new IllegalStateException("Training has no training days");
    }

}

