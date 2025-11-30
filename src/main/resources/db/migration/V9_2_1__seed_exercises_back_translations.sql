-- Translations for BACK exercises
-- Languages: EN, PL

-- 1. Pull-ups
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pullups', 'en', 'Pull-ups', 'pull_ups', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pullups', 'pl', 'Podciąganie na Drążku', 'podciaganie_na_drazku', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Chin-Up
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.chin_up', 'en', 'Chin-Up', 'chin_up', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.chin_up', 'pl', 'Podciąganie Podchwytem', 'podciaganie_podchwytem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Wide-Grip Lat Pulldown
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.wide_grip_lat_pulldown', 'en', 'Wide-Grip Lat Pulldown', 'wide_grip_lat_pulldown', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.wide_grip_lat_pulldown', 'pl', 'Ściąganie Drążka Szerokim Chwytem', 'sciaganie_drazka_szerokim_chwytem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Bent Over Barbell Row
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.bent_over_barbell_row', 'en', 'Bent Over Barbell Row', 'bent_over_barbell_row', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.bent_over_barbell_row', 'pl', 'Wiosłowanie Sztangą w Opadzie', 'wioslowanie_sztanga_w_opadzie', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. One-Arm Dumbbell Row
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.one_arm_dumbbell_row', 'en', 'One-Arm Dumbbell Row', 'one_arm_dumbbell_row', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.one_arm_dumbbell_row', 'pl', 'Wiosłowanie Hantlą Jednorącz', 'wioslowanie_hantla_jednoracze', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Seated Cable Rows
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_cable_rows', 'en', 'Seated Cable Rows', 'seated_cable_rows', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_cable_rows', 'pl', 'Wiosłowanie na Wyciągu Siedząc', 'wioslowanie_na_wyciagu_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. T-Bar Row with Handle
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.t_bar_row_with_handle', 'en', 'T-Bar Row', 't_bar_row', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.t_bar_row_with_handle', 'pl', 'Wiosłowanie T-Bar', 'wioslowanie_t_bar', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Barbell Shrug
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_shrug', 'en', 'Barbell Shrug', 'barbell_shrug', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_shrug', 'pl', 'Wznosy Barków ze Sztangą', 'wznosy_barkow_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 9. Dumbbell Shrug
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_shrug', 'en', 'Dumbbell Shrug', 'dumbbell_shrug', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_shrug', 'pl', 'Wznosy Barków z Hantlami', 'wznosy_barkow_z_hantlami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
