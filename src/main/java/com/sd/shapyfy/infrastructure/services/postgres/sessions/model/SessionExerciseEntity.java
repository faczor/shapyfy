package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.component.PostgresSessionService.UpdateSessionData.UpdateExercise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
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

    @Column(name = "sets_amount")
    private int setsAmount;

    @Column(name = "reps_amount")
    private int repsAmount;

    @Column(name = "weight_amount")
    private Double weightAmount;

    @Column(name = "is_finished")
    private boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @ManyToOne
    @JoinColumn(name = "session_part_id")
    private SessionPartEntity sessionPart;

    public static SessionExerciseEntity from(int sets, int reps, Double weight, ExerciseEntity exercise, SessionPartEntity session) {
        return new SessionExerciseEntity(null, sets, reps, weight, false, exercise, session);
    }

    public void update(UpdateExercise updateExerciseParams) {
        Optional.ofNullable(updateExerciseParams.setsAmount()).ifPresent(this::setSetsAmount);
        Optional.ofNullable(updateExerciseParams.repsAmount()).ifPresent(this::setRepsAmount);
        Optional.ofNullable(updateExerciseParams.weightAmount()).ifPresent(this::setWeightAmount);
        Optional.ofNullable(updateExerciseParams.isFinished()).ifPresent(this::setFinished);
    }
}
