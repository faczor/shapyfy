-- Translations for TRICEPS exercises
-- Languages: EN, PL

-- 1. Triceps Pushdown
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.triceps_pushdown', 'en', 'Triceps Pushdown', 'triceps_pushdown', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.triceps_pushdown', 'pl', 'Prostowanie Ramion na Wyciągu', 'prostowanie_ramion_na_wyciagu', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Rope Pushdown
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.triceps_pushdown_rope', 'en', 'Triceps Rope Pushdown', 'triceps_rope_pushdown', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.triceps_pushdown_rope', 'pl', 'Prostowanie Ramion na Linie', 'prostowanie_ramion_na_linie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Close-Grip Bench Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.close_grip_barbell_bench_press', 'en', 'Close-Grip Bench Press', 'close_grip_bench_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.close_grip_barbell_bench_press', 'pl', 'Wyciskanie Wąskim Chwytem', 'wyciskanie_waskim_chwytem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Triceps Dips
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dips_triceps', 'en', 'Dips (Triceps)', 'dips_triceps', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dips_triceps', 'pl', 'Pompki na Poręczach (Triceps)', 'pompki_na_poreczach_triceps', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. EZ-Bar Skullcrusher
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ez_bar_skullcrusher', 'en', 'EZ-Bar Skullcrusher', 'ez_bar_skullcrusher', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.ez_bar_skullcrusher', 'pl', 'Francuskie Wyciskanie', 'francuskie_wyciskanie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Overhead Triceps Extension
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.overhead_triceps_extension', 'en', 'Overhead Triceps Extension', 'overhead_triceps_extension', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.overhead_triceps_extension', 'pl', 'Prostowanie Ramion nad Głową', 'prostowanie_ramion_nad_glowa', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Tricep Dumbbell Kickback
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.tricep_dumbbell_kickback', 'en', 'Tricep Kickback', 'tricep_kickback', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.tricep_dumbbell_kickback', 'pl', 'Odwodzenie Ramion w Tył', 'odwodzenie_ramion_w_tyl', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Bench Dips
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.bench_dips', 'en', 'Bench Dips', 'bench_dips', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.bench_dips', 'pl', 'Pompki na Ławce', 'pompki_na_lawce', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
