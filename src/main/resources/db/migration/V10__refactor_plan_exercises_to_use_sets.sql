-- Create new exercise_sets table
CREATE TABLE exercise_sets (
    id UUID PRIMARY KEY,
    plan_exercise_id UUID NOT NULL,
    set_index INT NOT NULL,
    reps INT,
    weight DECIMAL(5, 2),
    CONSTRAINT fk_exercise_set_plan_exercise FOREIGN KEY (plan_exercise_id) REFERENCES plan_exercises(id) ON DELETE CASCADE
);

CREATE INDEX idx_exercise_sets_plan_exercise_id ON exercise_sets(plan_exercise_id);

-- Remove old columns from plan_exercises table
ALTER TABLE plan_exercises DROP COLUMN IF EXISTS target_sets;
ALTER TABLE plan_exercises DROP COLUMN IF EXISTS target_reps;
ALTER TABLE plan_exercises DROP COLUMN IF EXISTS target_weight;
