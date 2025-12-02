-- Seed exercise library: FOREARMS
-- Total exercises: 5 (most popular)

-- 1. Wrist Roller
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('c9d0e1f2-0009-4009-8009-000000000001', 'exercises.wrist_roller', 'FOREARMS', '{}', '{"OTHER"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Palms-Up Wrist Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('c9d0e1f2-0009-4009-8009-000000000002', 'exercises.palms_up_wrist_curl', 'FOREARMS', '{}', '{"BARBELL"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Palms-Down Wrist Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('c9d0e1f2-0009-4009-8009-000000000003', 'exercises.palms_down_wrist_curl', 'FOREARMS', '{}', '{"BARBELL"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Farmer's Walk
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('c9d0e1f2-0009-4009-8009-000000000004', 'exercises.farmers_walk', 'FOREARMS', '{"CORE","SHOULDERS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Finger Curls
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('c9d0e1f2-0009-4009-8009-000000000005', 'exercises.finger_curls', 'FOREARMS', '{}', '{"BARBELL"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
