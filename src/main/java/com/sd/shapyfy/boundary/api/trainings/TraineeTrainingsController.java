package com.sd.shapyfy.boundary.api.trainings;

import com.sd.shapyfy.boundary.api.trainings.contract.TrainingDocument;
import com.sd.shapyfy.boundary.api.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingAdapter;
import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/trainings")
public class TraineeTrainingsController {

    public final TrainingAdapter trainingAdapter;

    @GetMapping("/active")
    public ResponseEntity<Object> listTrainings() {
        return null;
    }

    @PostMapping
    public ResponseEntity<TrainingDocument> createTraining(
            @RequestBody CreateTrainingDocument document,
            @RequestHeader(name = "Authorization") String authToken) {
        log.info("Auth token {}", authToken);
        log.info("Attempt to create training {}", document);
        Training training = trainingAdapter.createTraining(UserId.of(document.getUserId()));
        return ResponseEntity.ok(TrainingDocument.from(training));
    }
}
