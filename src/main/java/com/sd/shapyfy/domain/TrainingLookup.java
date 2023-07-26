package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.*;
import com.sd.shapyfy.domain.model.exception.CurrentTrainingNotFound;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.domain.session.Session;
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
        List<Plan> userPlans = trainingFetcher.fetchFor(userId);

        Plan activePlan = findActiveTraining(userPlans).orElseThrow(() -> new CurrentTrainingNotFound(userId));

        return CurrentTraining.from(activePlan);
    }

    private Optional<Plan> findActiveTraining(List<Plan> userPlans) {
        return userPlans.stream().filter(Plan::isActive)
                .findAny()
                .or(() -> userPlans.stream().filter(Plan::isDraft).findFirst());
    }

    public record CurrentTraining(
            PlanId planId,
            String name,
            List<Day> days) {

        //TODO proper exception
        public Session sessionFor(LocalDate localDate) {
            return days.stream().map(Day::session).filter(session -> session.date().equals(localDate))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Session not found for " + localDate));
        }

        public record Day(
                TrainingDayId trainingDayId,
                String name,
                ConfigurationDayType dayType,
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

        public static CurrentTraining from(Plan plan) {
            return new CurrentTraining(
                    plan.getId(),
                    plan.getName(),
                    plan.getTrainingDays().stream().map(Day::from).toList()
            );
        }
    }
}
