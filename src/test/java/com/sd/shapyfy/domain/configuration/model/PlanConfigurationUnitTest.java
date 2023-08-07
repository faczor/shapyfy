package com.sd.shapyfy.domain.configuration.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.TRAINING;
import static org.assertj.core.api.Assertions.assertThat;

public class PlanConfigurationUnitTest {

    @Test
    @DisplayName("[PlanConfiguration.restDaysAfterTraining()]::Should return 0 when last day is training day")
    public void shouldReturn0_whenLastDayIsTrainingDay() {
        PlanConfiguration configuration = configuration(List.of(c_day(REST), c_day(REST), c_day(TRAINING)));

        assertThat(configuration.restDaysAfterTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[PlanConfiguration.restDaysAfterTraining()]::Should return 2 when last day is training day")
    public void shouldReturn2_when2LastDaysAreRest() {
        PlanConfiguration configuration = configuration(List.of(c_day(TRAINING), c_day(REST), c_day(REST)));

        assertThat(configuration.restDaysAfterTraining()).isEqualTo(2);
    }

    @Test
    @DisplayName("[PlanConfiguration.restDaysBeforeTraining()]::Should return 0 when first day is training day")
    public void shouldReturn0_whenFirstDayIsTrainingDay() {
        PlanConfiguration configuration = configuration(List.of(c_day(TRAINING), c_day(REST)));

        assertThat(configuration.restDaysBeforeTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[PlanConfiguration.restDaysBeforeTraining()]::Should return 2 when training day is a third training day")
    public void shouldReturn2_whenTrainingDayIsThird() {
        PlanConfiguration configuration = configuration(List.of(c_day(REST), c_day(REST), c_day(TRAINING)));

        assertThat(configuration.restDaysBeforeTraining()).isEqualTo(2);
    }

    @Test
    @DisplayName("[PlanConfiguration.daysPlanAmount()]::Should return 2 on 2 configuration days")
    public void shouldReturn2_when2configurationDaysAreSet() {
        PlanConfiguration configuration = configuration(List.of(c_day(REST), c_day(TRAINING)));

        assertThat(configuration.daysPlanAmount()).isEqualTo(2);
    }

    private PlanConfiguration configuration(List<ConfigurationDay> c_days) {
        return new PlanConfiguration(
                null,
                c_days
        );
    }

    private ConfigurationDay c_day(ConfigurationDayType type) {
        UUID uuid = UUID.randomUUID();
        return new ConfigurationDay(
                ConfigurationDayId.of(uuid),
                type,
                type + "::" + uuid,
                List.of()
        );
    }
}
