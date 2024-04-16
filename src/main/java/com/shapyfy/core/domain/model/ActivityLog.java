package com.shapyfy.core.domain.model;

import java.time.LocalDate;
import java.util.List;

public record ActivityLog(
        RecordId id,
        LocalDate date,
        TrackType type,
        List<WorkoutExerciseConfig> exercises
) {
    private record RecordId(String value) {
    }

    enum TrackType {
        WORKOUT,
        SKIP,
        RESIGN
    }
}
