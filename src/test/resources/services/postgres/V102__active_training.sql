INSERT INTO configurations(configuration_id, name)
VALUES ('00000000-0000-0000-0000-000000000002', 'PUSH PULL');

INSERT INTO configuration_parts(configuration_part_id, name, type, configuration_id)
VALUES ('00000000-0000-0000-0000-000000000201', 'PUSH', 'TRAINING_DAY', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0000-000000000202', 'ODPOCZYNEK 1', 'REST_DAY', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0000-000000000203', 'PULL', 'TRAINING_DAY', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0000-000000000204', 'ODPOCZYNEK 2', 'REST_DAY', '00000000-0000-0000-0000-000000000002');

INSERT INTO configuration_part_exercises(configuration_part_exercise_id, rest_between_sets, sets_amount, reps_amount, weight_amount, configuration_part_id, exercise_id)
VALUES ('00000000-0000-0000-0000-000000020101', 60, 3, 8, 40, '00000000-0000-0000-0000-000000000201', '00000000-0000-0000-0000-000000000001'),
       ('00000000-0000-0000-0000-000000020102', 90, 4, 6, 60, '00000000-0000-0000-0000-000000000201', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0000-000000020103', 120, 4, 6, 110.5, '00000000-0000-0000-0000-000000000201', '00000000-0000-0000-0000-000000000003'),

       ('00000000-0000-0000-0000-000000020301', 30, 2, 5, 10.5, '00000000-0000-0000-0000-000000000203', '00000000-0000-0000-0000-000000000004'),
       ('00000000-0000-0000-0000-000000020302', 50, 3, 8, 50, '00000000-0000-0000-0000-000000000203', '00000000-0000-0000-0000-000000000005');

INSERT INTO trainings (training_id, user_id, name, configuration_id)
VALUES ('00000000-0000-0000-0002-000000000201', '00000000000000002', 'PUSH PULL', '00000000-0000-0000-0000-000000000002');

INSERT INTO sessions(session_id, state, training_id)
VALUES ('00000000-0000-0000-0002-000000020101', 'ACTIVE', '00000000-0000-0000-0002-000000000201'),
       ('00000000-0000-0000-0002-000000020102', 'FOLLOW_UP', '00000000-0000-0000-0002-000000000201');

INSERT INTO session_parts(session_part_id, name, type, state, session_date, session_id)
VALUES ('00000000-0000-0000-0002-000002010101', 'PUSH', 'TRAINING_DAY', 'ACTIVE', '2023-01-01', '00000000-0000-0000-0002-000000020101'),
       ('00000000-0000-0000-0002-000002010102','ODPOCZYNEK 1', 'REST_DAY', 'ACTIVE', '2023-01-02', '00000000-0000-0000-0002-000000020101'),
       ('00000000-0000-0000-0002-000002010103','PULL', 'TRAINING_DAY', 'ACTIVE', '2023-01-03', '00000000-0000-0000-0002-000000020101'),
       ('00000000-0000-0000-0002-000002010104','ODPOCZYNEK 2', 'REST_DAY', 'ACTIVE', '2023-01-04', '00000000-0000-0000-0002-000000020101'),
       --
       ('00000000-0000-0000-0002-000002010201', 'PUSH', 'TRAINING_DAY', 'ACTIVE', '2023-01-05', '00000000-0000-0000-0002-000000020102'),
       ('00000000-0000-0000-0002-000002010202', 'ODPOCZYNEK 1', 'REST_DAY', 'ACTIVE', '2023-01-06', '00000000-0000-0000-0002-000000020102'),
       ('00000000-0000-0000-0002-000002010203', 'PULL', 'TRAINING_DAY', 'ACTIVE', '2023-01-07', '00000000-0000-0000-0002-000000020102'),
       ('00000000-0000-0000-0002-000002010204', 'ODPOCZYNEK 2', 'REST_DAY', 'ACTIVE', '2023-01-08', '00000000-0000-0000-0002-000000020102');

INSERT INTO session_exercises(session_exercise_id, is_finished, rest_between_sets, note, session_part_id, exercise_id)
VALUES ('00000000-0000-0000-0002-000201010101', false, 60, null, '00000000-0000-0000-0002-000002010101', '00000000-0000-0000-0000-000000000001'),
       ('00000000-0000-0000-0002-000201010102', false, 90, null, '00000000-0000-0000-0002-000002010101', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0002-000201010103', false, 120, null, '00000000-0000-0000-0002-000002010101', '00000000-0000-0000-0000-000000000003'),
       ('00000000-0000-0000-0002-000201010401', false, 30, null, '00000000-0000-0000-0002-000002010104','00000000-0000-0000-0000-000000000004'),
       ('00000000-0000-0000-0002-000201010402', false, 50, null, '00000000-0000-0000-0002-000002010104', '00000000-0000-0000-0000-000000000005'),
       --
       ('00000000-0000-0000-0002-000201020101', false, 60, null, '00000000-0000-0000-0002-000002010201', '00000000-0000-0000-0000-000000000001'),
       ('00000000-0000-0000-0002-000201020102', false, 90, null, '00000000-0000-0000-0002-000002010201', '00000000-0000-0000-0000-000000000002'),
       ('00000000-0000-0000-0002-000201020103', false, 120, null, '00000000-0000-0000-0002-000002010201', '00000000-0000-0000-0000-000000000003'),
       ('00000000-0000-0000-0002-000201020301', false, 30, null, '00000000-0000-0000-0002-000002010203','00000000-0000-0000-0000-000000000004'),
       ('00000000-0000-0000-0002-000201020302', false, 50, null, '00000000-0000-0000-0002-000002010203', '00000000-0000-0000-0000-000000000005');

INSERT INTO session_exercise_sets(set_id, reps_amount, weight_amount, is_finished, session_exercise_id)
VALUES  ('00000000-0000-0000-0002-020101010101', 8, 40, false, '00000000-0000-0000-0002-000201010101'),
        ('00000000-0000-0000-0002-020101010102', 8, 40, false, '00000000-0000-0000-0002-000201010101'),
        ('00000000-0000-0000-0002-020101010103', 8, 40, false, '00000000-0000-0000-0002-000201010101'),
        --
        ('00000000-0000-0000-0002-020101010201', 6, 60, false, '00000000-0000-0000-0002-000201010102'),
        ('00000000-0000-0000-0002-020101010202', 6, 60, false, '00000000-0000-0000-0002-000201010102'),
        ('00000000-0000-0000-0002-020101010203', 6, 60, false, '00000000-0000-0000-0002-000201010102'),
        ('00000000-0000-0000-0002-020101010204', 6, 60, false, '00000000-0000-0000-0002-000201010102'),
        --
        ('00000000-0000-0000-0002-020101010301', 6, 110.5, false, '00000000-0000-0000-0002-000201010103'),
        ('00000000-0000-0000-0002-020101010302', 6, 110.5, false, '00000000-0000-0000-0002-000201010103'),
        ('00000000-0000-0000-0002-020101010303', 6, 110.5, false, '00000000-0000-0000-0002-000201010103'),
        ('00000000-0000-0000-0002-020101010304', 6, 110.5, false, '00000000-0000-0000-0002-000201010103'),
        --
        ('00000000-0000-0000-0002-020101040101', 5, 10.5, false, '00000000-0000-0000-0002-000201010401'),
        ('00000000-0000-0000-0002-020101040102', 5, 10.5, false, '00000000-0000-0000-0002-000201010401'),
        --
        ('00000000-0000-0000-0002-020101040201', 8, 50, false, '00000000-0000-0000-0002-000201010402'),
        ('00000000-0000-0000-0002-020101040202', 8, 50, false, '00000000-0000-0000-0002-000201010402'),
        ('00000000-0000-0000-0002-020101040203', 8, 50, false, '00000000-0000-0000-0002-000201010402'),
        --
        --
        ('00000000-0000-0000-0002-020102010101', 8, 40, false, '00000000-0000-0000-0002-000201020101'),
        ('00000000-0000-0000-0002-020102010102', 8, 40, false, '00000000-0000-0000-0002-000201020101'),
        ('00000000-0000-0000-0002-020102010103', 8, 40, false, '00000000-0000-0000-0002-000201020101'),
        --
        ('00000000-0000-0000-0002-020102010201', 6, 60, false, '00000000-0000-0000-0002-000201020102'),
        ('00000000-0000-0000-0002-020102010202', 6, 60, false, '00000000-0000-0000-0002-000201020102'),
        ('00000000-0000-0000-0002-020102010203', 6, 60, false, '00000000-0000-0000-0002-000201020102'),
        ('00000000-0000-0000-0002-020102010204', 6, 60, false, '00000000-0000-0000-0002-000201020102'),
        --
        ('00000000-0000-0000-0002-020102010301', 6, 110.5, false, '00000000-0000-0000-0002-000201020103'),
        ('00000000-0000-0000-0002-020102010302', 6, 110.5, false, '00000000-0000-0000-0002-000201020103'),
        ('00000000-0000-0000-0002-020102010303', 6, 110.5, false, '00000000-0000-0000-0002-000201020103'),
        ('00000000-0000-0000-0002-020102010304', 6, 110.5, false, '00000000-0000-0000-0002-000201020103'),
        --
        ('00000000-0000-0000-0002-020102030101', 5, 10.5, false, '00000000-0000-0000-0002-000201020301'),
        ('00000000-0000-0000-0002-020102030102', 5, 10.5, false, '00000000-0000-0000-0002-000201020301'),
        --
        ('00000000-0000-0000-0002-020102030201', 8, 50, false, '00000000-0000-0000-0002-000201020302'),
        ('00000000-0000-0000-0002-020102030202', 8, 50, false, '00000000-0000-0000-0002-000201020302'),
        ('00000000-0000-0000-0002-020102030203', 8, 50, false, '00000000-0000-0000-0002-000201020302');