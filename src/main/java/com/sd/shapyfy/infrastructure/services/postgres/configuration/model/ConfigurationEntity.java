package com.sd.shapyfy.infrastructure.services.postgres.configuration.model;

import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.CreateConfigurationParams;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Data
@NoArgsConstructor
@Table(name = "configurations")
public class ConfigurationEntity {

    @Id
    @Column(name = "configuration_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL)
    List<ConfigurationPartEntity> parts;

    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL)
    List<ConfigurationAttributeEntity> attributes;

    @OneToOne(mappedBy = "configuration")
    TrainingEntity training;

    public static ConfigurationEntity create(CreateConfigurationParams configurationParams) {
        return new ConfigurationEntity(configurationParams);
    }

    private ConfigurationEntity(CreateConfigurationParams params) {
        this.name = params.name();
        this.parts = new ArrayList<>();
        params.sessionDayConfigurations().stream().map(ConfigurationPartEntity::from).forEach(this::addPart);

        this.attributes = new ArrayList<>();

        Stream.concat(
                params.exerciseAttributes().stream().map(ConfigurationAttributeEntity::forExercise),
                params.setAttributes().stream().map(ConfigurationAttributeEntity::forSet)
        ).forEach(this::addAttribute);
    }

    public void addPart(ConfigurationPartEntity part) {
        part.setConfiguration(this);
        this.parts.add(part);
    }

    public void addAttribute(ConfigurationAttributeEntity attribute) {
        attribute.setConfiguration(this);
        this.attributes.add(attribute);
    }
}
