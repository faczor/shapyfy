package com.shapyfy.core.boundary.api.trainingDay;

import com.shapyfy.core.boundary.api.trainingDay.adapter.PlanDayApiAdapter;
import com.shapyfy.core.boundary.api.trainingDay.model.CompletePlanDayRequest;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract;
import com.shapyfy.core.domain.model.PlanDay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanDayController {

    private final PlanDayApiAdapter planDayApiAdapter;

    @GetMapping("/plan_days/{planDayId}")
    public ResponseEntity<PlanDayContract> getPlanDay(@PathVariable(name = "planDayId") UUID planDayId) {
        log.info("Fetching plan day with id: {}", planDayId);
        PlanDayContract planDay = planDayApiAdapter.getPlanDay(PlanDay.PlanDayId.of(planDayId));
        log.info("Fetched plan day with id: {}", planDayId);
        return ResponseEntity.ok(planDay);
    }

    @PostMapping("/plan_day_completions")
    public ResponseEntity<Void> completePlanDay(@RequestBody CompletePlanDayRequest request) {
        log.info("Completing plan day with request: {}", request);
        planDayApiAdapter.completePlanDay(request);
        log.info("Completed plan day with request");
        return ResponseEntity.ok().build();
    }
}
