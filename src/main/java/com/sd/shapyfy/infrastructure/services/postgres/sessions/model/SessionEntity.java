package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

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
    private List<SessionPartEntity> sessionParts;

    public SessionPartEntity createPart(String name, SessionPartType type) {
        SessionPartEntity sessionPartEntity = new SessionPartEntity(
                null, name, type, SessionPartState.SKIP, ExistanceType.CONSTANT, null, this, new ArrayList<>()
        );
        sessionParts.add(sessionPartEntity);

        return sessionPartEntity;
    }

    public void update(SessionState state) {
        this.state = state;
    }
}
