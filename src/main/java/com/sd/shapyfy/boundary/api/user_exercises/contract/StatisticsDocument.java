package com.sd.shapyfy.boundary.api.user_exercises.contract;

import com.sd.shapyfy.boundary.api.sessions.contract.AttributeDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.SetDocument;

import java.time.LocalDate;
import java.util.List;

public record StatisticsDocument(
        List<Statistics> statistics) {
    private record Statistics(
            LocalDate date,
            List<AttributeDocument> attributes,
            List<SetDocument> sets) {
    }
}
