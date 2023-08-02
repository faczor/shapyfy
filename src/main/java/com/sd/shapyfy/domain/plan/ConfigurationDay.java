package com.sd.shapyfy.domain.plan;


import com.sd.shapyfy.domain.model.ConfigurationDayType;

import java.util.List;

public record ConfigurationDay(
        ConfigurationDayId id,
        ConfigurationDayType type,
        String name,
        List<TrainingExercise> exercises) {

    public boolean isTrainingDay() {
        return type == ConfigurationDayType.TRAINING;
    }

}
