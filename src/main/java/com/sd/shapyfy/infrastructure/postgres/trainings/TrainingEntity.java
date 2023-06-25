package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.infrastructure.postgres.sessions.TrainingDayEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trainings")
public class TrainingEntity {

    @Id
    @Column(name = "training_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "user_id")
    private UUID userId;

    @OneToMany(mappedBy = "training")
    private Set<TrainingDayEntity> days = new HashSet<>();
}
