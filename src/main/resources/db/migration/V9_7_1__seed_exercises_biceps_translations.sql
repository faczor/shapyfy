-- Translations for BICEPS exercises
-- Languages: EN, PL

-- 1. Barbell Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_curl', 'en', 'Barbell Curl', 'barbell_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_curl', 'pl', 'Uginanie Ramion ze Sztangą', 'uginanie_ramion_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Dumbbell Bicep Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_bicep_curl', 'en', 'Dumbbell Bicep Curl', 'dumbbell_bicep_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_bicep_curl', 'pl', 'Uginanie Ramion z Hantlami', 'uginanie_ramion_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Hammer Curls
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hammer_curls', 'en', 'Hammer Curls', 'hammer_curls', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hammer_curls', 'pl', 'Uginanie Młotkowe', 'uginanie_mlotkowe', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Preacher Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.preacher_curl', 'en', 'Preacher Curl', 'preacher_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.preacher_curl', 'pl', 'Modlitewnik', 'modlitewnik', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. EZ-Bar Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ez_bar_curl', 'en', 'EZ-Bar Curl', 'ez_bar_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ez_bar_curl', 'pl', 'Uginanie Ramion Gryf Łamany', 'uginanie_ramion_gryf_lamany', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Concentration Curls
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.concentration_curls', 'en', 'Concentration Curls', 'concentration_curls', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.concentration_curls', 'pl', 'Uginanie Koncentryczne', 'uginanie_koncentryczne', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Cable Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_biceps_cable_curl', 'en', 'Cable Curl', 'cable_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_biceps_cable_curl', 'pl', 'Uginanie na Wyciągu', 'uginanie_na_wyciagu', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Incline Dumbbell Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_dumbbell_curl', 'en', 'Incline Dumbbell Curl', 'incline_dumbbell_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_dumbbell_curl', 'pl', 'Uginanie na Ławce Skośnej', 'uginanie_na_lawce_skosnej', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
