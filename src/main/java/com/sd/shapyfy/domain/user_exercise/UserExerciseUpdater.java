package com.sd.shapyfy.domain.user_exercise;

import com.sd.shapyfy.domain.configuration.model.Attribute;
import com.sd.shapyfy.domain.configuration.model.ExerciseSet;
import com.sd.shapyfy.domain.plan.TrainingExerciseService;
import com.sd.shapyfy.domain.plan.TrainingProcess;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.session.SessionLookup;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserExerciseUpdater {

    private final SessionLookup sessionLookup;

    private final TrainingExerciseService trainingExerciseService;

    public void updateUpcomingSessionExercise(TrainingExercise trainingExercise, UserId userId) {
        log.info("Attempt to update upcoming session exercise {} for {}", trainingExercise, userId);
        List<Session> allUserFollowUps = sessionLookup.lookupFollowUp(userId);

        allUserFollowUps.stream()
                .map(session -> session.partsWithExercise(trainingExercise.exercise().id()))
                .flatMap(List::stream)
                .map(part -> part.exerciseById(trainingExercise.exercise().id()))
                .forEach(exerciseToUpdate -> trainingExerciseService.update(buildUpdateRequest(trainingExercise, exerciseToUpdate)));
    }

    private static TrainingProcess.UpdateTrainingExercise buildUpdateRequest(TrainingExercise trainingExercise, TrainingExercise exerciseToUpdate) {

        List<TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest> attributesToUpdate = exerciseToUpdate.attributes().stream()
                .map(attribute -> requestForAttribute(attribute, trainingExercise.attributes()))
                .toList();

        return new TrainingProcess.UpdateTrainingExercise(
                exerciseToUpdate.id(),
                false,
                attributesToUpdate,
                requestForSetsUpdateRequest(trainingExercise, exerciseToUpdate)
        );
    }

    private static TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest requestForAttribute(Attribute attribute, List<Attribute> updateFrom) {
        Optional<Attribute> possibleAttributeToUpdateFrom = updateFrom.stream().filter(updateFromAttribute -> Objects.equals(attribute.name(), updateFromAttribute.name())).findFirst();

        return new TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest(
                attribute.attributeId(),
                possibleAttributeToUpdateFrom.map(Attribute::value).orElse(attribute.name())
        );
    }

    private static List<TrainingProcess.UpdateTrainingExercise.UpdateSetRequest> requestForSetsUpdateRequest(TrainingExercise trainingExercise, TrainingExercise exerciseToUpdate) {
        List<TrainingProcess.UpdateTrainingExercise.UpdateSetRequest> setsToUpdate = new ArrayList<>();
        for (int iterator = 0; iterator < exerciseToUpdate.sets().size(); iterator++) {
            ExerciseSet setToUpdate = exerciseToUpdate.sets().get(iterator);
            ExerciseSet exerciseSet = trainingExercise.sets().get(iterator);
            setsToUpdate.add(new TrainingProcess.UpdateTrainingExercise.UpdateSetRequest(
                    setToUpdate.id(),
                    exerciseSet.reps(),
                    exerciseSet.weight(),
                    false,
                    setToUpdate.attributes().stream().map(a -> requestForAttribute(a, exerciseSet.attributes())).toList()));
        }

        return setsToUpdate;
    }
}
