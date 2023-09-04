package com.sd.shapyfy.infrastructure.services.postgres.trainings.model;

import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
    private List<SessionEntity> sessions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id")
    private ConfigurationEntity configuration;

    public TrainingEntity(UserId userId, String name) {
        this.name = name;
        this.userId = userId.getValue();
    }

    public static TrainingEntity create(String name, UserId userId) {

        return new TrainingEntity(userId, name);
    }

    public SessionEntity createNewSession() {
        SessionEntity sessionEntity = new SessionEntity(
                null,
                SessionState.DRAFT,
                this,
                new ArrayList<>());

        sessions.add(sessionEntity);
        return sessionEntity;
    }

    public SessionEntity getConfigurationSession() {
        Optional<SessionEntity> draftSession = sessions.stream().filter(session -> session.getState().isDraft()).findFirst();

        return draftSession.orElseGet(() -> sessions.stream().filter(session -> session.getState() == SessionState.FOLLOW_UP || session.getState().isActive()).findFirst().orElseThrow());

    }

    public SessionEntity createSession(SessionState state, List<SessionPartCreationParams> sessionPartRequestParams) {
        SessionEntity session = SessionEntity.from(state, sessionPartRequestParams);
        addSession(session);

        return session;
    }

    private void addSession(SessionEntity session) {
        session.setTraining(this);
        sessions.add(session);
    }
}
