package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.domain.port.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFetcher {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlan fetchFor(UserId userId) {
        log.info("Fetching training plan for {}", userId);
        return trainingPlanRepository.findByUserId(userId);
    }
}
