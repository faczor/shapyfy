package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class TrainingToDomainConverter {

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    public Training convert(TrainingEntity training) {
        return new Training(
                planConfigurationToDomainConverter.convert(training, training.getDays()),
                training.getSessions().stream().map(this::toSession).toList()
        );
    }

    //TODO move to proper converter :)
    private Session toSession(SessionEntity sessionEntity) {
        LocalDate firstDate = min(sessionEntity.getSessionParts(), comparing(SessionPartEntity::getDate)).getDate();
        LocalDate lastDate = max(sessionEntity.getSessionParts(), comparing(SessionPartEntity::getDate)).getDate();

        List<TrainingDayEntity> days = sessionEntity.getTraining().getDays();


        return new Session(
                SessionId.of(sessionEntity.getId()),
                sessionEntity.getSessionParts().stream().map(trainingDayToDomainConverter::toSession).toList(),
                new DateRange(
                        firstDate.minusDays(calculateRestDaysBefore(days)),
                        lastDate.plusDays(calculateRestDaysAfter(days)))
        );
    }

    private int calculateRestDaysAfter(List<TrainingDayEntity> days) {
        int index = 0;

        for (int i = days.size() - 1; i > 0; i--) {
            if (!days.get(i).isOff()) {
                return index;
            }
            index++;
        }

        return index;
    }

    private int calculateRestDaysBefore(List<TrainingDayEntity> days) {
        int index = 0;
        for (TrainingDayEntity day : days) {
            if (!day.isOff()) {
                return index;
            }
            index++;
        }
        return index;
    }
}
