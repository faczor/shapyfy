-- Seed exercise library: CALVES
-- Total exercises: 5 (most popular)

-- 1. Standing Calf Raises (Machine)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e1f2a3b4-0011-4011-8011-000000000001', 'exercises.standing_calf_raises', 'CALVES', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Seated Calf Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e1f2a3b4-0011-4011-8011-000000000002', 'exercises.seated_calf_raise', 'CALVES', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Calf Press on Leg Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e1f2a3b4-0011-4011-8011-000000000003', 'exercises.calf_press_on_leg_press', 'CALVES', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Donkey Calf Raises
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e1f2a3b4-0011-4011-8011-000000000004', 'exercises.donkey_calf_raises', 'CALVES', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Standing Dumbbell Calf Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES ('e1f2a3b4-0011-4011-8011-000000000005', 'exercises.standing_dumbbell_calf_raise', 'CALVES', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
