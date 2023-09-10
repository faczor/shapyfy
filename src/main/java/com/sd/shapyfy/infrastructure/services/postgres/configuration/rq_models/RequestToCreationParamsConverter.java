package com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models;

import com.sd.shapyfy.domain.exercise.ExerciseFetcher;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration.SessionDayConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestToCreationParamsConverter {

    private final PostgresExerciseFetcher exerciseFetcher;

    public CreateConfigurationParams convert(TrainingPlanCreator.PlanConfiguration planConfiguration) {
        return new CreateConfigurationParams(
                planConfiguration.name(),
                planConfiguration.exerciseAttributes(),
                planConfiguration.setAttributes(),
                planConfiguration.sessionDayConfigurations().stream().map(this::convertDayConfiguration).toList()
        );
    }

    private CreateConfigurationParams.SessionDayConfiguration convertDayConfiguration(SessionDayConfiguration sessionDayConfiguration) {
        return new CreateConfigurationParams.SessionDayConfiguration(
                sessionDayConfiguration.name(),
                sessionDayConfiguration.dayType(),
                sessionDayConfiguration.selectedExercises().stream().map(this::convertSelectedExercises).toList()
        );
    }

    private CreateConfigurationParams.SessionDayConfiguration.SelectedExercise convertSelectedExercises(SessionDayConfiguration.SelectedExercise selectedExercise) {
        return new CreateConfigurationParams.SessionDayConfiguration.SelectedExercise(
                exerciseFetcher.fetchFor(selectedExercise.exerciseId()),
                selectedExercise.sets(),
                selectedExercise.reps(),
                selectedExercise.weight(),
                selectedExercise.secondRestBetweenSets()
        );
    }
}
