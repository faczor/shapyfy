CREATE TABLE IF NOT EXISTS translations
(
    id                BIGSERIAL PRIMARY KEY,
    translation_key   VARCHAR(255) NOT NULL,
    language          VARCHAR(5)   NOT NULL,
    value             TEXT         NOT NULL,
    normalized_value  VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_translation_key_language UNIQUE (translation_key, language)
);

CREATE INDEX IF NOT EXISTS idx_translations_normalized_value
    ON translations (normalized_value);

CREATE INDEX IF NOT EXISTS idx_translations_language
    ON translations (language);
