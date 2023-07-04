package com.sd.shapyfy.boundary.api.trainings;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.contract.TrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.converter.TrainingToDomainConverter;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingAdapter;
import com.sd.shapyfy.domain.user.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/trainings")
@RequiredArgsConstructor
public class TraineeTrainingsController {

    public final TrainingAdapter trainingAdapter;

    public final TrainingToDomainConverter trainingToDomainConverter;

    @PostMapping
    public ResponseEntity<TrainingDocument> createTraining(@Valid @RequestBody CreateTrainingDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        Training training = trainingAdapter.createTraining(trainingToDomainConverter.convertForCreation(document), userId);
        return ResponseEntity.ok(TrainingDocument.from(training));
    }

    @GetMapping("/active")
    public ResponseEntity<Object> listTrainings() {
        return null;
    }
}
