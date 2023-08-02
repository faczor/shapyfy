package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.StartPlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.PlanDocument;
import com.sd.shapyfy.boundary.api.plans.converter.PlanToDomainConverter;
import com.sd.shapyfy.domain.configuration.TrainingPlanActivator;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/plans")
@RequiredArgsConstructor
public class TraineePlansController {

    public final TrainingPlanCreator trainingPlanCreator;

    public final TrainingPlanActivator trainingPlanActivator;

    public final PlanToDomainConverter planToDomainConverter;

    @PostMapping
    public ResponseEntity<PlanDocument> createNewPlan(@Valid @RequestBody CreatePlanDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        PlanConfiguration plan = trainingPlanCreator.create(planToDomainConverter.convertForCreation(document), userId);
        return ResponseEntity.ok(PlanDocument.from(plan));
    }

    @PatchMapping("/{plan_id}/activations")
    public ResponseEntity<Void> finishConfiguration(
            @PathVariable("plan_id") String pathVariablePlanId,
            @Valid @RequestBody StartPlanDocument document) {
        PlanId planId = PlanId.of(pathVariablePlanId);
        log.info("Attempt to activate training {} with params {}", planId, document);

        trainingPlanActivator.activate(planId, ConfigurationDayId.of(document.trainingDayStartId()), document.startDate());

        return ResponseEntity.ok().build();
    }
}
