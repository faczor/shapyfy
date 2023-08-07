package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.configuration.exception.ConfigurationWithoutTrainingDays;
import com.sd.shapyfy.domain.plan.model.Plan;

import java.util.List;

public record PlanConfiguration(
        Plan plan,
        List<ConfigurationDay> configurationDays) {

    public int restDaysAfterTraining() {
        ConfigurationDay lastTrainingDay = lastTrainingDay();
        int indexOfLastTrainingDay = configurationDays.indexOf(lastTrainingDay);

        return configurationDays.size() - 1 - indexOfLastTrainingDay;
    }

    public int restDaysBeforeTraining() {
        ConfigurationDay firstTrainingDay = firstTrainingDay();
        return configurationDays.indexOf(firstTrainingDay);
    }

    public int daysPlanAmount() {
        return configurationDays().size();
    }

    private ConfigurationDay firstTrainingDay() {
        return configurationDays.stream().filter(ConfigurationDay::isTrainingDay).findFirst()
                .orElseThrow(() -> new ConfigurationWithoutTrainingDays("First training day not found for " + plan.id()));
    }

    private ConfigurationDay lastTrainingDay() {
        for (int i = configurationDays.size() - 1; i >= 0; i--) {
            ConfigurationDay trainingDay = configurationDays.get(i);
            if (trainingDay.isTrainingDay()) {
                return trainingDay;
            }
        }
        throw new ConfigurationWithoutTrainingDays("Last training day not found for " + plan.id());
    }
}
