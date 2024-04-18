package com.shapyfy.core.domain;

import com.shapyfy.core.domain.ActivityLogs.CreateWorkoutLogRequest.WorkoutExerciseLog;
import com.shapyfy.core.domain.model.*;
import com.shapyfy.core.domain.port.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
        List<WorkoutSet> sets = request.exercises().stream().map(this::mapRequestToWorkoutSet).flatMap(List::stream).toList();

        ActivityLog savedLog = activityLogRepository.save(ActivityLog.workout(request.date(), planDay, sets));
        log.info("Workout track saved {}", savedLog);
    }

    public List<ActivityLog> fetchFor(LocalDate from, LocalDate to, UserId userId) {
        log.info("Fetching activity logs for {} from {} to {}", userId, from, to);

        return activityLogRepository.findAll(from, to, userId);
    }

    public List<ActivityLog> logsForPlanDay(PlanDay.PlanDayId planDayId) {
        return activityLogRepository.findAllByPlanDayId(planDayId);
    }

    private List<WorkoutSet> mapRequestToWorkoutSet(WorkoutExerciseLog finishedExercise) {
        Exercise exercise = exercises.fetchById(finishedExercise.exerciseId());
        return finishedExercise.sets().stream().map(set -> new WorkoutSet(null, set.reps(), set.weight(), exercise, finishedExercise.sets.indexOf(set))).toList();
    }

    public record CreateWorkoutLogRequest(
            LocalDate date,
            PlanDay.PlanDayId planDayId,
            List<WorkoutExerciseLog> exercises) {
        record WorkoutExerciseLog(
                Exercise.ExerciseId exerciseId,
                List<WorkoutSetLog> sets) {
            record WorkoutSetLog(
                    int reps,
                    double weight) {
            }
        }
    }
}
