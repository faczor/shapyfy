package com.sd.shapyfy.boundary.api.trainingDays;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectExercisesToTrainingDayDocument;
import com.sd.shapyfy.boundary.api.trainingDays.contract.SelectedExercisesDocument;
import com.sd.shapyfy.boundary.api.trainingDays.converter.ApiTrainingDayToDomainConverter;
import com.sd.shapyfy.domain.PlanExerciseSelector;
import com.sd.shapyfy.domain.PlanManagementAdapter;
import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.TrainingDayId;
import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
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

    private final ApiTrainingDayToDomainConverter apiTrainingDayToDomainConverter;

    private final PlanExerciseSelector planExerciseSelector;

    @PatchMapping("/{training_day_id}")
    public ResponseEntity<SelectedExercisesDocument> fillTrainingDay(
            @PathVariable(name = "training_day_id") String trainingDayId,
            @RequestBody @Valid SelectExercisesToTrainingDayDocument selectExercisesToTrainingDayDocument) {
        log.info("Attempt to fill training day {} with {}", trainingDayId, selectExercisesToTrainingDayDocument);
        UserId userId = currentUserId();
        List<PlanExerciseSelector.SelectedExercise> selectedExercises = apiTrainingDayToDomainConverter.convertToSelection(selectExercisesToTrainingDayDocument);

        //TODO rename
        ConfigurationDay configurationDay = planExerciseSelector.select(ConfigurationDayId.of(trainingDayId), selectedExercises, userId);

        return ResponseEntity.ok(SelectedExercisesDocument.from(configurationDay));
    }
}
