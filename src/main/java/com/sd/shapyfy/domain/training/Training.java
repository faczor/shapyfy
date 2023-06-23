package com.sd.shapyfy.domain.training;

import lombok.Value;

@Value
public class Training {
    TrainingId id;
}

//1. Training Service - DOMAIN
//2. Class TrainingLookup (use interface TrainingFetcher - PostgresTrainingFetcher impl in infrastructure)