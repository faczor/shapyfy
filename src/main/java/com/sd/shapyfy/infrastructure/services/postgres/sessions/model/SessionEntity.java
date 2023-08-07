package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "sessions")
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private TrainingEntity training;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<SessionPartEntity> sessionParts;

    public Optional<SessionPartEntity> findSessionWithState(TrainingDayEntity trainingDayEntity, SessionState state) {
        return sessionParts.stream().filter(part -> part.getState() == state && Objects.equals(part.getTrainingDay().getId(), trainingDayEntity.getId())).findFirst();
    }

    public SessionPartEntity createPart(SessionState state, TrainingDayEntity trainingDayEntity) {
        SessionPartEntity sessionPartEntity = new SessionPartEntity(
                null, state, null, trainingDayEntity, this, new ArrayList<>()
        );
        sessionParts.add(sessionPartEntity);
        trainingDayEntity.getSessions().add(sessionPartEntity);

        return sessionPartEntity;
    }

}
