
CREATE TABLE training_plans
(
    id        uuid PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    status    VARCHAR(32)  NOT NULL,
    start_date DATE         NOT NULL,
    user_id   VARCHAR(36)  NOT NULL
);

CREATE TABLE plan_days
(
    id               uuid PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    type             VARCHAR(10)  NOT NULL,
    training_plan_id uuid  NOT NULL REFERENCES training_plans (id)
);

CREATE TABLE exercises
(
    id           uuid PRIMARY KEY,
    name         VARCHAR(255) NOT NULL
);

CREATE TABLE workout_exercise_configs
(
    id          uuid PRIMARY KEY,
    exercise_id uuid     NOT NULL REFERENCES exercises (id),
    sets        INTEGER     NOT NULL,
    reps        INTEGER     NOT NULL,
    weight      DOUBLE PRECISION     NOT NULL,
    rest_time   INTEGER     NOT NULL,
    order_index     INTEGER     NOT NULL,
    plan_day_id uuid NOT NULL REFERENCES plan_days (id)
);

CREATE TABLE activity_logs
(
    id          uuid PRIMARY KEY,
    date        DATE        NOT NULL,
    type        VARCHAR(10) NOT NULL,
    plan_day_id uuid NOT NULL REFERENCES plan_days (id)
);


CREATE TABLE workout_sets
(
    id          uuid PRIMARY KEY,
    reps        INTEGER          NOT NULL,
    weight      DOUBLE PRECISION NOT NULL,
    order_index     INTEGER          NOT NULL,
    exercise_id uuid      NOT NULL REFERENCES exercises (id),
    activity_log_id uuid NOT NULL REFERENCES activity_logs (id)
);