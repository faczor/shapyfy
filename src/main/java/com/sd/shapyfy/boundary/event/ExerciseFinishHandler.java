package com.sd.shapyfy.boundary.event;

import com.sd.shapyfy.domain.configuration.PlanConfigurationFetcher;
import com.sd.shapyfy.domain.configuration.SessionCreator;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.session.SessionLookup;
import com.sd.shapyfy.domain.session.SessionUpdater;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.domain.user_exercise.UserExerciseUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState.ACTIVE;
import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState.FINISHED;
import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState.FOLLOW_UP;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExerciseFinishHandler {

    private final UserExerciseUpdater userExerciseUpdater;

    private final SessionLookup sessionLookup;

    private final SessionUpdater sessionUpdater;

    private final PlanConfigurationFetcher trainingPlanFetcher;

    private final SessionCreator sessionCreator;

    //TODO is this proper way?
    // Last day of training can be rest day - better verification needed
    // SessionPart should be marked as finished
    // ---
    // What if not all of exercises will be finished by user?
    // Scheduler that will mark as finished/abandoned exercises
    @EventListener(OnExerciseFinishEvent.class)
    public void handleExerciseFinish(OnExerciseFinishEvent event) {
        log.info("Attempt to handle {}", event);
        TrainingExercise trainingExercise = event.getTrainingExercise();
        UserId userId = event.getUserId();
        SessionId sessionId = event.getSessionId();

        userExerciseUpdater.updateUpcomingSessionExercise(trainingExercise, userId);

        Session session = sessionLookup.lookup(sessionId);

        if (session.areExercisesFinished()) {
            handleSessionFinish(sessionId);
        }
    }

    private void handleSessionFinish(SessionId sessionId) {
        log.info("All exercises finished on session {}", sessionId);
        sessionUpdater.updateStatus(sessionId, FINISHED);
        Training training = trainingPlanFetcher.fetchForSession(sessionId);

        Session followUpSession = training.getFollowUpSession();
        sessionUpdater.updateStatus(followUpSession.sessionId(), ACTIVE);

        sessionCreator.createSession(
                training.configuration(),
                FOLLOW_UP,
                training.configuration().configurationDays().get(0).id(),
                followUpSession.lastDate().plusDays(1)
        );
    }
}
