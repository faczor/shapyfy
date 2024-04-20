package com.shapyfy.core.boundary.api.trainingDay.adapter;

import com.shapyfy.core.SystemTime;
import com.shapyfy.core.boundary.api.trainingDay.model.CompletePlanDayRequest;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract.PreviousWorkouts;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract.PreviousWorkouts.WorkoutSetContract;
import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog;
import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog.WorkoutSetLog;
import com.shapyfy.core.domain.PlanDays;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.WorkoutSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanDayApiAdapter {

    private final PlanDays planDays;

    private final ActivityLogs activityLogs;

    private final SystemTime systemTime;

    public PlanDayContract getPlanDay(PlanDay.PlanDayId planDayId) {
        PlanDay planDay = planDays.fetchById(planDayId);
        List<ActivityLog> logsForPlan = activityLogs.logsForPlanDay(planDayId);

        return mapToContract(planDay, logsForPlan);
    }

    public void completePlanDay(CompletePlanDayRequest request) {
        LocalDate today = systemTime.today();

        activityLogs.workout(
                new ActivityLogs.CreateWorkoutLogRequest(
                        today,
                        PlanDay.PlanDayId.of(request.planDayId()),
                        request.completedExercises().stream().map(exercise -> new WorkoutExerciseLog(
                                Exercise.ExerciseId.of(exercise.exerciseId()),
                                exercise.completedSets().stream().map(set -> new WorkoutSetLog(
                                        set.reps(),
                                        set.weight()
                                )).toList()
                        )).toList()
                )
        );
    }

    //TODO Code build by AI: Refactor this method
    private PlanDayContract mapToContract(PlanDay planDay, List<ActivityLog> logs) {

        List<ExerciseWithPreviousOccurrences> exerciseWithPreviousOccurrences = convertToHelpingAggregators(logs);

        List<PlanDayContract.WorkoutExerciseContract> workoutExercises = planDay.getWorkoutExerciseConfigs().stream().map(exerciseConfig -> {

            Optional<ExerciseWithPreviousOccurrences> possibleOccurrence = exerciseWithPreviousOccurrences.stream().filter(occurrence -> occurrence.exercise().equals(exerciseConfig.getExercise())).findFirst();

            List<PreviousWorkouts> previousWorkouts = possibleOccurrence.map(occurrence -> occurrence.dateSets().stream().map(support -> {
                List<WorkoutSetContract> sets = support.sets().stream().map(set -> new WorkoutSetContract(
                        set.getReps(),
                        set.getWeight()
                )).toList();
                return new PreviousWorkouts(support.date(), sets);

            }).toList()).orElse(Collections.emptyList());

            return new PlanDayContract.WorkoutExerciseContract(
                    exerciseConfig.getExercise().getId().getId(),
                    exerciseConfig.getExercise().getName(),
                    new PlanDayContract.WorkoutExerciseContract.ExerciseConfigContract(
                            exerciseConfig.getReps(),
                            exerciseConfig.getWeight(),
                            exerciseConfig.getSets(),
                            exerciseConfig.getRestTime()
                    ),
                    previousWorkouts
            );
        }).toList();

        return new PlanDayContract(
                planDay.getId().getId(),
                planDay.getName(),
                planDay.getType(),
                workoutExercises
        );
    }

    private List<ExerciseWithPreviousOccurrences> convertToHelpingAggregators(List<ActivityLog> activityLogs) {
        List<ExerciseWithPreviousOccurrences> helpingAggregators = new ArrayList<>();

        for (ActivityLog activityLog : activityLogs) {
            for (WorkoutSet workoutSet : activityLog.getSets()) {
                Exercise exercise = workoutSet.getExercise();

                ExerciseWithPreviousOccurrences helpingAggregator = findHelpingAggregatorByExercise(helpingAggregators, exercise);

                if (helpingAggregator == null) {
                    helpingAggregator = new ExerciseWithPreviousOccurrences(
                            exercise,
                            new ArrayList<>()
                    );
                    helpingAggregators.add(helpingAggregator);
                }

                ExerciseWithPreviousOccurrences.DateSets setsByDate = findSetsByDate(helpingAggregator.dateSets(), activityLog.getDate());

                if (setsByDate == null) {
                    ArrayList<WorkoutSet> list = new ArrayList<>();
                    list.add(workoutSet);
                    ExerciseWithPreviousOccurrences.DateSets support = new ExerciseWithPreviousOccurrences.DateSets(
                            activityLog.getDate(),
                            list
                    );
                    helpingAggregator.dateSets().add(support);
                } else {
                    setsByDate.sets().add(workoutSet);
                }
            }
        }

        return helpingAggregators;
    }

    private ExerciseWithPreviousOccurrences findHelpingAggregatorByExercise(List<ExerciseWithPreviousOccurrences> exerciseWithPreviousOccurrences, Exercise exercise) {
        for (ExerciseWithPreviousOccurrences occurrence : exerciseWithPreviousOccurrences) {
            if (occurrence.exercise().equals(exercise)) {
                return occurrence;
            }
        }
        return null;
    }

    private ExerciseWithPreviousOccurrences.DateSets findSetsByDate(List<ExerciseWithPreviousOccurrences.DateSets> setsByDates, LocalDate date) {
        for (ExerciseWithPreviousOccurrences.DateSets setsByDate : setsByDates) {
            if (setsByDate.date().equals(date)) {
                return setsByDate;
            }
        }
        return null;
    }

    private record ExerciseWithPreviousOccurrences(Exercise exercise, List<DateSets> dateSets) {
        record DateSets(
                LocalDate date,
                List<WorkoutSet> sets
        ) {
        }
    }
}
