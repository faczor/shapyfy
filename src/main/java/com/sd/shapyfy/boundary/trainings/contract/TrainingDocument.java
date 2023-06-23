package com.sd.shapyfy.boundary.trainings.contract;

import com.sd.shapyfy.domain.training.Training;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class TrainingDocument {

    @NotNull
    String id;

    public static TrainingDocument from(Training training) {
        return new TrainingDocument(
                training.getId().getValue()
        );
    }
}
