-- Translations for GLUTES exercises
-- Languages: EN, PL

-- 1. Barbell Hip Thrust
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_hip_thrust', 'en', 'Barbell Hip Thrust', 'barbell_hip_thrust', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_hip_thrust', 'pl', 'Hip Thrust ze Sztangą', 'hip_thrust_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Barbell Glute Bridge
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_glute_bridge', 'en', 'Barbell Glute Bridge', 'barbell_glute_bridge', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_glute_bridge', 'pl', 'Mostek Pośladkowy ze Sztangą', 'mostek_posladkowy_ze_sztanga', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Glute Kickback
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.glute_kickback', 'en', 'Glute Kickback', 'glute_kickback', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.glute_kickback', 'pl', 'Odwodzenie Nogi na Wyciągu', 'odwodzenie_nogi_na_wyciagu', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Cable Pull Through
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pull_through', 'en', 'Cable Pull Through', 'cable_pull_through', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.pull_through', 'pl', 'Przeciąganie Linki między Nogami', 'przeciaganie_linki_miedzy_nogami', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Hip Abductor Machine
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.thigh_abductor', 'en', 'Hip Abductor Machine', 'hip_abductor_machine', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.thigh_abductor', 'pl', 'Odwodziciel', 'odwodziciel', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Single Leg Glute Bridge
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.single_leg_glute_bridge', 'en', 'Single Leg Glute Bridge', 'single_leg_glute_bridge', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.single_leg_glute_bridge', 'pl', 'Mostek Pośladkowy Jednożonóż', 'mostek_posladkowy_jednonoz', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
