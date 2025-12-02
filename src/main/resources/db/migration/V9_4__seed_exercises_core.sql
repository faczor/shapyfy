-- Seed exercise library: CORE
-- Total exercises: 10 (most popular)

-- 1. Plank
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000001', 'exercises.plank', 'CORE', '{}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Crunches
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000002', 'exercises.crunches', 'CORE', '{}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Sit-Up
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000003', 'exercises.sit_up', 'CORE', '{}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Cable Crunch
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000004', 'exercises.cable_crunch', 'CORE', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Rope Crunch (Allachy)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000005', 'exercises.rope_crunch', 'CORE', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Hanging Leg Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000006', 'exercises.hanging_leg_raise', 'CORE', '{}', '{"BODYWEIGHT"}',
        'ADVANCED', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Reverse Crunch
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000007', 'exercises.reverse_crunch', 'CORE', '{}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Russian Twist
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000008', 'exercises.russian_twist', 'CORE', '{"BACK"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 9. Pallof Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000009', 'exercises.pallof_press', 'CORE', '{"CHEST","SHOULDERS","TRICEPS"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 10. Ab Roller
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d4e5f6a7-0004-4004-8004-000000000010', 'exercises.ab_roller', 'CORE', '{"SHOULDERS"}', '{"BODYWEIGHT"}',
        'ADVANCED', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
