package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.ConfigurationAttribute;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;

import java.util.List;

public record PlanConfigurationDocument(
        @JsonProperty(value = "id")
        String id,
        //
        @JsonProperty(value = "training_days", required = true)
        List<TrainingDayConfigurationDocument> trainingDays,
        //
        @JsonProperty(value = "set_attributes")
        List<AttributeConfigurationDocument> setAttributes,
        //
        @JsonProperty(value = "exercise_attributes")
        List<AttributeConfigurationDocument> exerciseAttributes) {

    public static PlanConfigurationDocument from(TrainingConfiguration trainingConfiguration) {
        return new PlanConfigurationDocument(
                trainingConfiguration.configurationId().getValue().toString(),
                trainingConfiguration.configurationDays().stream().map(TrainingDayConfigurationDocument::from).toList(),
                trainingConfiguration.configurationAttributes().stream().filter(ConfigurationAttribute::isForSet).map(AttributeConfigurationDocument::from).toList(),
                trainingConfiguration.configurationAttributes().stream().filter(ConfigurationAttribute::isForExercise).map(AttributeConfigurationDocument::from).toList()
        );
    }
}
