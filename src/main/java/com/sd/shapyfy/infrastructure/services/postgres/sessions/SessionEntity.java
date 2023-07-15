package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
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

    @Column(name = "state")
    private String state;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_day_id")
    private TrainingDayEntity trainingDay;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SessionExerciseEntity> sessionExercises = new ArrayList<>();

    public static SessionEntity init(TrainingDayEntity trainingDay) {
        return new SessionEntity(
                null,
                "DRAFT",
                trainingDay,
                List.of()
        );
    }
}
