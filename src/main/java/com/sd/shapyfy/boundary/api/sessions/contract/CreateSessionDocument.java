package com.sd.shapyfy.boundary.api.sessions.contract;

import com.sd.shapyfy.domain.trainingDay.SessionType;
import lombok.Value;

import java.time.DayOfWeek;

@Value
public class CreateSessionDocument {

    DayOfWeek day;

    SessionType sessionType;
}
