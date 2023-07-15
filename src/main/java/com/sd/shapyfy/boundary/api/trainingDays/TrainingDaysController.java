package com.sd.shapyfy.boundary.api.trainingDays;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectExercisesToTrainingDayDocument;
import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectedExercisesDocument;
import com.sd.shapyfy.boundary.api.trainingDays.converter.TrainingDayToDomainConverter;
import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.user.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/training_days")
public class TrainingDaysController {

    private final TrainingManagementAdapter trainingManagementAdapter;

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    @PatchMapping("/{training_day_id}")
    public ResponseEntity<SelectedExercisesDocument> fillTrainingDay(
            @PathVariable(name = "training_day_id") String trainingDayId,
            @RequestBody @Valid SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {
        log.info("Attempt to fill training day {} with {}", trainingDayId, selectExercisesToTrainingDayDocument);
        UserId userId = currentUserId();
        List<TrainingManagementAdapter.SelectedExercise> selectedExercises = trainingDayToDomainConverter.convertToSelection(selectExercisesToTrainingDayDocument);
        Training.TrainingDay trainingDay = trainingManagementAdapter.exercisesSelection(TrainingDayId.of(trainingDayId), selectedExercises, userId);

        return ResponseEntity.ok(SelectedExercisesDocument.from(trainingDay));
    }
}
