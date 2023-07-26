package com.sd.shapyfy.domain.plan;

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

    public ConfigurationDay firstTrainingDay() {
        return configurationDays.stream().filter(ConfigurationDay::isTrainingDay).findFirst().orElseThrow(() -> new IllegalStateException("Training has no training days"));
    }

    public ConfigurationDay lastTrainingDay() {
        for (int i = configurationDays.size() - 1; i >= 0; i--) {
            ConfigurationDay trainingDay = configurationDays.get(i);
            if (trainingDay.isTrainingDay()) {
                return trainingDay;
            }
        }

        //TODO proper exception
        throw new IllegalStateException("Training has no training days");
    }


}
