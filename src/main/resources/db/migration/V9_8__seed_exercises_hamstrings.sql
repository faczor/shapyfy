-- Seed exercise library: HAMSTRINGS
-- Total exercises: 8 (most popular)

-- 1. Lying Leg Curls
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.lying_leg_curls', 'HAMSTRINGS', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 2. Seated Leg Curl
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.seated_leg_curl', 'HAMSTRINGS', '{}', '{"MACHINE"}',
        'BEGINNER', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 3. Romanian Deadlift
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.romanian_deadlift', 'HAMSTRINGS', '{"BACK","GLUTES"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 4. Barbell Deadlift (moved from Back)
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.barbell_deadlift', 'HAMSTRINGS', '{"BACK","FOREARMS","GLUTES","QUADS"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 5. Stiff-Legged Barbell Deadlift
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.stiff_legged_barbell_deadlift', 'HAMSTRINGS', '{"BACK","GLUTES"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 6. Good Morning
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.good_morning', 'HAMSTRINGS', '{"BACK","GLUTES"}', '{"BARBELL"}',
        'ADVANCED', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 7. Glute Ham Raise
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.glute_ham_raise', 'HAMSTRINGS', '{"GLUTES"}', '{"MACHINE"}',
        'ADVANCED', 'ISOLATION', NOW())
ON CONFLICT (translation_key) DO NOTHING;

-- 8. Sumo Deadlift
INSERT INTO exercises (id, translation_key, primary_muscle_group, secondary_muscle_groups, equipment_required,
                       difficulty, movement_pattern, created_at)
VALUES (gen_random_uuid(), 'exercises.sumo_deadlift', 'HAMSTRINGS', '{"BACK","GLUTES","QUADS"}', '{"BARBELL"}',
        'BEGINNER', 'HINGE', NOW())
ON CONFLICT (translation_key) DO NOTHING;
