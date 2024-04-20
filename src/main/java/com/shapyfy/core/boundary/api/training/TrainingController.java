package com.shapyfy.core.boundary.api.training;

import com.shapyfy.core.boundary.api.training.adapter.TrainingApiAdapter;
import com.shapyfy.core.boundary.api.training.model.ConfigurePlanRequest;
import com.shapyfy.core.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shapyfy.core.boundary.api.TokenUtils.currentUserId;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainings")
public class TrainingController {

    private final TrainingApiAdapter trainingApiAdapter;

    @PostMapping
    public ResponseEntity<Void> createTraining(@RequestBody ConfigurePlanRequest request) {
        UserId userId = currentUserId();
        log.info("Creating training for user: {} by {}", userId, request);
        trainingApiAdapter.createTraining(request, userId);
        log.info("Training created for user: {} ", userId);
        return ResponseEntity.ok().build();
    }
}
