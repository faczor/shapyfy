package com.shapyfy.core.boundary.api.trainingDay.adapter;

import com.shapyfy.core.SystemTime;
import com.shapyfy.core.boundary.api.trainingDay.model.CompletePlanDayRequest;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract.ExerciseConfigContract;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract.PreviousWorkouts;
import com.shapyfy.core.boundary.api.trainingDay.model.PlanDayContract.WorkoutExerciseContract.PreviousWorkouts.WorkoutSetContract;
import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog;
import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog.WorkoutSetLog;
import com.shapyfy.core.domain.PlanDays;
import com.shapyfy.core.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanDayApiAdapter {

    private final PlanDays planDays;

    private final ActivityLogs activityLogs;

    private final SystemTime systemTime;

    Collector<WorkoutSet, ?, Map<Exercise, Map<LocalDate, List<WorkoutSet>>>> groupWorkoutSetToExerciseDateSets = groupingBy(
            WorkoutSet::getExercise,
            groupingBy(set -> set.getActivityLog().getDate())
    );

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

    private PlanDayContract mapToContract(PlanDay planDay, List<ActivityLog> logs) {
        if (isNull(logs)) {
            throw new IllegalArgumentException("Logs cannot be null");
        }

        List<ExerciseLogsByDate> exerciseLogsByDate = groupExerciseLogsByDate(logs);

        List<WorkoutExerciseContract> workoutExercises = planDay.getWorkoutExerciseConfigs().stream()
                .map(config -> mapToWorkoutExerciseContract(config, exerciseLogsByDate))
                .toList();

        return new PlanDayContract(
                planDay.getId().getId(),
                planDay.getName(),
                planDay.getType(),
                workoutExercises
        );
    }

    private List<ExerciseLogsByDate> groupExerciseLogsByDate(List<ActivityLog> logs) {

        return logs.stream()
                .flatMap(log -> log.getSets().stream())
                .collect(groupWorkoutSetToExerciseDateSets)
                .entrySet().stream()
                .map(entry -> new ExerciseLogsByDate(entry.getKey(), entry.getValue()))
                .toList();
    }

    private WorkoutExerciseContract mapToWorkoutExerciseContract(
            WorkoutExerciseConfig exerciseConfig,
            List<ExerciseLogsByDate> exerciseLogsByDate) {
        Exercise exercise = exerciseConfig.getExercise();
        List<PreviousWorkouts> previousWorkouts = getPreviousWorkouts(exerciseLogsByDate, exercise);

        return new WorkoutExerciseContract(
                exercise.getId().getId(),
                exercise.getName(),
                ExerciseConfigContract.from(exerciseConfig),
                previousWorkouts
        );
    }

    private List<PreviousWorkouts> getPreviousWorkouts(
            List<ExerciseLogsByDate> exerciseLogsByDate,
            Exercise exercise) {
        Map<LocalDate, List<WorkoutSet>> logsByDate = exerciseLogsByDate.stream()
                .filter(logs -> Objects.equals(logs.exercise(), exercise))
                .findFirst()
                .map(ExerciseLogsByDate::logsByDate)
                .orElseGet(Collections::emptyMap);

        return logsByDate.entrySet().stream()
                .map(entry -> PreviousWorkouts.from(entry.getKey(), entry.getValue()))
                .toList();
    }

    public record ExerciseLogsByDate(
            Exercise exercise,
            Map<LocalDate, List<WorkoutSet>> logsByDate) {
    }
}
