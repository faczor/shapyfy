package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.infrastructure.postgres.trainingDayExercises.TrainingDayExerciseEntity;
import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@Table(name = "training_days")
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDayEntity {

    @Id
    @Column(name = "training_day_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private DayOfWeek day;

    @Column(name = "execution_order", nullable = false)
    private int order;

    @Column(name = "is_off")
    private boolean isOff;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private TrainingEntity training;

    @OneToMany(mappedBy = "trainingDay", fetch = EAGER)
    private List<TrainingDayExerciseEntity> trainingDayExercise = new ArrayList<>();
}
