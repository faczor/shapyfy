package com.sd.shapyfy.boundary.api.trainingDays.converter;

import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectExercisesToTrainingDayDocument;
import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.exercises.ExerciseId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingDayToDomainConverter {

    public List<TrainingManagementAdapter.SelectedExercise> convertToSelection(SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {

        return selectExercisesToTrainingDayDocument.selectedExercises().stream()
                .map(document -> new TrainingManagementAdapter.SelectedExercise(
                        ExerciseId.of(document.exerciseId()),
                        document.sets(),
                        document.reps(),
                        document.weight())
                ).toList();
    }
}
