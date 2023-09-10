package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.domain.plan.TrainingProcess;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeEntity;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "session_exercise_additional_attributes")
@NoArgsConstructor
@AllArgsConstructor
public class SessionExerciseAdditionalAttributeEntity {

    @Id
    @Column(name = "attribute_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "session_exercise_id")
    private SessionExerciseEntity sessionExercise;

    public static SessionExerciseAdditionalAttributeEntity from(ConfigurationAttributeEntity configurationAttributeEntity) {
        if (configurationAttributeEntity.getConfigurationAttributeType() != ConfigurationAttributeType.EXERCISE) {
            throw new IllegalStateException();//TODO proper exception
        }
        return new SessionExerciseAdditionalAttributeEntity(configurationAttributeEntity);
    }

    public SessionExerciseAdditionalAttributeEntity(ConfigurationAttributeEntity configurationAttributeEntity) {
        this.name = configurationAttributeEntity.getName();
    }

    public void update(TrainingProcess.UpdateTrainingExercise.UpdateAttributeRequest updateAttributeRequest) {
        this.value = updateAttributeRequest.value();
    }
}
