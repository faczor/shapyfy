package com.sd.shapyfy.infrastructure.services.postgres.configuration;

import com.sd.shapyfy.domain.configuration.ConfigurationDayFetcher;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresConfigurationDayFetcher implements ConfigurationDayFetcher {

    private final PostgresConfigurationDayRepository configurationDayRepository;

    private final TrainingEntityToDomainConverter trainingEntityToDomainConverter;

    @Override
    public ConfigurationDay fetchForId(ConfigurationDayId configurationDayId) {
        log.info("Fetch {}", configurationDayId);
        ConfigurationPartEntity partEntity = findById(configurationDayId);

        return trainingEntityToDomainConverter.convertPartToDomain(partEntity);
    }

    //TODO
    public ConfigurationPartEntity findById(ConfigurationDayId configurationDayId) {
        return configurationDayRepository.findById(configurationDayId.getValue()).orElseThrow();
    }
}
