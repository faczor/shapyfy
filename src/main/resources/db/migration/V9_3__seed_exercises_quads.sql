-- Seed exercise library: QUADS
-- Total exercises: 10 (most popular)

-- 1. Barbell Squat (Back Squat)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.barbell_squat', 'QUADS', '{"BACK","CALVES","GLUTES","HAMSTRINGS"}', '{"BARBELL"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Front Barbell Squat
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.front_barbell_squat', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"BARBELL"}',
        'ADVANCED', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Goblet Squat
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.goblet_squat', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS","SHOULDERS"}', '{"KETTLEBELL"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Hack Squat
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.hack_squat', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"MACHINE"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Bulgarian Split Squat (Split Squat with Dumbbells)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.split_squat_with_dumbbells', 'QUADS', '{"GLUTES","HAMSTRINGS"}', '{"DUMBBELLS"}',
        'ADVANCED', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Barbell Lunge
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.barbell_lunge', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"BARBELL"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Dumbbell Lunges
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.dumbbell_lunges', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Leg Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.leg_press', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"MACHINE"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 9. Leg Extensions
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.leg_extensions', 'QUADS', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 10. Dumbbell Step Ups
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.dumbbell_step_ups', 'QUADS', '{"CALVES","GLUTES","HAMSTRINGS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'SQUAT', NOW())
ON CONFLICT (translation_key) DO NOTHING;
