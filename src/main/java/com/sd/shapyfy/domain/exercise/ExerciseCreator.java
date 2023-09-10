package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.boundary.api.exercises.Creator;
import com.sd.shapyfy.domain.exercise.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExerciseCreator {

    private final ExerciseService exerciseService;

    public Exercise create(String name, Creator creator) {
        log.info("Attempt to create exercise {} creator {}", name, creator);

        return exerciseService.create(name, creator);
    }
}
