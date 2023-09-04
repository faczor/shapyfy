package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.*;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.SessionExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.converter.SessionEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.*;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class TrainingToDomainConverter {

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    private final SessionEntityToDomainConverter sessionEntityToDomainConverter;

    public Training convert(TrainingEntity training) {

        return new Training(
                trainingEntityToDomainConverter.convertToConfiguration(training),
                training.getSessions().stream().map(sessionEntityToDomainConverter::convert).toList()
        );
    }
}
