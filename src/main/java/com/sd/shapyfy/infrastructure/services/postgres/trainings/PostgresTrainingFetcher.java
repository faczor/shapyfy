package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import com.sd.shapyfy.domain.TrainingFetcher;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.user.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresTrainingFetcher implements TrainingFetcher {

    private final PostgresTrainingRepository trainingRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    @Override
    public List<Training> fetchFor(UserId userId) {
        Collection<TrainingEntity> allByUserId = trainingRepository.findAllByUserId(userId.getValue());

        return allByUserId.stream().map(trainingEntityToDomainConverter::convert).toList();
    }
}
