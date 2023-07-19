package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.*;
import com.sd.shapyfy.domain.model.exception.CurrentTrainingNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingLookup {

    private final TrainingFetcher trainingFetcher;

    public CurrentTraining currentTrainingFor(UserId userId) {
        log.info("Attempt to fetch current training for {}", userId);
        List<Training> userTrainings = trainingFetcher.fetchFor(userId);

        Training activeTraining = findActiveTraining(userTrainings).orElseThrow(() -> new CurrentTrainingNotFound(userId));

        return CurrentTraining.from(activeTraining);
    }

    private Optional<Training> findActiveTraining(List<Training> userTrainings) {
        return userTrainings.stream().filter(Training::isActive)
                .findAny()
                .or(() -> userTrainings.stream().filter(Training::isDraft).findFirst());
    }

    public record CurrentTraining(
            TrainingId trainingId,
            String name,
            List<Day> days) {

        //TODO proper exception
        public Session sessionFor(LocalDate localDate) {
            return days.stream().map(Day::session).filter(session -> session.getDate().equals(localDate))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Session not found for " + localDate));
        }

        public record Day(
                TrainingDayId trainingDayId,
                String name,
                TrainingDayType dayType,
                DayOfWeek dayOfWeek,
                Session session
        ) {
            public static Day from(TrainingDay trainingDay) {
                return new Day(
                        trainingDay.getId(),
                        trainingDay.getName(),
                        trainingDay.getDayType(),
                        trainingDay.getDay(),
                        trainingDay.mostCurrentSession()
                );
            }
        }

        public static CurrentTraining from(Training training) {
            return new CurrentTraining(
                    training.getId(),
                    training.getName(),
                    training.getTrainingDays().stream().map(Day::from).toList()
            );
        }
    }
}
