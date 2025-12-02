-- Seed exercise library: BICEPS
-- Total exercises: 8 (most popular)

-- 1. Barbell Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000001', 'exercises.barbell_curl', 'BICEPS', '{"FOREARMS"}', '{"BARBELL"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Dumbbell Bicep Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000002', 'exercises.dumbbell_bicep_curl', 'BICEPS', '{"FOREARMS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Hammer Curls
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000003', 'exercises.hammer_curls', 'BICEPS', '{"FOREARMS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Preacher Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000004', 'exercises.preacher_curl', 'BICEPS', '{}', '{"EZ_BAR"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. EZ-Bar Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000005', 'exercises.ez_bar_curl', 'BICEPS', '{"FOREARMS"}', '{"EZ_BAR"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Concentration Curls
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000006', 'exercises.concentration_curls', 'BICEPS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Cable Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000007', 'exercises.standing_biceps_cable_curl', 'BICEPS', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Incline Dumbbell Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('a7b8c9d0-0007-4007-8007-000000000008', 'exercises.incline_dumbbell_curl', 'BICEPS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
