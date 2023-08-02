package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

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
