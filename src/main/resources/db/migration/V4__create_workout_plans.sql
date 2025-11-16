-- =====================================================
-- WORKOUT PLANS: Template-based workout planning
-- =====================================================
-- This migration adds support for workout plans (templates) that define:
-- - Multi-day cycles (not just 7-day weeks, e.g., 8-day PPL cycle)
-- - Explicit workout and rest days in sequence
-- - Target exercises with sets/reps/weight for each day
-- - Both user-specific plans and global shareable templates
-- =====================================================

-- Create workout_plans table (aggregate root)
CREATE TABLE workout_plans (
    id UUID PRIMARY KEY,
    user_id VARCHAR(128),  -- NULL = global template, NOT NULL = user-specific plan
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cycle_days INT NOT NULL,  -- How many days in the plan cycle (e.g., 7 for weekly, 8 for PPL)
    is_active BOOLEAN NOT NULL DEFAULT false,  -- Only one active plan per user
    activation_date DATE,  -- When user activated this plan (for cycle day calculation)
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT chk_cycle_days CHECK (cycle_days > 0 AND cycle_days <= 30)
);

-- Index for finding user's active plan
CREATE INDEX idx_workout_plans_user_active ON workout_plans(user_id, is_active)
    WHERE is_active = true;

-- Index for global templates
CREATE INDEX idx_workout_plans_templates ON workout_plans(user_id)
    WHERE user_id IS NULL;

-- Create plan_days table (part of plan aggregate)
CREATE TABLE plan_days (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL,
    day_index INT NOT NULL,  -- 0-based index within cycle (0 to cycle_days-1)
    name VARCHAR(255),       -- e.g., "Push Day 1", "Rest", "Pull Day 2"
    day_type VARCHAR(20) NOT NULL,
    notes TEXT,              -- Optional notes (e.g., "Focus on form", "Active rest - cardio")
    CONSTRAINT fk_plan_days_plan FOREIGN KEY (plan_id)
        REFERENCES workout_plans(id) ON DELETE CASCADE,
    CONSTRAINT uq_plan_day_index UNIQUE(plan_id, day_index),
    CONSTRAINT chk_day_type CHECK (day_type IN ('WORKOUT', 'REST')),
    CONSTRAINT chk_day_index CHECK (day_index >= 0)
);

-- Create plan_exercises table (part of plan aggregate)
CREATE TABLE plan_exercises (
    id UUID PRIMARY KEY,
    plan_day_id UUID NOT NULL,
    exercise_id UUID NOT NULL,
    order_index INT NOT NULL,  -- Order within the day
    target_sets INT NOT NULL,
    target_reps INT,           -- Target reps per set (e.g., 10 in "3x10")
    target_weight DECIMAL(10,2),  -- Optional target weight
    notes TEXT,                -- Exercise-specific notes (e.g., "Pause reps", "Dropset on last set")
    CONSTRAINT fk_plan_exercises_day FOREIGN KEY (plan_day_id)
        REFERENCES plan_days(id) ON DELETE CASCADE,
    CONSTRAINT fk_plan_exercises_exercise FOREIGN KEY (exercise_id)
        REFERENCES exercises(id),
    CONSTRAINT uq_plan_exercise_order UNIQUE(plan_day_id, order_index),
    CONSTRAINT chk_target_sets CHECK (target_sets > 0),
    CONSTRAINT chk_target_reps CHECK (target_reps IS NULL OR target_reps > 0),
    CONSTRAINT chk_order_index_plan CHECK (order_index >= 0)
);

-- Index for efficient plan day exercise queries
CREATE INDEX idx_plan_exercises_day ON plan_exercises(plan_day_id, order_index);

-- =====================================================
-- Link workouts to plan days (optional reference)
-- =====================================================
-- When user starts workout from plan, we track which plan day it was
-- This enables tracking adherence and performance vs targets
ALTER TABLE workouts ADD COLUMN plan_day_id UUID;
ALTER TABLE workouts ADD CONSTRAINT fk_workouts_plan_day
    FOREIGN KEY (plan_day_id) REFERENCES plan_days(id) ON DELETE SET NULL;

-- Index for workout-to-plan tracking
CREATE INDEX idx_workouts_plan_day ON workouts(plan_day_id);

-- =====================================================
-- Ensure only one active plan per user
-- =====================================================
-- Partial unique index: only one active plan per user (excluding templates)
CREATE UNIQUE INDEX uq_user_active_plan ON workout_plans(user_id)
    WHERE is_active = true AND user_id IS NOT NULL;
