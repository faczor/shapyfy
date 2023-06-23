package com.sd.shapyfy.infrastructure.postgres.sessions;

import com.sd.shapyfy.infrastructure.postgres.trainings.TrainingEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    private TrainingEntity training;

}
