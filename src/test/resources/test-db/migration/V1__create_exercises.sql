CREATE TABLE IF NOT EXISTS exercises
(
    id              UUID PRIMARY KEY,
    translation_key VARCHAR(255) NOT NULL UNIQUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    CONSTRAINT uk_exercises_translation_key UNIQUE (translation_key)
);
