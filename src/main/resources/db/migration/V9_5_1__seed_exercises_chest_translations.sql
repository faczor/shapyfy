-- Translations for CHEST exercises
-- Languages: EN, PL

-- 1. Barbell Bench Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_bench_press', 'en', 'Barbell Bench Press', 'barbell_bench_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_bench_press', 'pl', 'Wyciskanie Sztangi na Ławce', 'wyciskanie_sztangi_na_lawce', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Dumbbell Bench Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_bench_press', 'en', 'Dumbbell Bench Press', 'dumbbell_bench_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_bench_press', 'pl', 'Wyciskanie Hantli na Ławce', 'wyciskanie_hantli_na_lawce', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Incline Barbell Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_barbell_press', 'en', 'Incline Barbell Press', 'incline_barbell_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_barbell_press', 'pl', 'Wyciskanie Sztangi na Skosie', 'wyciskanie_sztangi_na_skosie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Incline Dumbbell Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_dumbbell_press', 'en', 'Incline Dumbbell Press', 'incline_dumbbell_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.incline_dumbbell_press', 'pl', 'Wyciskanie Hantli na Skosie', 'wyciskanie_hantli_na_skosie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Decline Barbell Bench Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.decline_barbell_bench_press', 'en', 'Decline Barbell Bench Press', 'decline_barbell_bench_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.decline_barbell_bench_press', 'pl', 'Wyciskanie Sztangi na Ławce Spadowej', 'wyciskanie_sztangi_na_lawce_spadowej', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Dumbbell Flyes
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_flyes', 'en', 'Dumbbell Flyes', 'dumbbell_flyes', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_flyes', 'pl', 'Rozpiętki z Hantlami', 'rozpietki_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Cable Crossover
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_crossover', 'en', 'Cable Crossover', 'cable_crossover', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_crossover', 'pl', 'Krzyżowanie Linek', 'krzyzowanie_linek', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Push-ups
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pushups', 'en', 'Push-ups', 'push_ups', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pushups', 'pl', 'Pompki', 'pompki', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 9. Dips (Chest Version)
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dips_chest', 'en', 'Dips (Chest)', 'dips_chest', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dips_chest', 'pl', 'Pompki na Poręczach', 'pompki_na_poreczach', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 10. Butterfly (Pec Deck)
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.butterfly', 'en', 'Butterfly', 'butterfly', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.butterfly', 'pl', 'Rozpiętki na Maszynie', 'rozpietki_na_maszynie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
