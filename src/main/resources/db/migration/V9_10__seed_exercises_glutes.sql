-- Seed exercise library: GLUTES
-- Total exercises: 6 (most popular)

-- 1. Barbell Hip Thrust
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000001', 'exercises.barbell_hip_thrust', 'GLUTES', '{"HAMSTRINGS"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Barbell Glute Bridge
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000002', 'exercises.barbell_glute_bridge', 'GLUTES', '{"HAMSTRINGS"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Glute Kickback
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000003', 'exercises.glute_kickback', 'GLUTES', '{"HAMSTRINGS"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Cable Pull Through
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000004', 'exercises.pull_through', 'GLUTES', '{"HAMSTRINGS","BACK"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Hip Abductor Machine
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000005', 'exercises.thigh_abductor', 'GLUTES', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Single Leg Glute Bridge
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('d0e1f2a3-0010-4010-8010-000000000006', 'exercises.single_leg_glute_bridge', 'GLUTES', '{"HAMSTRINGS"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
