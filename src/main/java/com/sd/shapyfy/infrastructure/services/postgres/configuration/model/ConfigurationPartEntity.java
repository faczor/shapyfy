package com.sd.shapyfy.infrastructure.services.postgres.configuration.model;

import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.CreateConfigurationParams;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.CreateConfigurationParams.SessionDayConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "configuration_parts")
public class ConfigurationPartEntity {

    @Id
    @Column(name = "configuration_part_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SessionPartType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private ConfigurationEntity configuration;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<ConfigurationPartExerciseEntity> exercises;

    //TODO switch from string exerciseAttributes to ValueObject :)
    public static ConfigurationPartEntity from(SessionDayConfiguration dayConfiguration) {
        return new ConfigurationPartEntity(dayConfiguration);
    }

    private ConfigurationPartEntity(SessionDayConfiguration dayConfiguration) {
        this.name = dayConfiguration.name();
        this.type = dayConfiguration.dayType();
        this.exercises = new ArrayList<>();
        dayConfiguration.selectedExercises().stream().map(ConfigurationPartExerciseEntity::from).forEach(this::addExercise);
    }

    public void addExercise(ConfigurationPartExerciseEntity exercise) {
        exercise.setPart(this);
        this.exercises.add(exercise);
    }
}
