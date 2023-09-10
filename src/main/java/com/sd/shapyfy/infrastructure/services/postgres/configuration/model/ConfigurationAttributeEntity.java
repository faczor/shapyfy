package com.sd.shapyfy.infrastructure.services.postgres.configuration.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeType.EXERCISE;
import static com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeType.SET;

@Data
@Entity
@NoArgsConstructor
@Table(name = "configuration_attributes")
public class ConfigurationAttributeEntity {

    @Id
    @Column(name = "attribute_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ConfigurationAttributeType configurationAttributeType;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private ConfigurationEntity configuration;

    public ConfigurationAttributeEntity(String name, ConfigurationAttributeType configurationAttributeType) {
        this.name = name;
        this.configurationAttributeType = configurationAttributeType;
    }

    public static ConfigurationAttributeEntity forExercise(String name) {
        return new ConfigurationAttributeEntity(name, EXERCISE);
    }

    public static ConfigurationAttributeEntity forSet(String name) {
        return new ConfigurationAttributeEntity(name, SET);
    }
}
