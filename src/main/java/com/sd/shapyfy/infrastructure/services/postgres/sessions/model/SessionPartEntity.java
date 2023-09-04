package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.domain.configuration.SessionService;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "session_parts")
@NoArgsConstructor
@AllArgsConstructor
public class SessionPartEntity {

    @Id
    @Column(name = "session_part_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SessionPartType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SessionPartState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "existence_type")
    private ExistenceType existenceType;

    @Column(name = "session_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionEntity session;

    @OneToMany(mappedBy = "sessionPart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();

  //  public static SessionPartEntity from(SessionPartCreationParams param) {
  //      return new SessionPartEntity(param);
  //  }

    private SessionPartEntity(SessionPartCreationParams param) {
        this.name = param.name();
        this.type = param.type();
        this.state = param.state();
        this.existenceType = ExistenceType.CONSTANT;
        this.date = param.date();
        this.sessionExercises = new ArrayList<>(); //TODO is this needed?
        param.selectedExercisesPrams().stream().map(SessionExerciseEntity::from).forEach(this::addSessionExercise);
    }

    private void addSessionExercise(SessionExerciseEntity sessionExerciseEntity) {
        sessionExerciseEntity.setSessionPart(this);
        this.sessionExercises.add(sessionExerciseEntity);
    }

    public static SessionPartEntity from(SessionPartCreationParams createSessionPartRequestParams) {
        return new SessionPartEntity(createSessionPartRequestParams);
    }
}
