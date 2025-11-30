-- 1. Barbell Shoulder Press (OHP)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.barbell_shoulder_press', 'SHOULDERS', '{"CHEST","TRICEPS"}', '{"BARBELL"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Dumbbell Shoulder Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.dumbbell_shoulder_press', 'SHOULDERS', '{"TRICEPS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Seated Dumbbell Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.seated_dumbbell_press', 'SHOULDERS', '{"TRICEPS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Arnold Dumbbell Press
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.arnold_dumbbell_press', 'SHOULDERS', '{"TRICEPS"}', '{"DUMBBELLS"}',
        'BEGINNER', 'PUSH', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Side Lateral Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.side_lateral_raise', 'SHOULDERS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Cable Seated Lateral Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.cable_seated_lateral_raise', 'SHOULDERS', '{"BACK"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Face Pull
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.face_pull', 'SHOULDERS', '{"BACK"}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'PULL', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Cable Rear Delt Fly
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.cable_rear_delt_fly', 'SHOULDERS', '{}', '{"CABLE_MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 9. Seated Bent-Over Rear Delt Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.seated_bent_over_rear_delt_raise', 'SHOULDERS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 10. Front Dumbbell Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.front_dumbbell_raise', 'SHOULDERS', '{}', '{"DUMBBELLS"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;
