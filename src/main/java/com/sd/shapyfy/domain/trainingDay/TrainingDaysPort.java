package com.sd.shapyfy.domain.trainingDay;

import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.training.Training;

import java.util.List;

public interface TrainingDaysPort {

    Training.TrainingDay selectExercises(TrainingDayId trainingDayId, List<TrainingManagementAdapter.SelectedExercise> exerciseIdList);
}
