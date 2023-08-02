package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.PlanExerciseSelector.SelectedExercise;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;

import java.util.List;

public interface ConfigurationService {

    ConfigurationDay fillConfigurationWithExercises(ConfigurationDayId configurationDayId, List<SelectedExercise> selectedExercises);
}
