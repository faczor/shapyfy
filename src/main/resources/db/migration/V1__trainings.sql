--create schema shapyfy

CREATE TABLE shapyfy.trainings
(
    training_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     VARCHAR(64) NOT NULL,
    name        varchar(256)
);

CREATE TABLE shapyfy.training_days
(
    training_day_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(256) NOT NULL,
    training_id     UUID         NOT NULL,
    is_off          BOOL         NOT NULL,
    day             VARCHAR(32)  NOT NULL,
    execution_order INTEGER      NOT NULL,
    CONSTRAINT fk_training FOREIGN KEY (training_id) REFERENCES shapyfy.trainings (training_id)
);

CREATE TABLE shapyfy.exercises
(
    exercise_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(256) UNIQUE NOT NULL,
    videoUrl    VARCHAR(256)
);

CREATE TABLE shapyfy.training_day_exercises
(
    training_day_exercises_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sets_amount               INT  NOT NULL,
    reps_amount               INT  NOT NULL,
    weight_amount             DECIMAL,
    execution_order           INT  NOT NULL,
    training_days_id          UUID NOT NULL,
    exercise_id               UUID NOT NULL,
    CONSTRAINT fk_session FOREIGN KEY (training_days_id) REFERENCES shapyfy.training_days (training_day_id),
    CONSTRAINT fk_exercise FOREIGN KEY (exercise_id) REFERENCES shapyfy.exercises (exercise_id)
);