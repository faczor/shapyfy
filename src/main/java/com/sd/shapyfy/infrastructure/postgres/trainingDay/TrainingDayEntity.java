package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.UUID;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_id")
    private TrainingEntity training;
}
