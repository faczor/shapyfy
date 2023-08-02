package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.plans.contract.*;
import com.sd.shapyfy.boundary.api.plans.converter.ApiPlanToDomainConverter;
import com.sd.shapyfy.domain.configuration.PlanExerciseSelector;
import com.sd.shapyfy.domain.configuration.TrainingPlanActivator;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
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

import java.util.List;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/plans")
@RequiredArgsConstructor
public class TraineePlansController {

    private final TrainingPlanCreator trainingPlanCreator;

    private final TrainingPlanActivator trainingPlanActivator;

    private final PlanExerciseSelector planExerciseSelector;

    private final ApiPlanToDomainConverter apiPlanToDomainConverter;

    @PostMapping
    public ResponseEntity<PlanDocument> createNewPlan(@Valid @RequestBody CreatePlanDocument document) {
        log.info("Attempt to create training {}", document);
        UserId userId = currentUserId();

        PlanConfiguration plan = trainingPlanCreator.create(apiPlanToDomainConverter.convertForCreation(document), userId);
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

    @PatchMapping("/{plan_id}/configuration_days/{configuration_id}/selections")
    public ResponseEntity<ConfigurationDayDocument> selectExercisesToConfiguration(
            @PathVariable(name = "plan_id") String planId,
            @PathVariable(name = "configuration_id") String configurationDayId,
            @RequestBody @Valid ConfigurationDaySelectedExercisesDocument configurationDaySelectedExercisesDocument) {
        UserId userId = currentUserId();
        log.info("Attempt to fill configuration day {} for training plan {} with {} by {}", configurationDayId, planId, configurationDaySelectedExercisesDocument, userId);

        List<PlanExerciseSelector.SelectedExercise> selectedExercises = apiPlanToDomainConverter.convertForExercisesSelection(configurationDaySelectedExercisesDocument);
        ConfigurationDay configurationDay = planExerciseSelector.select(ConfigurationDayId.of(configurationDayId), selectedExercises, userId);

        return ResponseEntity.ok(ConfigurationDayDocument.from(configurationDay));
    }
}
