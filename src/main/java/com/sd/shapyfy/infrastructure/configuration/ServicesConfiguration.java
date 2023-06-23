package com.sd.shapyfy.infrastructure.configuration;

import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.infrastructure.postgres.trainings.PostgresTrainingPort;
import com.sd.shapyfy.infrastructure.postgres.trainings.PostgresTrainingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {

    @Bean
    TrainingPort trainingFetcher(PostgresTrainingRepository trainingRepository) {
        return new PostgresTrainingPort(
                new PostgresTrainingPort.TrainingConverter(),
                trainingRepository
        );
    }
}
