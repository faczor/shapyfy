package com.shapyfy.core.domain.model;

import java.time.LocalDate;
import java.util.List;

public record ActivityLog(
        RecordId id,
        LocalDate date,
        TrackType type,
        PlanDay planDay,
        List<WorkoutExerciseConfig> exercises
) {
    public record RecordId(String value) {
    }

    enum TrackType {
        WORKOUT,
        SKIP,
        RESIGN
    }
}
