package com.sd.shapyfy.domain.model;

public enum SessionState {
    DRAFT, FINISHED, ACTIVE, RUNNING, FOLLOW_UP, HISTORICAL;

    public boolean isDraft() {
        return this == DRAFT;
    }

    public boolean isActive() {
        return this == ACTIVE || this == RUNNING || this == FINISHED;
    }

    public boolean isFuture() {
        return this == FOLLOW_UP || isDraft() || isActive();
    }

    public boolean isRunning() {
        return this == RUNNING;
    }
}
