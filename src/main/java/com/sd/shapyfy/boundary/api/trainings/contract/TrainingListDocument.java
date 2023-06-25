package com.sd.shapyfy.boundary.api.trainings.contract;

import lombok.Value;

import java.util.List;

@Value
public class TrainingListDocument {

    List<TrainingDocument> items;
}
