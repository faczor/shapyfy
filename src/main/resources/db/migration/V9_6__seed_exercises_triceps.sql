-- Seed exercise library: TRICEPS
-- Total exercises: 8 (most popular)

-- 1. Triceps Pushdown
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.triceps_pushdown', 'TRICEPS', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Rope Pushdown
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.triceps_pushdown_rope', 'TRICEPS', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Close-Grip Bench Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.close_grip_barbell_bench_press', 'TRICEPS', '{"CHEST","SHOULDERS"}', '{"BARBELL"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Triceps Dips
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.dips_triceps', 'TRICEPS', '{"CHEST","SHOULDERS"}', '{"BODYWEIGHT"}',
        'ADVANCED', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. EZ-Bar Skullcrusher
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.ez_bar_skullcrusher', 'TRICEPS', '{}', '{"EZ_BAR"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Overhead Triceps Extension
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.overhead_triceps_extension', 'TRICEPS', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Tricep Dumbbell Kickback
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.tricep_dumbbell_kickback', 'TRICEPS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Bench Dips
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.bench_dips', 'TRICEPS', '{"CHEST","SHOULDERS"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;
