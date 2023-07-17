package com.sd.shapyfy.boundary.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.NotProperResourceState;
import com.sd.shapyfy.domain.model.exception.TrainingNotFilledProperlyException;
import com.sd.shapyfy.domain.model.TrainingDayId;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedRestCallException.class)
    public ResponseEntity<Object> handleUnauthorizedCall(UnauthorizedRestCallException ex) {
        log.error("Unauthorized call on {}", extractCurrentRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .badRequest()
                .body(ValidationErrorDocument.from(ex.getBindingResult().getAllErrors()));
    }

    @ExceptionHandler(NotProperResourceState.class)
    public ResponseEntity<Object> handleNotProperResourceState(NotProperResourceState ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(TrainingNotFilledProperlyException.class)
    public ResponseEntity<Object> handleTrainingNotFilledProperly(TrainingNotFilledProperlyException ex) {
        return ResponseEntity
                .badRequest()
                .body(TrainingValidationErrorDocument.from(ex.getTrainingDayIds()));
    }

    private static String extractCurrentRequestURI() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
        } catch (NullPointerException ex) {
            return "unknown";
        }
    }

    @Value
    private static class ValidationErrorDocument {

        List<FieldValidationError> errors;

        public static ValidationErrorDocument from(List<ObjectError> allErrors) {
            return new ValidationErrorDocument(allErrors.stream()
                    .map(error -> (FieldError) error)
                    .map(error -> FieldValidationError.of(error.getField(), error.getDefaultMessage()))
                    .toList());

        }

        @Value(staticConstructor = "of")
        private static class FieldValidationError {

            String field;

            String message;
        }
    }

    private record TrainingValidationErrorDocument(
            List<TrainingDayError> errors
    ) {

        public static TrainingValidationErrorDocument from(List<TrainingDayId> trainingDayIds) {
            return new TrainingValidationErrorDocument(
                    trainingDayIds.stream().map(id -> new TrainingDayError(id.getValue().toString(), "Training day not filled properly")).toList());
        }

        private record TrainingDayError(

                @JsonProperty("training_day_id")
                String dayId,

                @JsonProperty("message")
                String message
        ) {
        }
    }
}
