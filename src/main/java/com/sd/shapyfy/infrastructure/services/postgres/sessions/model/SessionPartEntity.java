package com.sd.shapyfy.infrastructure.services.postgres.sessions.model;

import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.component.PostgresSessionService;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.component.PostgresSessionService.UpdateSessionData.UpdateExercise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SessionState state;

    @Column(name = "session_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "training_day_id")
    private TrainingDayEntity trainingDay;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionEntity session;

    @OneToMany(mappedBy = "sessionPart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();

    public void update(PostgresSessionService.UpdateSessionData editableSessionParams) {
        Optional.ofNullable(editableSessionParams.state()).ifPresent(s -> this.state = s);
        Optional.ofNullable(editableSessionParams.date()).ifPresent(d -> this.date = d);
        Optional.ofNullable(editableSessionParams.updateExercises()).ifPresent(this::updateSessionExercises);
    }

    private void updateSessionExercises(List<UpdateExercise> exercises) {
        exercises.forEach(this::updateSingleSessionExercise);
    }

    private void updateSingleSessionExercise(UpdateExercise updateExerciseParams) {
        SessionExerciseEntity sessionExercise = this.getSessionExercises().stream()
                .filter(e -> Objects.equals(e.getExercise().getId(), updateExerciseParams.exercise().getId())).findFirst()
                .orElseGet(() -> {
                    SessionExerciseEntity createdExercise = new SessionExerciseEntity();
                    createdExercise.setExercise(updateExerciseParams.exercise());
                    createdExercise.setSessionPart(this);
                    this.sessionExercises.add(createdExercise);
                    return createdExercise;
                });
        sessionExercise.update(updateExerciseParams);
    }

}
