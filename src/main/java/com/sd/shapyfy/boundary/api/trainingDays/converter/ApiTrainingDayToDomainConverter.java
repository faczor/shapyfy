package com.sd.shapyfy.boundary.api.trainingDays.converter;

import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectExercisesToTrainingDayDocument;
import com.sd.shapyfy.domain.PlanExerciseSelector;
import com.sd.shapyfy.domain.PlanManagementAdapter;
import com.sd.shapyfy.domain.model.ExerciseId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiTrainingDayToDomainConverter {

    public List<PlanManagementAdapter.SelectedExercise> convertToSelectionDep(SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {

        return selectExercisesToTrainingDayDocument.selectedExercises().stream()
                .map(document -> new PlanManagementAdapter.SelectedExercise(
                        ExerciseId.of(document.exerciseId()),
                        document.sets(),
                        document.reps(),
                        document.weight())
                ).toList();
    }

    public List<PlanExerciseSelector.SelectedExercise> convertToSelection(SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {

        return selectExercisesToTrainingDayDocument.selectedExercises().stream()
                .map(document -> new PlanExerciseSelector.SelectedExercise(
                        ExerciseId.of(document.exerciseId()),
                        document.sets(),
                        document.reps(),
                        document.weight())
                ).toList();
    }
}
