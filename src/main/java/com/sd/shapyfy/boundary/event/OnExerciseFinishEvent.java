package com.sd.shapyfy.boundary.event;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnExerciseFinishEvent extends ApplicationEvent {

    private final TrainingExercise trainingExercise;
    private final UserId userId;
    private final SessionId sessionId;
    private final SessionPartId sessionPartId;

    public OnExerciseFinishEvent(Object source, TrainingExercise trainingExercise, UserId userId, SessionId sessionId, SessionPartId sessionPartId) {
        super(source);
        this.trainingExercise = trainingExercise;
        this.userId = userId;
        this.sessionId = sessionId;
        this.sessionPartId = sessionPartId;
    }
}
