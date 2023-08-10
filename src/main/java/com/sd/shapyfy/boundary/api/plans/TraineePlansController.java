package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.PlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.StartPlanDocument;
import com.sd.shapyfy.boundary.api.plans.converter.ApiPlanToDomainConverter;
import com.sd.shapyfy.domain.configuration.TrainingPlanActivator;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.domain.user.model.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/plans")
@RequiredArgsConstructor
public class TraineePlansController {

    private final TrainingPlanCreator trainingPlanCreator;

    private final TrainingPlanActivator trainingPlanActivator;

    private final ApiPlanToDomainConverter apiPlanToDomainConverter;

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
