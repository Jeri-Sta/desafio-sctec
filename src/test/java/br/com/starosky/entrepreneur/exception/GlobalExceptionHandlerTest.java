package br.com.starosky.entrepreneur.exception;

import br.com.starosky.entrepreneur.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationException_ShouldReturn400BadRequest() {
        ValidationException exception = new ValidationException("City is invalid");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("City is invalid", response.getBody().getMessage());
    }

    @Test
    void handleValidationErrors_ShouldReturn400BadRequestWithFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        FieldError fieldError1 = new FieldError("object", "enterpriseName", "Enterprise name is required");
        FieldError fieldError2 = new FieldError("object", "city", "City is required");

        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(ex.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Validation") || response.getBody().getMessage().contains("Valida"));
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Enterprise name is required"));
        assertTrue(response.getBody().getErrors().contains("City is required"));
    }

    @Test
    void handleGenericException_ShouldReturn500InternalServerError() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("error") || response.getBody().getMessage().contains("erro"));
    }

    @Test
    void handleValidationException_NullMessage_ShouldReturn400WithNullMessage() {
        ValidationException exception = new ValidationException(null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getMessage());
    }
}
