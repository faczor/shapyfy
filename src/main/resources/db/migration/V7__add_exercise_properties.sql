-- =====================================================
-- EXERCISE PROPERTIES: Enhanced exercise metadata
-- =====================================================
-- This migration adds properties for workout recommendation:
-- - primary_muscle_group: Main muscle targeted (single value)
-- - secondary_muscle_groups: Supporting muscles worked (array)
-- - equipment_required: What equipment is needed (array)
-- - difficulty: BEGINNER or ADVANCED
-- - movement_pattern: PUSH, PULL, SQUAT, HINGE, ISOLATION
-- =====================================================

-- Add new columns to exercises table
ALTER TABLE exercises
    ADD COLUMN primary_muscle_group VARCHAR(20) NOT NULL DEFAULT 'CHEST',
    ADD COLUMN secondary_muscle_groups TEXT[] NOT NULL DEFAULT '{}',
    ADD COLUMN equipment_required TEXT[] NOT NULL DEFAULT '{}',
    ADD COLUMN difficulty VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    ADD COLUMN movement_pattern VARCHAR(20) NOT NULL DEFAULT 'ISOLATION';

-- Remove defaults after adding columns (force explicit values going forward)
ALTER TABLE exercises
    ALTER COLUMN primary_muscle_group DROP DEFAULT,
    ALTER COLUMN secondary_muscle_groups DROP DEFAULT,
    ALTER COLUMN equipment_required DROP DEFAULT,
    ALTER COLUMN difficulty DROP DEFAULT,
    ALTER COLUMN movement_pattern DROP DEFAULT;

-- Add constraints
ALTER TABLE exercises
    ADD CONSTRAINT chk_difficulty CHECK (difficulty IN ('BEGINNER', 'ADVANCED')),
    ADD CONSTRAINT chk_movement_pattern CHECK (movement_pattern IN ('PUSH', 'PULL', 'SQUAT', 'HINGE', 'ISOLATION')),
    ADD CONSTRAINT chk_primary_muscle_group CHECK (primary_muscle_group IN ('CHEST', 'BACK', 'SHOULDERS', 'BICEPS', 'TRICEPS', 'QUADS', 'HAMSTRINGS', 'GLUTES', 'CALVES', 'CORE', 'FOREARMS')),
    ADD CONSTRAINT chk_equipment_not_empty CHECK (array_length(equipment_required, 1) > 0);

-- Create indexes for filtering exercises by equipment, muscle groups, and difficulty
CREATE INDEX idx_exercises_equipment ON exercises USING GIN(equipment_required);
CREATE INDEX idx_exercises_primary_muscle ON exercises(primary_muscle_group);
CREATE INDEX idx_exercises_secondary_muscles ON exercises USING GIN(secondary_muscle_groups);
CREATE INDEX idx_exercises_difficulty ON exercises(difficulty);
CREATE INDEX idx_exercises_movement_pattern ON exercises(movement_pattern);

-- =====================================================
-- Valid enum values for reference:
-- =====================================================
-- Equipment: BODYWEIGHT, DUMBBELLS, BARBELL, KETTLEBELL,
--            RESISTANCE_BANDS, PULL_UP_BAR, BENCH,
--            CABLE_MACHINE, SMITH_MACHINE
--
-- MuscleGroup: CHEST, BACK, SHOULDERS, BICEPS, TRICEPS,
--              QUADS, HAMSTRINGS, GLUTES, CALVES, CORE, FOREARMS
--
-- Difficulty: BEGINNER, ADVANCED
--
-- MovementPattern: PUSH, PULL, SQUAT, HINGE, ISOLATION
-- =====================================================
