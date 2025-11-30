-- Translations for HAMSTRINGS exercises
-- Languages: EN, PL

-- 1. Lying Leg Curls
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.lying_leg_curls', 'en', 'Lying Leg Curls', 'lying_leg_curls', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.lying_leg_curls', 'pl', 'Uginanie Nóg Leżąc', 'uginanie_nog_lezac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Seated Leg Curl
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_leg_curl', 'en', 'Seated Leg Curl', 'seated_leg_curl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_leg_curl', 'pl', 'Uginanie Nóg Siedząc', 'uginanie_nog_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Romanian Deadlift
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.romanian_deadlift', 'en', 'Romanian Deadlift', 'romanian_deadlift', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.romanian_deadlift', 'pl', 'Martwy Ciąg Rumuński', 'martwy_ciag_rumunski', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Barbell Deadlift
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_deadlift', 'en', 'Barbell Deadlift', 'barbell_deadlift', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_deadlift', 'pl', 'Martwy Ciąg', 'martwy_ciag', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Stiff-Legged Barbell Deadlift
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.stiff_legged_barbell_deadlift', 'en', 'Stiff-Legged Deadlift', 'stiff_legged_deadlift', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.stiff_legged_barbell_deadlift', 'pl', 'Martwy Ciąg na Prostych Nogach', 'martwy_ciag_na_prostych_nogach', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Good Morning
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.good_morning', 'en', 'Good Morning', 'good_morning', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.good_morning', 'pl', 'Good Morning', 'good_morning', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Glute Ham Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.glute_ham_raise', 'en', 'Glute Ham Raise', 'glute_ham_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.glute_ham_raise', 'pl', 'GHR', 'ghr', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Sumo Deadlift
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.sumo_deadlift', 'en', 'Sumo Deadlift', 'sumo_deadlift', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.sumo_deadlift', 'pl', 'Martwy Ciąg Sumo', 'martwy_ciag_sumo', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
