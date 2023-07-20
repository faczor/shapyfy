package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.plans.contract.CreatePlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.StartPlanDocument;
import com.sd.shapyfy.boundary.api.plans.contract.PlanDocument;
import com.sd.shapyfy.boundary.api.plans.converter.PlanToDomainConverter;
import com.sd.shapyfy.domain.PlanManagementAdapter;
import com.sd.shapyfy.domain.model.Training;
import com.sd.shapyfy.domain.model.PlanId;
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
@ApiV1("/v1/plans")
@RequiredArgsConstructor
public class TraineePlansController {

    public final PlanManagementAdapter planManagement;

    public final PlanToDomainConverter planToDomainConverter;

    @PostMapping
    public ResponseEntity<PlanDocument> createNewPlan(@Valid @RequestBody CreatePlanDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        Training training = planManagement.create(planToDomainConverter.convertForCreation(document), userId);
        return ResponseEntity.ok(PlanDocument.from(training));
    }

    @PutMapping("/{plan_id}/activations")
    public ResponseEntity<Void> finishConfiguration(
            @PathVariable("plan_id") String pathVariablePlanId,
            @Valid @RequestBody StartPlanDocument document) {
        PlanId planId = PlanId.of(pathVariablePlanId);
        log.info("Attempt to activate training {} with params {}", planId, document);

        planManagement.activate(planId, TrainingDayId.of(document.trainingDayStartId()), document.startDate());

        return ResponseEntity.ok().build();
    }
}
