package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.exception.TrainingNotConfiguredProperly;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanActivator {

    private final TrainingLookup trainingLookup;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void activate(PlanId planId, ConfigurationDayId configurationDayId, LocalDate startDate) {
        log.info("Attempt to activate {} by {} on {}", planId, configurationDayId, startDate);
        TrainingConfiguration trainingConfiguration = trainingLookup.configurationFor(planId);
        validateIfTrainingIsFilledProperly(trainingConfiguration.configurationDays());
        //TODO set status of plan :)

        //TODO move event publishing to interface
        applicationEventPublisher.publishEvent(new OnTrainingActivationEvent(this, trainingConfiguration, ConfigurationDayId.of(configurationDayId.getValue()), startDate));
    }

    private void validateIfTrainingIsFilledProperly(List<ConfigurationDay> configurationDays) {
        List<ConfigurationDay> trainingDaysWithoutExercises = configurationDays.stream()
                .filter(ConfigurationDay::isTrainingDay)
                .filter(trainingDay -> isEmpty(trainingDay.exercises()))
                .toList();

        if (isNotEmpty(trainingDaysWithoutExercises)) {
            log.info("Training is not filled properly. Training days without exercises: {}", trainingDaysWithoutExercises);
            throw new TrainingNotConfiguredProperly(trainingDaysWithoutExercises.stream().map(ConfigurationDay::id).toList());
        }
    }

}
