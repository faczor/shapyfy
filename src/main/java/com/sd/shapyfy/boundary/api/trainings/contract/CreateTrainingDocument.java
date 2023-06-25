package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Data
public class CreateTrainingDocument {

    @JsonProperty("user_id")
    String userId;
}
