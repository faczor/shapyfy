package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingPlanToEntityConverter {

    public TrainingEntity onCreation(String name, UserId userId) {
        return new TrainingEntity(
                null,
                userId.getValue(),
                name,
                List.of(),
                List.of()
        );
    }
}
