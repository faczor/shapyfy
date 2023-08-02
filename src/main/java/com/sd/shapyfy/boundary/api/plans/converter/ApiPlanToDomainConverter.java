package com.sd.shapyfy.boundary.api.plans.converter;

import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.ConfigurationDaySelectedExercisesDocument;
import com.sd.shapyfy.domain.configuration.PlanExerciseSelector;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiPlanToDomainConverter {

    public TrainingPlanCreator.PlanCreationInitialConfigurationParams convertForCreation(CreatePlanDocument document) {
        return new TrainingPlanCreator.PlanCreationInitialConfigurationParams(
                document.name(),
                document.dayConfigurations().stream()
                        .map(dayConfiguration -> new SessionDayConfiguration(dayConfiguration.name(), dayConfiguration.type()))
                        .toList());
    }

    public List<PlanExerciseSelector.SelectedExercise> convertForExercisesSelection(ConfigurationDaySelectedExercisesDocument configurationDaySelectedExercisesDocument) {

        return configurationDaySelectedExercisesDocument.selectedExercises().stream()
                .map(document -> new PlanExerciseSelector.SelectedExercise(
                        ExerciseId.of(document.exerciseId()),
                        document.sets(),
                        document.reps(),
                        document.weight())
                ).toList();
    }
}
