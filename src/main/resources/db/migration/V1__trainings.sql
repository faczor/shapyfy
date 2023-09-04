--create schema shapyfy

CREATE TABLE shapyfy.exercises
(
    exercise_id UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    name        VARCHAR(256) UNIQUE NOT NULL,
    videoUrl    VARCHAR(256),
    creator     VARCHAR(32)         NOT NULL,
    created_at   timestamp           NOT NULL default now(),
    updated_at   timestamp           NOT NULL default now()
);

CREATE TABLE shapyfy.configurations
(
    configuration_id uuid PRIMARY KEY   DEFAULT gen_random_uuid(),
    name             varchar(256),
    created_at       timestamp NOT NULL default now(),
    updated_at       timestamp NOT NULL default now()
);

CREATE TABLE shapyfy.configuration_parts
(
    configuration_part_id UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name                  VARCHAR(256),
    type                  varchar(32) NOT NULL,
    created_at            timestamp   NOT NULL default now(),
    updated_at            timestamp   NOT NULL default now(),
    --
    configuration_id      uuid        NOT NULL,
    CONSTRAINT fk_configuration FOREIGN KEY (configuration_id) REFERENCES shapyfy.configurations (configuration_id)

);

CREATE TABLE shapyfy.configuration_part_exercises
(
    configuration_part_exercise_id UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    rest_between_sets              INT       NOT NULL,
    sets_amount                    INT       NOT NULL,
    reps_amount                    INT       NOT NULL,
    weight_amount                  DECIMAL,
    created_at                     timestamp NOT NULL default now(),
    updated_at                     timestamp NOT NULL default now(),
    --
    configuration_part_id          uuid      NOT NULL,
    exercise_id                    uuid      NOT NULL,
    CONSTRAINT fk_configuration_part FOREIGN KEY (configuration_part_id) REFERENCES shapyfy.configuration_parts (configuration_part_id),
    CONSTRAINT fk_exercise FOREIGN KEY (exercise_id) REFERENCES shapyfy.exercises (exercise_id)
);

CREATE TABLE shapyfy.configuration_attributes
(
    attribute_id                   uuid primary key default gen_random_uuid(),
    name                           varchar(256) NOT NULL,
    type                           varchar(32)  NOT NULL,
    --
    configuration_id      uuid        NOT NULL,
    CONSTRAINT fk_configuration FOREIGN KEY (configuration_id) REFERENCES shapyfy.configurations (configuration_id)
);

CREATE TABLE shapyfy.trainings
(
    training_id      UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id          VARCHAR(64) NOT NULL,
    name             varchar(256),
    created_at       timestamp   NOT NULL default now(),
    updated_at       timestamp   NOT NULL default now(),
    --
    configuration_id uuid        NOT NULL,
    CONSTRAINT fk_configuration FOREIGN KEY (configuration_id) REFERENCES shapyfy.configurations (configuration_id)

);

CREATE TABLE shapyfy.sessions
(
    session_id  UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    state       varchar(32) NOT NULL,
    created_at   timestamp   NOT NULL default now(),
    updated_at   timestamp   NOT NULL default now(),
    --
    training_id UUID        NOT NULL,
    CONSTRAINT fk_training FOREIGN KEY (training_id) REFERENCES shapyfy.trainings (training_id)
);

CREATE TABLE shapyfy.session_parts
(
    session_part_id UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name            VARCHAR(256),
    type            varchar(32) NOT NULL, --is_off should be also included
    state           varchar(32) NOT NULL,
    existence_type  varchar(32) NOT NULL DEFAULT 'CONSTANT',
    session_date    timestamp,
    created_at       timestamp   NOT NULL default now(),
    updated_at       timestamp   NOT NULL default now(),
    --
    session_id      UUID        NOT NULL,
    CONSTRAINT fk_sessions FOREIGN KEY (session_id) REFERENCES shapyfy.sessions (session_id)
);

CREATE TABLE shapyfy.session_exercises
(
    session_exercise_id UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    is_finished         BOOLEAN   NOT NULL,
    rest_between_sets   INT       NOT NULL,
    note                TEXT,
    created_at           timestamp NOT NULL default now(),
    updated_at           timestamp NOT NULL default now(),
    --
    session_part_id     UUID      NOT NULL,
    exercise_id         UUID      NOT NULL,
    CONSTRAINT fk_session_part FOREIGN KEY (session_part_id) REFERENCES shapyfy.session_parts (session_part_id),
    CONSTRAINT fk_exercise FOREIGN KEY (exercise_id) REFERENCES shapyfy.exercises (exercise_id)
);

CREATE TABLE shapyfy.session_exercise_sets
(
    set_id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    reps_amount         INT     NOT NULL,
    weight_amount       DECIMAL,
    is_finished         BOOLEAN NOT NULL default false,
    --
    session_exercise_id UUID    NOT NULL,
    CONSTRAINT fk_session_exercise FOREIGN KEY (session_exercise_id) REFERENCES shapyfy.session_exercises (session_exercise_id)
);

CREATE TABLE shapyfy.session_exercise_additional_attributes
(
    attribute_id        uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name                varchar(256),
    value               varchar(256),
    --
    session_exercise_id UUID NOT NULL,
    CONSTRAINT fk_session_exercise FOREIGN KEY (session_exercise_id) REFERENCES shapyfy.session_exercises (session_exercise_id)
);

CREATE TABLE shapyfy.session_exercise_set_additional_attributes
(
    attribute_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         varchar(256),
    value        varchar(256),
    ---
    set_id       uuid NOT NULL,
    CONSTRAINT fk_session_exercise_set FOREIGN KEY (set_id) REFERENCES shapyfy.session_exercise_sets (set_id)
);