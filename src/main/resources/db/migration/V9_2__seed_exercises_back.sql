-- Seed exercise library: BACK
-- Total exercises: 9 (most popular)

-- 1. Pull-ups
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000001', 'exercises.pullups', 'BACK', '{"BICEPS"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Chin-Up
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000002', 'exercises.chin_up', 'BACK', '{"BICEPS","FOREARMS"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Wide-Grip Lat Pulldown
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000003', 'exercises.wide_grip_lat_pulldown', 'BACK', '{"BICEPS","SHOULDERS"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Bent Over Barbell Row
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000004', 'exercises.bent_over_barbell_row', 'BACK', '{"BICEPS","SHOULDERS"}', '{"BARBELL"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. One-Arm Dumbbell Row
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000005', 'exercises.one_arm_dumbbell_row', 'BACK', '{"BICEPS","SHOULDERS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Seated Cable Rows
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000006', 'exercises.seated_cable_rows', 'BACK', '{"BICEPS","SHOULDERS"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. T-Bar Row with Handle
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000007', 'exercises.t_bar_row_with_handle', 'BACK', '{"BICEPS"}', '{"BARBELL"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Barbell Shrug
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000008', 'exercises.barbell_shrug', 'BACK', '{}', '{"BARBELL"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 9. Dumbbell Shrug
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('b2c3d4e5-0002-4002-8002-000000000009', 'exercises.dumbbell_shrug', 'BACK', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
