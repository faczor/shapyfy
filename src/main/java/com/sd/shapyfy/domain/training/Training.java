package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

@Value
public class Training {
    TrainingId id;

    UserId userId;

    public static Training initialize(UserId userId) {
        return new Training(
                null,
                userId
        );
    }
}

//1. Training Service - DOMAIN
//2. Class TrainingLookup (use interface TrainingFetcher - PostgresTrainingFetcher impl in infrastructure)