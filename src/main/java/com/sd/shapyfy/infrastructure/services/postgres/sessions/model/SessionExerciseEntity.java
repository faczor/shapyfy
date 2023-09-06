package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams.SelectedExercisesParams;
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
@Table(name = "session_exercises")
@NoArgsConstructor
@AllArgsConstructor
public class SessionExerciseEntity {

    @Id
    @Column(name = "session_exercise_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rest_between_sets")
    private int restBetweenSets;

    @Column(name = "is_finished")
    private boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @ManyToOne
    @JoinColumn(name = "session_part_id")
    private SessionPartEntity sessionPart;

    @OneToMany(mappedBy = "sessionExercise", cascade = CascadeType.ALL)
    private List<SessionExerciseSetEntity> sets;

    @OneToMany(mappedBy = "sessionExercise", cascade = CascadeType.ALL)
    private List<SessionExerciseAdditionalAttributeEntity> attributes;

    public static SessionExerciseEntity from(SelectedExercisesParams params) {
        return new SessionExerciseEntity(params);
    }

    private SessionExerciseEntity(SelectedExercisesParams params) {
        this.restBetweenSets = params.restBetweenSets();
        this.exercise = params.exercise();
        this.sets = new ArrayList<>();
        this.attributes = new ArrayList<>();
        params.setConfigurations().stream().map(SessionExerciseSetEntity::from).forEach(this::addSet);
        params.attributes().stream().map(SessionExerciseAdditionalAttributeEntity::from).forEach(this::addAttribute);
    }

    private void addAttribute(SessionExerciseAdditionalAttributeEntity attribute) {
        attribute.setSessionExercise(this);
        this.attributes.add(attribute);
    }

    private void addSet(SessionExerciseSetEntity set) {
        set.setSessionExercise(this);
        this.sets.add(set);
    }
}
