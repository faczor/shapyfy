package com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.component.PostgresSessionService.UpdateSessionData;
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
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;
import static java.lang.String.format;

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

    @Column(name = "is_off")
    private boolean isOff;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private TrainingEntity training;

    @OneToMany(mappedBy = "trainingDay", fetch = EAGER, cascade = CascadeType.ALL)
    private List<SessionEntity> sessions = new ArrayList<>();

    public SessionEntity sessionWithState(SessionState state) {
        return sessions.stream().filter(session -> session.getState() == state).findFirst()
                .orElseThrow(() -> new SessionNotFound(format("Session with state %s not found in trainingDay %s", state, this.id)));
    }

    public void createNewSession(UpdateSessionData editableSessionParams) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setTrainingDay(this);
        sessionEntity.update(editableSessionParams);

        this.getSessions().add(sessionEntity);
    }

    public Optional<SessionEntity> getMostCurrentSession() {
        Optional<SessionEntity> activeSession = sessions.stream().filter(session -> session.getState().isActive()).findFirst();

        if (activeSession.isEmpty()) {
            return sessions.stream().filter(session -> session.getState().isDraft()).findFirst();
        }

        return activeSession;
    }
}