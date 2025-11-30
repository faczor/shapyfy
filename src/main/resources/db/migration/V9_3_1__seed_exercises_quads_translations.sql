-- Translations for QUADS exercises
-- Languages: EN, PL

-- 1. Barbell Squat (Back Squat)
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_squat', 'en', 'Barbell Squat', 'barbell_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_squat', 'pl', 'Przysiad ze Sztangą', 'przysiad_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Front Barbell Squat
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.front_barbell_squat', 'en', 'Front Squat', 'front_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.front_barbell_squat', 'pl', 'Przysiad Frontowy', 'przysiad_frontowy', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Goblet Squat
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.goblet_squat', 'en', 'Goblet Squat', 'goblet_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.goblet_squat', 'pl', 'Przysiad Kubkowy', 'przysiad_kubkowy', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Hack Squat
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hack_squat', 'en', 'Hack Squat', 'hack_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.hack_squat', 'pl', 'Hack Squat', 'hack_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Bulgarian Split Squat
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.split_squat_with_dumbbells', 'en', 'Bulgarian Split Squat', 'bulgarian_split_squat', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.split_squat_with_dumbbells', 'pl', 'Bułgarskie Przysiad', 'bulgarskie_przysiad', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Barbell Lunge
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_lunge', 'en', 'Barbell Lunge', 'barbell_lunge', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_lunge', 'pl', 'Wykroki ze Sztangą', 'wykroki_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Dumbbell Lunges
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_lunges', 'en', 'Dumbbell Lunges', 'dumbbell_lunges', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_lunges', 'pl', 'Wykroki z Hantlami', 'wykroki_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Leg Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.leg_press', 'en', 'Leg Press', 'leg_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.leg_press', 'pl', 'Prasa Nożna', 'prasa_nozna', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 9. Leg Extensions
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.leg_extensions', 'en', 'Leg Extensions', 'leg_extensions', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.leg_extensions', 'pl', 'Prostowanie Nóg', 'prostowanie_nog', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 10. Dumbbell Step Ups
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_step_ups', 'en', 'Dumbbell Step Ups', 'dumbbell_step_ups', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_step_ups', 'pl', 'Wspięcia na Ławkę z Hantlami', 'wspiecia_na_lawke_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
