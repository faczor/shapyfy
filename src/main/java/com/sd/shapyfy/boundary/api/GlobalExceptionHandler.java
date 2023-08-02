package com.sd.shapyfy.boundary.api;

import com.sd.shapyfy.domain.InvalidDomainResourceState;
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

    @ExceptionHandler(InvalidDomainResourceState.class)
    public ResponseEntity<Object> handleNotProperResourceState(InvalidDomainResourceState ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
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

}
