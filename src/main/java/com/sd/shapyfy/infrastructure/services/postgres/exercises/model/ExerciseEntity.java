package com.sd.shapyfy.infrastructure.services.postgres.exercises.model;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "exercises")
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {

    @Id
    @Column(name = "exercise_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "exercise")
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();
}
