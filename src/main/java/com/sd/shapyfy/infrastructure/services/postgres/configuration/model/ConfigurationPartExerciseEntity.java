package com.sd.shapyfy.infrastructure.services.postgres.configuration.model;

import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.CreateConfigurationParams;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models.CreateConfigurationParams.SessionDayConfiguration.SelectedExercise;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "configuration_part_exercises")
public class ConfigurationPartExerciseEntity {

    @Id
    @Column(name = "configuration_part_exercise_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sets_amount")
    private int setsAmount;

    @Column(name = "reps_amount")
    private int repsAmount;

    @Column(name = "weight_amount")
    private Double weightAmount;

    @Column(name = "rest_between_sets")
    private int restBetweenSets;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseEntity exercise;

    @ManyToOne
    @JoinColumn(name = "configuration_part_id")
    private ConfigurationPartEntity part;

    public ConfigurationPartExerciseEntity(SelectedExercise selectedExercise) {
        this.setsAmount = selectedExercise.sets();
        this.repsAmount = selectedExercise.reps();
        this.weightAmount = selectedExercise.weight();
        this.restBetweenSets = selectedExercise.secondRestBetweenSets();
        this.exercise = selectedExercise.exercise();
    }

    public static ConfigurationPartExerciseEntity from(SelectedExercise selectedExercise) {
        return new ConfigurationPartExerciseEntity(selectedExercise);
    }
}
