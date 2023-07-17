package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.TrainingDayId;

import java.util.List;

public interface TrainingDaysPort {

    TrainingDay selectExercises(TrainingDayId trainingDayId, List<TrainingManagementAdapter.SelectedExercise> exerciseIdList);
}
