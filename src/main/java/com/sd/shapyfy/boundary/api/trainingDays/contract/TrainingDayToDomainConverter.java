package com.sd.shapyfy.boundary.api.trainingDays.contract;

import com.sd.shapyfy.domain.exercises.ExerciseId;
import com.sd.shapyfy.domain.trainingDay.TrainingDaysAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingDayToDomainConverter {

    public List<TrainingDaysAdapter.ExerciseDetails> convertToSelection(SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {

        return selectExercisesToTrainingDayDocument.selectedExercises().stream()
                .map(document -> new TrainingDaysAdapter.ExerciseDetails(
                        ExerciseId.of(document.exerciseId()),
                        document.sets(),
                        document.reps(),
                        document.weight())
                ).toList();
    }
}
