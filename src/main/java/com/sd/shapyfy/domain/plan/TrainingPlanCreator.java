package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayType;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanCreator {

    private final TrainingPlanService trainingPlanService;

    public PlanConfiguration create(PlanCreationInitialConfigurationParams configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);

        PlanConfiguration planConfiguration = trainingPlanService.create(configurationParams, userId);
        log.info("Training plan created for user {} with configuration {}", userId, planConfiguration);
        return planConfiguration;
    }


    public record PlanCreationInitialConfigurationParams(
            String name,
            List<SessionDayConfiguration> sessionDayConfigurations) {

        public record SessionDayConfiguration(
                String name,
                ConfigurationDayType dayType) {
        }
    }
}
