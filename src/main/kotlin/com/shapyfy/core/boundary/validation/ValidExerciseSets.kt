package com.shapyfy.core.boundary.validation

import com.shapyfy.core.boundary.workouts.WorkoutExerciseRequest
import com.shapyfy.core.domain.workout.ExerciseStatus
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ExerciseSetsValidator::class])
annotation class ValidExerciseSets(
    val message: String = "sets cannot be empty for completed exercises",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ExerciseSetsValidator : ConstraintValidator<ValidExerciseSets, WorkoutExerciseRequest> {

    override fun isValid(
        request: WorkoutExerciseRequest?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (request == null) return true

        val status = try {
            ExerciseStatus.valueOf(request.status.uppercase())
        } catch (e: IllegalArgumentException) {
            // Invalid status - let other validators handle this
            return true
        }

        // Only COMPLETED exercises require sets
        if (status.requiresSets() && request.sets.isEmpty()) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("sets cannot be empty for completed exercises")
                .addPropertyNode("sets")
                .addConstraintViolation()
            return false
        }

        return true
    }
}
