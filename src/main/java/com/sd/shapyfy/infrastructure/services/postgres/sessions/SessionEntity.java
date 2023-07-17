package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.domain.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import jakarta.persistence.*;
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
}
