package com.sd.shapyfy.boundary.sessions.contract;

import com.sd.shapyfy.domain.session.SessionType;
import lombok.Value;

import java.time.DayOfWeek;

@Value
public class CreateSessionDocument {

    DayOfWeek day;

    SessionType sessionType;
}
