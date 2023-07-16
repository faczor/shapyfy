package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.session.model.Session;
import com.sd.shapyfy.domain.TrainingManagementAdapter.TrainingInitialConfiguration.SessionDayConfiguration;
import com.sd.shapyfy.domain.session.model.exception.ActiveSessionNotFound;
import com.sd.shapyfy.domain.session.model.exception.DraftSessionNotFound;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.trainingDay.TrainingDayType;
import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Value
public class Training {

    TrainingId id;

    UserId userId;

    String name;

    List<TrainingDay> trainingDays;

    public static Training initialize(UserId userId, TrainingManagementAdapter.TrainingInitialConfiguration trainingInitialConfiguration) {
        return new Training(
                null,
                userId,
                trainingInitialConfiguration.getName().orElse(trainingInitialConfiguration.getSessionDayConfigurations().stream().map(SessionDayConfiguration::getName).collect(joining(","))),
                trainingInitialConfiguration.getSessionDayConfigurations().stream().map(configuration -> TrainingDay.create(configuration.getName(), configuration.getDayOfWeek(), configuration.getDayType())).toList()
        );
    }

    public boolean isActive() {
        return trainingDays.stream().map(TrainingDay::getSessions)
                .flatMap(Collection::stream)
                .anyMatch(Session::isActive);
    }

    public boolean isDraft() {
        return trainingDays.stream().map(TrainingDay::getSessions)
                .flatMap(Collection::stream)
                .anyMatch(Session::isDraft);
    }

    @Value
    public static class TrainingDay {

        TrainingDayId id;

        String name;

        DayOfWeek day;

        TrainingDayType dayType;

        List<Session> sessions;

        private static TrainingDay create(String name, DayOfWeek day, TrainingDayType trainingDayType) {
            return new TrainingDay(null, name, day, trainingDayType, List.of());
        }

        //TODO better holding of off day
        public Session mostCurrentSession() {
            if (dayType == TrainingDayType.OFF) {
                return null;
            }

            try {
                return activeSession();
            } catch (ActiveSessionNotFound e) {
                return draftSession();
            }
        }

        private Session activeSession() {
            return sessions.stream().filter(Session::isActive).findFirst().orElseThrow(() -> new ActiveSessionNotFound(id));
        }

        private Session draftSession() {
            return sessions.stream().filter(Session::isDraft).findFirst().orElseThrow(() -> new DraftSessionNotFound(id));
        }
    }
}

