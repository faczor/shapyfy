package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "session_id")
    private SessionEntity session;
}
