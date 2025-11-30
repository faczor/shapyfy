-- Translations for SHOULDERS exercises
-- Languages: EN, PL

-- 1. Barbell Shoulder Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_shoulder_press', 'en', 'Barbell Shoulder Press', 'barbell_shoulder_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.barbell_shoulder_press', 'pl', 'Wyciskanie Sztangi nad Głowę', 'wyciskanie_sztangi_nad_glowe', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 2. Dumbbell Shoulder Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_shoulder_press', 'en', 'Dumbbell Shoulder Press', 'dumbbell_shoulder_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.dumbbell_shoulder_press', 'pl', 'Wyciskanie Hantli nad Głowę', 'wyciskanie_hantli_nad_glowe', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 3. Seated Dumbbell Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_dumbbell_press', 'en', 'Seated Dumbbell Press', 'seated_dumbbell_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_dumbbell_press', 'pl', 'Wyciskanie Hantli Siedząc', 'wyciskanie_hantli_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 4. Arnold Dumbbell Press
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.arnold_dumbbell_press', 'en', 'Arnold Dumbbell Press', 'arnold_dumbbell_press', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.arnold_dumbbell_press', 'pl', 'Wyciskanie Arnold', 'wyciskanie_arnold', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 5. Side Lateral Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.side_lateral_raise', 'en', 'Side Lateral Raise', 'side_lateral_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.side_lateral_raise', 'pl', 'Unoszenie Hantli Bokiem', 'unoszenie_hantli_bokiem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 6. Cable Seated Lateral Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_seated_lateral_raise', 'en', 'Cable Seated Lateral Raise', 'cable_seated_lateral_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_seated_lateral_raise', 'pl', 'Unoszenie na Wyciągu Siedząc', 'unoszenie_na_wyciagu_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 7. Face Pull
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.face_pull', 'en', 'Face Pull', 'face_pull', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.face_pull', 'pl', 'Face Pull', 'face_pull', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 8. Cable Rear Delt Fly
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_rear_delt_fly', 'en', 'Cable Rear Delt Fly', 'cable_rear_delt_fly', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.cable_rear_delt_fly', 'pl', 'Rozpiętki na Wyciągu na Tylny Bark', 'rozpietki_na_wyciagu_na_tylny_bark', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 9. Seated Bent-Over Rear Delt Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_bent_over_rear_delt_raise', 'en', 'Seated Bent-Over Rear Delt Raise', 'seated_bent_over_rear_delt_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.seated_bent_over_rear_delt_raise', 'pl', 'Unoszenie na Tylny Bark w Opadzie Siedząc', 'unoszenie_na_tylny_bark_w_opadzie_siedzac', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

-- 10. Front Dumbbell Raise
INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.front_dumbbell_raise', 'en', 'Front Dumbbell Raise', 'front_dumbbell_raise', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;

INSERT INTO translations (translation_key, language, value, normalized_value, created_at)
VALUES ('exercises.front_dumbbell_raise', 'pl', 'Unoszenie Hantli Przodem', 'unoszenie_hantli_przodem', NOW())
ON CONFLICT (translation_key, language) DO NOTHING;
