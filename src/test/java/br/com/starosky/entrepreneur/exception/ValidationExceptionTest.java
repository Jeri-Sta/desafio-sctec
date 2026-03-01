package br.com.starosky.entrepreneur.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

    @Test
    void constructor_WithMessage_ShouldSetMessage() {
        String message = "Validation failed";

        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_WithMessageAndCause_ShouldSetMessageAndCause() {
        String message = "Validation failed";
        Throwable cause = new RuntimeException("Original cause");

        ValidationException exception = new ValidationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void getMessage_ShouldReturnCorrectMessage() {
        String message = "City must be valid";
        ValidationException exception = new ValidationException(message);

        assertEquals("City must be valid", exception.getMessage());
    }

    @Test
    void getCause_ShouldReturnCorrectCause() {
        Throwable cause = new IllegalArgumentException("Invalid input");
        ValidationException exception = new ValidationException("Error", cause);

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }
}
