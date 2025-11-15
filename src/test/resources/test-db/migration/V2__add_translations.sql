CREATE TABLE IF NOT EXISTS translations
(
    id                BIGSERIAL PRIMARY KEY,
    translation_key   VARCHAR(255) NOT NULL,
    language          VARCHAR(5)   NOT NULL,
    value             TEXT         NOT NULL,
    category          VARCHAR(50)  NOT NULL,
    normalized_value  VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_translation_key_language UNIQUE (translation_key, language),
    CONSTRAINT uk_category_normalized UNIQUE (category, normalized_value)
);

CREATE INDEX IF NOT EXISTS idx_translations_category_language
    ON translations (category, language);

CREATE INDEX IF NOT EXISTS idx_translations_category_normalized
    ON translations (category, normalized_value);
