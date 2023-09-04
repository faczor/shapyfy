package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams.SelectedExercisesParams.SetConfiguration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "session_exercise_sets")
@NoArgsConstructor
@AllArgsConstructor
public class SessionExerciseSetEntity {

    @Id
    @Column(name = "set_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reps_amount")
    private int repsAmount;

    @Column(name = "weight_amount")
    private Double weightAmount;

    @Column(name = "is_finished")
    private boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "session_exercise_id")
    private SessionExerciseEntity sessionExercise;

    @OneToMany(mappedBy = "set")
    List<SetAdditionalAttributeEntity> attributes;

    public static SessionExerciseSetEntity from(SetConfiguration configParams) {
        return new SessionExerciseSetEntity(configParams);
    }

    private SessionExerciseSetEntity(SetConfiguration configParams) {
        this.repsAmount = configParams.repsAmount();
        this.weightAmount = configParams.weightAmount();
        this.attributes = new ArrayList<>();

        configParams.attributes().stream().map(SetAdditionalAttributeEntity::from).forEach(this::setAttribute);
    }

    private void setAttribute(SetAdditionalAttributeEntity attribute) {
        attribute.setSet(this);
        attributes.add(attribute);
    }
}
