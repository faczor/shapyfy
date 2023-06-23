package com.sd.shapyfy.boundary.trainings.contract;

import lombok.Value;

import java.util.List;

@Value
public class TrainingListDocument {

    List<TrainingDocument> items;
}
