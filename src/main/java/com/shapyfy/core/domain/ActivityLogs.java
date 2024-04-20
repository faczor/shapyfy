package com.shapyfy.core.domain;

import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog;
import com.shapyfy.core.domain.model.*;
import com.shapyfy.core.domain.port.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLogs {

    private final ActivityLogRepository activityLogRepository;

    private final PlanDays planDays;

    private final Exercises exercises;

    public void skip(LocalDate date) {
        log.info("Attempt to skip track {}", date);
    }

    public void move(LocalDate from, LocalDate to) {
        log.info("Attempt to move track from {} to {}", from, to);
    }

    public void workout(CreateWorkoutLogRequest request) {
        log.info("Attempt to workout track {}", request);
        PlanDay planDay = planDays.fetchById(request.planDayId());

        ActivityLog log = ActivityLog.workout(request.date(), planDay);
        request.exercises().stream().map(e -> mapRequestToWorkoutSet(e, log))
                .flatMap(List::stream)
                .forEach(log::addSet);

        ActivityLog savedLog = activityLogRepository.save(log);
        ActivityLogs.log.info("Workout track saved {}", savedLog);
    }

    public List<ActivityLog> fetchFor(LocalDate from, LocalDate to, List<PlanDay.PlanDayId> planDayIds) {
        log.info("Fetching activity logs for {} from {} to {}", planDayIds, from, to);

        return activityLogRepository.findAll(from, to, planDayIds);
    }

    public List<ActivityLog> logsForPlanDay(PlanDay.PlanDayId planDayId) {
        log.info("Fetching activity logs for plan day {}", planDayId);
        return activityLogRepository.findAllByPlanDayId(planDayId);
    }

    private List<WorkoutSet> mapRequestToWorkoutSet(WorkoutExerciseLog finishedExercise, ActivityLog log) {
        Exercise exercise = exercises.fetchById(finishedExercise.exerciseId());
        return IntStream.range(0, finishedExercise.sets().size())
                .mapToObj(index -> WorkoutSet.from(
                        finishedExercise.sets().get(index).reps(),
                        finishedExercise.sets().get(index).weight(),
                        exercise,
                        index, log))
                .toList();
    }

    public record CreateWorkoutLogRequest(
            LocalDate date,
            PlanDay.PlanDayId planDayId,
            List<WorkoutExerciseLog> exercises) {
        public record WorkoutExerciseLog(
                Exercise.ExerciseId exerciseId,
                List<WorkoutSetLog> sets) {
            public record WorkoutSetLog(
                    int reps,
                    double weight) {
            }
        }
    }
}
