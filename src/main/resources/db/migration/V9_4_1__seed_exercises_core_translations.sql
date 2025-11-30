-- Translations for CORE exercises
-- Languages: EN, PL

-- 1. Plank
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.plank', 'en', 'Plank', 'plank', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.plank', 'pl', 'Deska', 'deska', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Crunches
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.crunches', 'en', 'Crunches', 'crunches', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.crunches', 'pl', 'Brzuszki', 'brzuszki', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Sit-Up
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.sit_up', 'en', 'Sit-Up', 'sit_up', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.sit_up', 'pl', 'Siad z Leżenia', 'siad_z_lezenia', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Cable Crunch
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_crunch', 'en', 'Cable Crunch', 'cable_crunch', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_crunch', 'pl', 'Brzuszki na Wyciągu', 'brzuszki_na_wyciagu', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Rope Crunch (Allachy)
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.rope_crunch', 'en', 'Rope Crunch', 'rope_crunch', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.rope_crunch', 'pl', 'Allachy', 'allachy', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Hanging Leg Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hanging_leg_raise', 'en', 'Hanging Leg Raise', 'hanging_leg_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hanging_leg_raise', 'pl', 'Unoszenie Nóg w Zwisie', 'unoszenie_nog_w_zwisie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Reverse Crunch
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.reverse_crunch', 'en', 'Reverse Crunch', 'reverse_crunch', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.reverse_crunch', 'pl', 'Brzuszki Odwrotne', 'brzuszki_odwrotne', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Russian Twist
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.russian_twist', 'en', 'Russian Twist', 'russian_twist', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.russian_twist', 'pl', 'Skręty Rosyjskie', 'skrety_rosyjskie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 9. Pallof Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pallof_press', 'en', 'Pallof Press', 'pallof_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pallof_press', 'pl', 'Pallof Press', 'pallof_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 10. Ab Roller
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ab_roller', 'en', 'Ab Roller', 'ab_roller', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ab_roller', 'pl', 'Rollout z Kółkiem', 'rollout_z_kolkiem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
