package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.domain.exercise.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseLookup {

    private final ExerciseFetcher exerciseFetcher;

    public List<Exercise> fetchExercises() {
        return exerciseFetcher.fetchExercises();
    }
}
