package com.sd.shapyfy.domain.model;

import com.sd.shapyfy.domain.model.exception.ActiveSessionNotFound;
import com.sd.shapyfy.domain.model.exception.DraftSessionNotFound;
import com.sd.shapyfy.domain.session.Session;
import lombok.Value;

import java.time.DayOfWeek;
import java.util.List;

@Value
public class TrainingDay {

    TrainingDayId id;

    String name;

    DayOfWeek day;

    ConfigurationDayType dayType;

    List<Session> sessions;

    public static TrainingDay create(String name, DayOfWeek day, ConfigurationDayType configurationDayType) {
        return new TrainingDay(null, name, day, configurationDayType, List.of());
    }

    //TODO better holding of off day
    public Session mostCurrentSession() {
        if (dayType == ConfigurationDayType.REST) {
            return null;
        }

        try {
            return activeSession();
        } catch (ActiveSessionNotFound e) {
            return draftSession();
        }
    }

    public boolean isTrainingDay() {
        return dayType != ConfigurationDayType.REST;
    }

    private Session activeSession() {
        return sessions.stream().filter(Session::isActive).findFirst().orElseThrow(() -> new ActiveSessionNotFound(id));
    }

    public Session draftSession() {
        return sessions.stream().filter(Session::isDraft).findFirst().orElseThrow(() -> new DraftSessionNotFound(id));
    }
}
