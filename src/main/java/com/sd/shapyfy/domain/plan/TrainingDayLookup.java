package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingDayLookup {

    private final TrainingDayFetcher trainingDayFetcher;

    public SessionPart lookupById(SessionPartId sessionPartId) {
        log.info("Lookup {}", sessionPartId);
        return trainingDayFetcher.fetchFor(sessionPartId);
    }
}
