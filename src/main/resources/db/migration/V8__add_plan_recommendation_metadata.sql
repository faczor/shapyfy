-- =====================================================
-- WORKOUT PLAN RECOMMENDATIONS: Metadata for AI-generated plans
-- =====================================================
-- This migration adds recommendation metadata to workout plans:
-- - is_recommended: Flag for AI-recommended plans
-- - recommendation_score: 0.0-1.0 score for how well plan fits preferences
-- - target_experience_level: BEGINNER, INTERMEDIATE, ADVANCED
-- - target_goal: MUSCLE_BUILDING, STRENGTH, WEIGHT_LOSS, GENERAL_FITNESS
-- =====================================================

-- Add recommendation columns to workout_plans table
ALTER TABLE workout_plans
    ADD COLUMN is_recommended BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN recommendation_score DECIMAL(3,2),
    ADD COLUMN target_experience_level VARCHAR(20),
    ADD COLUMN target_goal VARCHAR(30);

-- Remove default for is_recommended (force explicit values going forward)
ALTER TABLE workout_plans
    ALTER COLUMN is_recommended DROP DEFAULT;

-- Add constraints
ALTER TABLE workout_plans
    ADD CONSTRAINT chk_recommendation_score CHECK (recommendation_score IS NULL OR (recommendation_score >= 0 AND recommendation_score <= 1)),
    ADD CONSTRAINT chk_target_experience CHECK (target_experience_level IS NULL OR target_experience_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    ADD CONSTRAINT chk_target_goal CHECK (target_goal IS NULL OR target_goal IN ('MUSCLE_BUILDING', 'STRENGTH', 'WEIGHT_LOSS', 'GENERAL_FITNESS'));

-- Create index for finding recommended plans by experience and goal
CREATE INDEX idx_workout_plans_recommended ON workout_plans(is_recommended, target_experience_level, target_goal)
    WHERE is_recommended = true;

-- =====================================================
-- Valid enum values for reference:
-- =====================================================
-- ExperienceLevel: BEGINNER, INTERMEDIATE, ADVANCED
-- FitnessGoal: MUSCLE_BUILDING, STRENGTH, WEIGHT_LOSS, GENERAL_FITNESS
-- =====================================================
