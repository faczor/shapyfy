package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import java.util.EnumSet;

public enum SessionState {
    DRAFT, FINISHED, ACTIVE, SKIPPED, RUNNING, FOLLOW_UP, HISTORICAL;

    public boolean isDraft() {
        return this == DRAFT;
    }

    public boolean isActive() {
        return this == ACTIVE || this == RUNNING || this == FINISHED;
    }

    public boolean haveTrainingDate() {
        return EnumSet.complementOf(EnumSet.of(DRAFT)).contains(this);
    }
}
