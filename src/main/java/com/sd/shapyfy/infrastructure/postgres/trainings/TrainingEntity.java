package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.infrastructure.postgres.sessions.SessionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "training")
    private Set<SessionEntity> sessions = new HashSet<>();
}
