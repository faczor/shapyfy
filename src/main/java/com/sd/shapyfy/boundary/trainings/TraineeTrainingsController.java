package com.sd.shapyfy.boundary.trainings;

import com.sd.shapyfy.boundary.trainings.contract.CreateTrainingDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TraineeTrainingsController {

    @GetMapping("/active")
    public ResponseEntity<Object> listTrainings() {
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> createTraining(@RequestBody CreateTrainingDocument document) {
        return null;
    }
}
