package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.infrastructure.postgres.trainingDay.TrainingDayEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "trainings")
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEntity {

    @Id
    @Column(name = "training_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainingDayEntity> days;
}
