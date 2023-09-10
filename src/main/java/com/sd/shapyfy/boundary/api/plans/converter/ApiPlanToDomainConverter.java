package com.sd.shapyfy.boundary.api.plans.converter;

import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration.SessionDayConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration.SessionDayConfiguration.SelectedExercise;
import org.springframework.stereotype.Component;

@Component
public class ApiPlanToDomainConverter {

    public TrainingPlanCreator.PlanConfiguration convertForCreation(CreatePlanDocument document) {
        return new TrainingPlanCreator.PlanConfiguration(
                document.name(),
                document.exerciseAttributes(),
                document.setAttributes(),
                document.dayConfigurations().stream()
                        .map(dayConfiguration -> new SessionDayConfiguration(
                                dayConfiguration.name(),
                                dayConfiguration.type(),
                                dayConfiguration.selectedExercises().stream().map(exercise -> new SelectedExercise(
                                        ExerciseId.of(exercise.exerciseId()),
                                        exercise.sets(),
                                        exercise.reps(),
                                        exercise.weight(),
                                        exercise.timeRestBetweenSets().toSeconds())
                                ).toList()))
                        .toList());
    }

}
