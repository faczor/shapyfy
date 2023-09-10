package com.sd.shapyfy.infrastructure.services.postgres.configuration;

import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresConfigurationDayRepository extends JpaRepository<ConfigurationPartEntity, UUID> {
}
