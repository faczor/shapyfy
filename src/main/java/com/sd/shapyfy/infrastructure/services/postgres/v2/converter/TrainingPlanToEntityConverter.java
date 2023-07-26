package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
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
                List.of()
        );
    }
}
