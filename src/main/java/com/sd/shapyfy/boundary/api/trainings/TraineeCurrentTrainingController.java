package com.sd.shapyfy.boundary.api.trainings;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.TokenUtils;
import com.sd.shapyfy.boundary.api.trainings.contract.CurrentTrainingResponseDocument;
import com.sd.shapyfy.boundary.api.trainings.contract.TrainingDocument;
import com.sd.shapyfy.domain.TrainingLookup;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@ApiV1("/v1/current-trainings")
@RequiredArgsConstructor
public class TraineeCurrentTrainingController {

    private final TrainingLookup trainingLookup;

    @GetMapping
    public ResponseEntity<CurrentTrainingResponseDocument> trainingDetails() {
        UserId userId = TokenUtils.currentUserId();
        TrainingLookup.CurrentTraining training = trainingLookup.currentTrainingFor(userId);

        return ResponseEntity.ok(CurrentTrainingResponseDocument.from(training));
    }

}
