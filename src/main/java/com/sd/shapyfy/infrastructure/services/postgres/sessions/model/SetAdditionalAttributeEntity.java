package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

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
@Table(name = "session_exercise_set_additional_attributes")
@NoArgsConstructor
@AllArgsConstructor
public class SetAdditionalAttributeEntity {

    @Id
    @Column(name = "attribute_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private SessionExerciseSetEntity set;

    public static SetAdditionalAttributeEntity from(ConfigurationAttributeEntity configurationAttributeEntity) {
        if (configurationAttributeEntity.getConfigurationAttributeType() != ConfigurationAttributeType.SET) {
            throw new IllegalStateException();
        }
        return new SetAdditionalAttributeEntity(configurationAttributeEntity.getName());
    }

    private SetAdditionalAttributeEntity(String name) {
        this.name = name;
    }
}
