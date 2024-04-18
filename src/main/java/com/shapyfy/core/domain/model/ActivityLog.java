package com.shapyfy.core.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ActivityLog(
        ActivityLogId id,
        LocalDate date,
        TrackType type,
        PlanDay planDay,
        List<WorkoutSet> sets
) {

    public static ActivityLog workout(LocalDate date, PlanDay planDay, List<WorkoutSet> sets) {
        return new ActivityLog(ActivityLogId.newVal(), date, TrackType.WORKOUT, planDay, sets);
    }

    public record ActivityLogId(String value) {
        public static ActivityLogId newVal() {
            return new ActivityLogId(UUID.randomUUID().toString());
        }
    }

    public enum TrackType {
        WORKOUT,
        SKIP,
        RESIGN
    }
}
