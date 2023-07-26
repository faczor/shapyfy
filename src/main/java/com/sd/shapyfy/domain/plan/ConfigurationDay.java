package com.sd.shapyfy.domain.plan;


import com.sd.shapyfy.domain.model.ConfigurationDayType;
import com.sd.shapyfy.domain.model.Exercise;

import java.util.List;

public record ConfigurationDay(
        ConfigurationDayId id,
        ConfigurationDayType type,
        String name,
        List<Exercise> exercises) {

    public boolean isTrainingDay() {
        return type == ConfigurationDayType.TRAINING;
    }

}
