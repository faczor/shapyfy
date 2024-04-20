package com.shapyfy.core.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "activity_logs")
@Table(name = "activity_logs")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Value
public class ActivityLog {

    @EmbeddedId
    ActivityLogId id;

    LocalDate date;

    @Enumerated(EnumType.STRING)
    TrackType type;

    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    PlanDay planDay;

    @OneToMany(mappedBy = "activityLog", cascade = CascadeType.ALL)
    List<WorkoutSet> sets;

    public static ActivityLog workout(LocalDate date, PlanDay planDay) {
        return new ActivityLog(ActivityLogId.createNew(), date, TrackType.WORKOUT, planDay, new ArrayList<>());
    }

    public void addSet(WorkoutSet set) {
        sets.add(set);
    }

    @Value
    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    public static class ActivityLogId {

        UUID id;

        public static ActivityLogId createNew() {
            return new ActivityLogId(UUID.randomUUID());
        }
    }

    public enum TrackType {
        WORKOUT,
        SKIP,
        RESIGN
    }
}
