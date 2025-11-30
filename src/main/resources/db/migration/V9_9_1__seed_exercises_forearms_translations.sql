-- Translations for FOREARMS exercises
-- Languages: EN, PL

-- 1. Wrist Roller
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.wrist_roller', 'en', 'Wrist Roller', 'wrist_roller', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.wrist_roller', 'pl', 'Roller Nadgarstkowy', 'roller_nadgarstkowy', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Palms-Up Wrist Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.palms_up_wrist_curl', 'en', 'Palms-Up Wrist Curl', 'palms_up_wrist_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.palms_up_wrist_curl', 'pl', 'Uginanie Nadgarstków Nachwytem', 'uginanie_nadgarstkow_nachwytem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Palms-Down Wrist Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.palms_down_wrist_curl', 'en', 'Palms-Down Wrist Curl', 'palms_down_wrist_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.palms_down_wrist_curl', 'pl', 'Uginanie Nadgarstków Podchwytem', 'uginanie_nadgarstkow_podchwytem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Farmer's Walk
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.farmers_walk', 'en', 'Farmer''s Walk', 'farmers_walk', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.farmers_walk', 'pl', 'Spacer Farmera', 'spacer_farmera', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Finger Curls
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.finger_curls', 'en', 'Finger Curls', 'finger_curls', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.finger_curls', 'pl', 'Zginanie Palców', 'zginanie_palcow', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
