package com.sd.shapyfy.boundary.trainings;

import com.sd.shapyfy.boundary.trainings.contract.CreateTrainingDocument;
import com.sd.shapyfy.boundary.trainings.contract.TrainingDocument;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingAdapter;
import com.sd.shapyfy.domain.user.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TraineeTrainingsController {

    public final TrainingAdapter trainingAdapter;

    @GetMapping("/active")
    public ResponseEntity<Object> listTrainings() {
        return null;
    }

    @PostMapping
    public ResponseEntity<TrainingDocument> createTraining(@RequestBody @Valid CreateTrainingDocument document) {
        log.info("Attempt to create training {}", document);
        Training training = trainingAdapter.createTraining(UserId.of(document.getId()));
        return ResponseEntity.ok(TrainingDocument.from(training));
    }
}
