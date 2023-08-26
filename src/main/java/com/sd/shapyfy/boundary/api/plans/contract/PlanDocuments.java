package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlanDocuments(

        @JsonProperty(value = "plans", required = true)
        List<PlanDocument> plans) {
    public static PlanDocuments from(List<PlanDocument> list) {
        return new PlanDocuments(list);
    }
}
