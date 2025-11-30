-- Translations for CALVES exercises
-- Languages: EN, PL

-- 1. Standing Calf Raises
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_calf_raises', 'en', 'Standing Calf Raises', 'standing_calf_raises', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_calf_raises', 'pl', 'Wspięcia na Palce Stojąc', 'wspiecia_na_palce_stojac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Seated Calf Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_calf_raise', 'en', 'Seated Calf Raise', 'seated_calf_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_calf_raise', 'pl', 'Wspięcia na Palce Siedząc', 'wspiecia_na_palce_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Calf Press on Leg Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.calf_press_on_leg_press', 'en', 'Calf Press on Leg Press', 'calf_press_on_leg_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.calf_press_on_leg_press', 'pl', 'Wspięcia na Suwnicy', 'wspiecia_na_suwnicy', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Donkey Calf Raises
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.donkey_calf_raises', 'en', 'Donkey Calf Raises', 'donkey_calf_raises', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.donkey_calf_raises', 'pl', 'Osiołki', 'osiolki', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Standing Dumbbell Calf Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_dumbbell_calf_raise', 'en', 'Standing Dumbbell Calf Raise', 'standing_dumbbell_calf_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.standing_dumbbell_calf_raise', 'pl', 'Wspięcia z Hantlami', 'wspiecia_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
