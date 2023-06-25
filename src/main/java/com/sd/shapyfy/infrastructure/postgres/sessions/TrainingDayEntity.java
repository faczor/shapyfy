package com.sd.shapyfy.infrastructure.postgres.sessions;

import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;

@Data
@Entity
@Table(name = "training_days")
public class TrainingDayEntity {

    @Id
    @Column(name = "training_day_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name", nullable = false)
    private String email;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "type", nullable = false)
    // private TrainingDayType trainingDayType;

    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private DayOfWeek day;

    @Column(name = "execution_order", nullable = false)
    private int order;

    @ManyToOne
    private TrainingEntity training;
}
