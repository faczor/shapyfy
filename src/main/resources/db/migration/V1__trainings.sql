--create schema shapyfy

CREATE TABLE shapyfy.trainings
(
    training_id UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id     VARCHAR(64) NOT NULL,
    name        varchar(256),
    createdAt   timestamp   NOT NULL default now(),
    updatedAt   timestamp   NOT NULL default now()
);

CREATE TABLE shapyfy.training_days
(
    training_day_id UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name            VARCHAR(256) NOT NULL,
    is_off          BOOL         NOT NULL,
    createdAt       timestamp    NOT NULL default now(),
    updatedAt       timestamp    NOT NULL default now(),
    --
    training_id     UUID         NOT NULL,
    CONSTRAINT fk_training FOREIGN KEY (training_id) REFERENCES shapyfy.trainings (training_id)
);

CREATE TABLE shapyfy.exercises
(
    exercise_id UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    name        VARCHAR(256) UNIQUE NOT NULL,
    videoUrl    VARCHAR(256),
    createdAt   timestamp           NOT NULL default now(),
    updatedAt   timestamp           NOT NULL default now()
);

CREATE TABLE shapyfy.sessions
(
    session_id      UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    state           varchar(32) NOT NULL,
    session_date     timestamp,
    createdAt       timestamp   NOT NULL default now(),
    updatedAt       timestamp   NOT NULL default now(),
    --
    training_day_id UUID        NOT NULL,
    CONSTRAINT fk_training_day FOREIGN KEY (training_day_id) REFERENCES shapyfy.training_days (training_day_id)
);

CREATE TABLE shapyfy.session_exercises
(
    session_exercise_id UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    sets_amount         INT       NOT NULL,
    reps_amount         INT       NOT NULL,
    weight_amount       DECIMAL,
    is_finished         BOOLEAN   NOT NULL,
    createdAt           timestamp NOT NULL default now(),
    updatedAt           timestamp NOT NULL default now(),
    --
    session_id          UUID      NOT NULL,
    exercise_id         UUID      NOT NULL,
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES shapyfy.sessions (session_id),
    CONSTRAINT fk_exercise FOREIGN KEY (exercise_id) REFERENCES shapyfy.exercises (exercise_id)
);