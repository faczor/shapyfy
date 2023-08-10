package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.SessionService.EditSessionParams.EditSessionPart;
import com.sd.shapyfy.domain.configuration.SessionService.EditSessionPartParams;
import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCreator {

    private final SessionService sessionService;

    @EventListener(OnTrainingActivationEvent.class)
    public void createFollowUpsOnTrainingActivation(OnTrainingActivationEvent event) {
        createFollowUp(event.getTrainingConfiguration(), event.getLastTrainingDate());
    }

    private void createFollowUp(TrainingConfiguration trainingConfiguration, LocalDate lastTrainingDate) {
        int iterator = 0;
        lastTrainingDate = lastTrainingDate.plusDays(1);
        ArrayList<EditSessionPartParams> editSessionPartParams = new ArrayList<>();
        for (ConfigurationDay configurationDay : trainingConfiguration.configurationDays()) {

            EditSessionPartParams createSessionPartParams = new EditSessionPartParams(
                    configurationDay.name(),
                    configurationDay.type(),
                    lastTrainingDate.plusDays(iterator),
                    SessionPartState.ACTIVE,
                    configurationDay.exercises().stream().map(trainingExercise -> new EditSessionPartParams.SessionExerciseExerciseEditableParam(
                            trainingExercise.exercise().id(),
                            trainingExercise.sets(),
                            trainingExercise.reps(),
                            trainingExercise.weight(),
                            trainingExercise.breakBetweenSets(),
                            false
                    )).toList()
            );
            editSessionPartParams.add(createSessionPartParams);
            iterator++;
        }
        sessionService.createSession(trainingConfiguration.plan().id(), new SessionService.EditSessionParams(
                SessionState.FOLLOW_UP,
                editSessionPartParams.stream().map(p -> new EditSessionPart(null, p)).toList() // TODO not a best way to handle this ;)
        ));
    }
}
