package com.sd.shapyfy.domain.exercises;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService implements ExerciseAdapter {

    private final ExercisePort exercisePort;

    @Override
    public List<Exercise> fetchExercises() {
        return exercisePort.fetchExercises();
    }
}
