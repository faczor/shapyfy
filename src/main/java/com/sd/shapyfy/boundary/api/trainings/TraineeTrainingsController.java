package com.sd.shapyfy.boundary.api.trainings;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.contract.StartTrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.contract.TrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.converter.TrainingToDomainConverter;
import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.model.Training;
import com.sd.shapyfy.domain.model.TrainingId;
import com.sd.shapyfy.domain.model.TrainingDayId;
import com.sd.shapyfy.domain.model.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/trainings")
@RequiredArgsConstructor
public class TraineeTrainingsController {

    public final TrainingManagementAdapter trainingManagement;

    public final TrainingToDomainConverter trainingToDomainConverter;

    @PostMapping
    public ResponseEntity<TrainingDocument> createTraining(@Valid @RequestBody CreateTrainingDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        Training training = trainingManagement.create(trainingToDomainConverter.convertForCreation(document), userId);
        return ResponseEntity.ok(TrainingDocument.from(training));
    }

    @PutMapping("/{training_id}/activations")
    public ResponseEntity<Void> finishConfiguration(
            @PathVariable("training_id") String pathVariableTrainingId,
            @Valid @RequestBody StartTrainingDocument document) {
        TrainingId trainingId = TrainingId.of(pathVariableTrainingId);
        log.info("Attempt to activate training {} with params {}", trainingId, document);

        trainingManagement.activate(trainingId, TrainingDayId.of(document.trainingDayStartId()), document.startDate());

        return ResponseEntity.ok().build();
    }
}
