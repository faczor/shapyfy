package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.PlanExerciseSelector.SelectedExercise;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;

import java.util.List;

public interface ConfigurationService {

    ConfigurationDay fillConfigurationWithExercises(ConfigurationDayId configurationDayId, List<SelectedExercise> selectedExercises);
}
