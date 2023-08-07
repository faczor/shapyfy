package com.sd.shapyfy.infrastructure.services.postgres.trainings.model;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
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

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SessionEntity> sessions;

    public SessionEntity createNewSession() {
        SessionEntity sessionEntity = new SessionEntity(
               null, this, new ArrayList<>()
        );
        sessions.add(sessionEntity);

        return sessionEntity;
    }

    public Optional<SessionEntity> findSessionWithState(SessionState state) {
        return sessions.stream().filter(session -> session.getSessionParts().stream().anyMatch(part -> part.getState() == state)).findFirst();
    }
}
