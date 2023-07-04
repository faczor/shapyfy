package com.sd.shapyfy.infrastructure.postgres.trainingDayExercises;

import com.sd.shapyfy.infrastructure.postgres.exercises.ExerciseEntity;
import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "training_day_exercises")
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayExerciseEntity {


    @Id
    @Column(name = "training_day_exercises_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sets_amount")
    private int setsAmount;

    @Column(name = "reps_amount")
    private int repsAmount;

    @Column(name = "weight_amount")
    private Double weightAmount;

    @Column(name = "execution_order")
    private int order;

    @ManyToOne
    @JoinColumn(name = "training_days_id")
    private TrainingDayEntity trainingDay;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;
}
