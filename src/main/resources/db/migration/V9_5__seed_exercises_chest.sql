-- Seed exercise library: CHEST
-- Total exercises: 10 (most popular)

-- 1. Barbell Bench Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000001', 'exercises.barbell_bench_press', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"BARBELL"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Dumbbell Bench Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000002', 'exercises.dumbbell_bench_press', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Incline Barbell Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000003', 'exercises.incline_barbell_press', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"BARBELL"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Incline Dumbbell Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000004', 'exercises.incline_dumbbell_press', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Decline Barbell Bench Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000005', 'exercises.decline_barbell_bench_press', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"BARBELL"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Dumbbell Flyes
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000006', 'exercises.dumbbell_flyes', 'CHEST', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Cable Crossover
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000007', 'exercises.cable_crossover', 'CHEST', '{"SHOULDERS"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Push-ups
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000008', 'exercises.pushups', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"BODYWEIGHT"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 9. Dips (Chest Version)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000009', 'exercises.dips_chest', 'CHEST', '{"SHOULDERS","TRICEPS"}', '{"BODYWEIGHT"}',
        'ADVANCED', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 10. Butterfly (Pec Deck)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e5f6a7b8-0005-4005-8005-000000000010', 'exercises.butterfly', 'CHEST', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
