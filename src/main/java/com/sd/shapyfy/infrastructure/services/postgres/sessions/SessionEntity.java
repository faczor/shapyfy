package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.domain.SessionService;
import com.sd.shapyfy.domain.SessionService.EditableSessionParams.SessionExerciseExerciseEditableParam;
import com.sd.shapyfy.domain.session.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.v2.PostgresqlSessionService;
import com.sd.shapyfy.infrastructure.services.postgres.v2.PostgresqlSessionService.UpdateSessionData.UpdateExercise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SessionState state;

    @Column(name = "session_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "training_day_id")
    private TrainingDayEntity trainingDay;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();

    public static SessionEntity init(TrainingDayEntity trainingDay) {
        return new SessionEntity(
                null,
                SessionState.DRAFT,
                null,
                trainingDay,
                List.of()
        );
    }

    public static SessionEntity draftSession(TrainingDayEntity trainingDay) {
        return new SessionEntity(
                null,
                SessionState.DRAFT,
                null,
                trainingDay,
                List.of()
        );
    }

    public void update(PostgresqlSessionService.UpdateSessionData editableSessionParams) {
        Optional.ofNullable(editableSessionParams.state()).ifPresent(s -> this.state = s);
        Optional.ofNullable(editableSessionParams.date()).ifPresent(d -> this.date = d);
        Optional.ofNullable(editableSessionParams.updateExercises()).ifPresent(this::editExercises);
    }

    //TODO rename
    private void editExercises(List<UpdateExercise> exercises) {
        exercises.forEach(this::exerciseEdit);
    }

    private void exerciseEdit(UpdateExercise updateExerciseParams) {
        SessionExerciseEntity sessionExercise = this.getSessionExercises().stream()
                .filter(e -> Objects.equals(e.getExercise().getId(), updateExerciseParams.exercise().getId())).findFirst()
                .orElseGet(() -> {
                    SessionExerciseEntity createdExercise = new SessionExerciseEntity();
                    createdExercise.setExercise(updateExerciseParams.exercise());
                    createdExercise.setSession(this);
                    this.sessionExercises.add(createdExercise);
                    return createdExercise;
                });
        sessionExercise.update(updateExerciseParams);
    }

}
