package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.plans.contract.*;
import com.sd.shapyfy.boundary.api.plans.converter.ApiPlanToDomainConverter;
import com.sd.shapyfy.domain.configuration.TrainingLookup;
import com.sd.shapyfy.domain.configuration.TrainingPlanActivator;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.user.model.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/plans")
@RequiredArgsConstructor
public class TraineePlansController {

    private final TrainingPlanCreator trainingPlanCreator;

    private final TrainingPlanActivator trainingPlanActivator;

    private final TrainingLookup trainingLookup;

    private final ApiPlanToDomainConverter apiPlanToDomainConverter;

    @GetMapping
    public ResponseEntity<PlanDocuments> allUserTrainings() {
        UserId userId = currentUserId();
        log.info("Attempt to fetch all trainings {}", userId);

        List<Training> trainings = trainingLookup.trainingsFor(userId);

        return ResponseEntity.ok(PlanDocuments.from(trainings.stream().map(Training::configuration).map(PlanDocument::from).toList()));
    }

    @GetMapping("/{plan_id}")
    public ResponseEntity<PlanDocument> userTrainingById(@PathVariable("plan_id") String pathVariablePlanId) {
        log.info("Attempt to fetch plan {}", pathVariablePlanId);
        PlanId planId = PlanId.of(pathVariablePlanId);

        TrainingConfiguration trainingConfiguration = trainingLookup.configurationFor(planId);

        return ResponseEntity.ok(PlanDocument.from(trainingConfiguration));
    }

    @PostMapping
    public ResponseEntity<PlanDocument> createNewPlan(@Valid @RequestBody CreatePlanDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        TrainingConfiguration plan = trainingPlanCreator.create(apiPlanToDomainConverter.convertForCreation(document), userId);
        return ResponseEntity.ok(PlanDocument.from(plan));
    }

    @PatchMapping("/{plan_id}/activations")
    public ResponseEntity<Void> finishConfiguration(
            @PathVariable("plan_id") String pathVariablePlanId,
            @Valid @RequestBody StartPlanDocument document) {
        PlanId planId = PlanId.of(pathVariablePlanId);
        log.info("Attempt to activate training {} with params {}", planId, document);

        trainingPlanActivator.activate(planId, SessionPartId.of(document.sessionDayId()), document.startDate());

        return ResponseEntity.ok().build();
    }
}
