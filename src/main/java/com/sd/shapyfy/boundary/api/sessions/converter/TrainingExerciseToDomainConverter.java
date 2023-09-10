package com.sd.shapyfy.boundary.api.sessions.converter;

import com.sd.shapyfy.boundary.api.sessions.contract.AttributeFillValueDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.CompleteExerciseDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.CompleteSetDocument;
import com.sd.shapyfy.domain.configuration.model.AttributeId;
import com.sd.shapyfy.domain.configuration.model.ExerciseSetId;
import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.plan.TrainingProcess;
import org.springframework.stereotype.Component;

@Component
public class TrainingExerciseToDomainConverter {

    public TrainingProcess.UpdateTrainingExercise convertForFinishExercise(TrainingExerciseId trainingExerciseId, CompleteExerciseDocument document) {
        return new TrainingProcess.UpdateTrainingExercise(
                trainingExerciseId,
                true,
                document.attributes().stream().map(this::convertAttribute).toList(),
                document.sets().stream().map(this::convertSet).toList()
        );
    }

    public TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest convertAttribute(AttributeFillValueDocument attributeFillValueDocument) {
        return new TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest(
                AttributeId.of(attributeFillValueDocument.id()),
                attributeFillValueDocument.value()
        );
    }

    public TrainingProcess.UpdateTrainingExercise.UpdateSetRequest convertSet(CompleteSetDocument completeSetDocument) {
        return new TrainingProcess.UpdateTrainingExercise.UpdateSetRequest(
                ExerciseSetId.of(completeSetDocument.id()),
                completeSetDocument.reps(),
                completeSetDocument.weight(),
                true,
                completeSetDocument.attributes().stream().map(this::convertAttribute).toList()
        );
    }

}
