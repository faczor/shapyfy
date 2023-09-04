package com.sd.shapyfy.boundary.api.training_day.converter;

import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.TimeAmountDocument;
import com.sd.shapyfy.boundary.api.training_day.contract.AttributeDocument;
import com.sd.shapyfy.boundary.api.training_day.contract.ExerciseProcessDocument;
import com.sd.shapyfy.boundary.api.training_day.contract.SetDocument;
import com.sd.shapyfy.domain.plan.TrainingDayProcess.SessionExerciseWithPreviousOccurrences;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingExerciseToApiConverter {

    //TODO new document :)
    public ExerciseProcessDocument convertOnRun(SessionExerciseWithPreviousOccurrences sessionExerciseWithPreviousOccurrences) {
        TrainingExercise trainingExercise = sessionExerciseWithPreviousOccurrences.trainingExercise();
        List<TrainingExercise> exercises = sessionExerciseWithPreviousOccurrences.finishedExerciseOccurrences();

        return new ExerciseProcessDocument(
                trainingExercise.id().getValue(),
                ExerciseDocument.from(trainingExercise.exercise()),
                TimeAmountDocument.fromSeconds(trainingExercise.restBetweenSets()),
                trainingExercise.isFinished(),
                trainingExercise.attributes().stream().map(AttributeDocument::from).toList(),
                trainingExercise.sets().stream().map(SetDocument::from).toList(),
                exercises.stream().map(this::convertHistoricalDocument).toList()
        );
    }

    private ExerciseProcessDocument.ExerciseHistory convertHistoricalDocument(TrainingExercise trainingExercise) {
        return new ExerciseProcessDocument.ExerciseHistory(
                null, //TODO provide date of exercise
                trainingExercise.sets().stream().map(e -> new ExerciseProcessDocument.ExerciseHistory.SetHistory(
                        e.reps(),
                        e.weight(),
                        e.attributes().stream().map(AttributeDocument::from).toList()
                )).toList()
        );
    }
}
