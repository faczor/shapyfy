package com.sd.shapyfy.boundary.api.dashboard;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract;
import com.sd.shapyfy.boundary.api.dashboard.converter.DayStateToRestConverter;
import com.sd.shapyfy.boundary.api.plans.contract.TrainingSessionDayDocument;
import com.sd.shapyfy.boundary.api.dashboard.converter.DomainPlanToApiConverter;
import com.sd.shapyfy.domain.configuration.TrainingLookup;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.domain.plan.model.StateForDate;
import com.sd.shapyfy.domain.plan.TrainingDayResolver;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/dashboard")
public class DashboardController {

    private final TrainingDayResolver trainingDayResolver;

    private final DayStateToRestConverter dayStateToRestConverter;

    private final TrainingLookup trainingLookup;

    private final DomainPlanToApiConverter domainPlanToApiConverter;

    @GetMapping
    public ResponseEntity<UserDashboardContract> displayPlanForEachDay(
            @RequestParam(name = "from-date") LocalDate from,
            @RequestParam(name = "to-date") LocalDate to) {
        UserId userId = currentUserId();
        log.info("Attempt to display calendar by {} with from {} to {}", userId, from, to);
        List<StateForDate> stateForDates = trainingDayResolver.resolveForDates(from, to, userId);
        List<Training> trainings = trainingLookup.trainingsFor(userId);
        return ResponseEntity.ok(dayStateToRestConverter.convertToDashboard(trainings, stateForDates));
    }

    //TODO internal test
    //TODO check if training owned by user :)
    @GetMapping("/{plan_id}/day/{day_id}")
    public ResponseEntity<TrainingSessionDayDocument> trainingPlanDay(
            @PathVariable("plan_id") String pathPlanId,
            @PathVariable("day_id") String pathDayId) {

        PlanId planId = PlanId.of(pathPlanId);
        SessionPartId sessionPartId = SessionPartId.of(pathDayId);

        Training training = trainingLookup.trainingBy(planId);

        return ResponseEntity.ok(domainPlanToApiConverter.convert(training, sessionPartId));
    }
}
