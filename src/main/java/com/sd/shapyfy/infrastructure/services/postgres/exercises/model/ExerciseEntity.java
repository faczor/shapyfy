package com.sd.shapyfy.infrastructure.services.postgres.exercises.model;

import com.sd.shapyfy.boundary.api.exercises.Creator;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "creator")
    private Creator creator;

    @OneToMany(mappedBy = "exercise")
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();

    public static ExerciseEntity create(String name, Creator creator) {
        return new ExerciseEntity(
                null,
                name,
                creator,
                new ArrayList<>()
        );
    }
}
