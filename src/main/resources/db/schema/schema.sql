--create schema shapyfy

CREATE TABLE shapyfy.trainings
(
    training_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(256) NOT NULL,
    user_id     UUID         NOT NULL
);

CREATE TABLE shapyfy.sessions
(
    session_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(256) NOT NULL,
    training_id UUID         NOT NULL,
    type        VARCHAR(32)  NOT NULL,
    day         VARCHAR(32)  NOT NULL,
    CONSTRAINT fk_training FOREIGN KEY (training_id) REFERENCES shapyfy.trainings (training_id)
);

CREATE TABLE shapyfy.exercises
(
    exercise_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(256) NOT NULL,
    videoUrl    VARCHAR(256) NOT NULL
);

CREATE TABLE shapyfy.session_exercises
(
    session_exercise_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sets_amount         INT     NOT NULL,
    reps_amount         INT     NOT NULL,
    weight_amount       DECIMAL NOT NULL,
    session_id          UUID    NOT NULL,
    exercise_id         UUID    NOT NULL,
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES shapyfy.sessions (session_id),
    CONSTRAINT fk_exercise FOREIGN KEY (exercise_id) REFERENCES shapyfy.exercises (exercise_id)
);