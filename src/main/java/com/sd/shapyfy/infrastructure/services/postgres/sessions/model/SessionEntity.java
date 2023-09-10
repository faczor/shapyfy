package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SessionState state;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private TrainingEntity training;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SessionPartEntity> sessionParts = new ArrayList<>();

    private SessionEntity(SessionState state, List<SessionPartCreationParams> sessionPartRequestParams) {
        this.state = state;
        this.sessionParts = new ArrayList<>() ;
        sessionPartRequestParams.stream().map(SessionPartEntity::from).forEach(this::addPart);
    }

    public void addPart(SessionPartEntity sessionPartEntity) {
        sessionPartEntity.setSession(this);
        this.sessionParts.add(sessionPartEntity);
    }

    public static SessionEntity from(SessionState state, List<SessionPartCreationParams> sessionPartRequestParams) {
        return new SessionEntity(state, sessionPartRequestParams);
    }

}
