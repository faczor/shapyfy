-- Create workouts table (aggregate root)
CREATE TABLE workouts (
    id UUID PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL,  -- Firebase UID (e.g., "CJ0MJjT8YSWHgSKddm7UKhqI62B2")
    status VARCHAR(20) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT chk_workout_status CHECK (status IN ('COMPLETED', 'IN_PROGRESS', 'ABANDONED')),
    CONSTRAINT chk_workout_times CHECK (end_time > start_time)
);

-- Create index for user workouts queries
CREATE INDEX idx_workouts_user_id_start_time ON workouts(user_id, start_time DESC);

-- Create workout_exercises table (part of workout aggregate)
CREATE TABLE workout_exercises (
    id UUID PRIMARY KEY,
    workout_id UUID NOT NULL,
    exercise_id UUID NOT NULL,
    order_index INT NOT NULL,
    CONSTRAINT fk_workout_exercises_workout FOREIGN KEY (workout_id)
        REFERENCES workouts(id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_exercises_exercise FOREIGN KEY (exercise_id)
        REFERENCES exercises(id),
    CONSTRAINT uq_workout_order UNIQUE(workout_id, order_index),
    CONSTRAINT chk_order_index CHECK (order_index >= 0)
);

-- Create index for exercise lookup
CREATE INDEX idx_workout_exercises_exercise_id ON workout_exercises(exercise_id);

-- Create workout_sets table (part of workout aggregate)
CREATE TABLE workout_sets (
    id UUID PRIMARY KEY,
    workout_exercise_id UUID NOT NULL,
    set_number INT NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    reps INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_workout_sets_exercise FOREIGN KEY (workout_exercise_id)
        REFERENCES workout_exercises(id) ON DELETE CASCADE,
    CONSTRAINT uq_exercise_set_number UNIQUE(workout_exercise_id, set_number),
    CONSTRAINT chk_set_number CHECK (set_number > 0),
    CONSTRAINT chk_weight CHECK (weight >= 0),
    CONSTRAINT chk_reps CHECK (reps > 0)
);
