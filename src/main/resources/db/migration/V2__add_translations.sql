-- Create translations table
CREATE TABLE translations
(
    id              BIGSERIAL PRIMARY KEY,
    translation_key VARCHAR(255) NOT NULL,
    language        VARCHAR(2)   NOT NULL,
    value           TEXT         NOT NULL,
    category        VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_translation_key_language UNIQUE (translation_key, language)
);

CREATE INDEX idx_translation_key_lang ON translations (translation_key, language);
CREATE INDEX idx_category ON translations (category);

-- Migrate existing exercise names to translations
-- For each existing exercise, create English translation using the current name
INSERT INTO translations (translation_key, language, value, category)
SELECT
    'exercises.' || LOWER(REGEXP_REPLACE(name, '[^a-zA-Z0-9]+', '_', 'g')) AS translation_key,
    'en' AS language,
    name AS value,
    'exercises' AS category
FROM exercises
WHERE name IS NOT NULL;

-- Update exercises table: rename 'name' column to 'translation_key'
ALTER TABLE exercises
    RENAME COLUMN name TO translation_key;

-- Update exercises.translation_key to use normalized keys
UPDATE exercises
SET translation_key = 'exercises.' || LOWER(REGEXP_REPLACE(translation_key, '[^a-zA-Z0-9]+', '_', 'g'));

-- Add unique constraint on translation_key
ALTER TABLE exercises
    ADD CONSTRAINT uk_exercises_translation_key UNIQUE (translation_key);

-- Add NOT NULL constraint
ALTER TABLE exercises
    ALTER COLUMN translation_key SET NOT NULL;
