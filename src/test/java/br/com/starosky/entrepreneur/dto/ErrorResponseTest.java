package br.com.starosky.entrepreneur.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class ErrorResponseTest {

    @Test
    void of_WithStatusAndMessage_ShouldCreateErrorResponse() {
        int status = 400;
        String message = "Bad Request";

        ErrorResponse response = ErrorResponse.of(status, message);

        assertNotNull(response);
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void of_WithStatusMessageAndErrors_ShouldCreateErrorResponse() {
        int status = 400;
        String message = "Validation failed";
        List<String> errors = Arrays.asList("Field1 is required", "Field2 must not be empty");

        ErrorResponse response = ErrorResponse.of(status, message, errors);

        assertNotNull(response);
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(2, response.getErrors().size());
        assertTrue(response.getErrors().contains("Field1 is required"));
        assertTrue(response.getErrors().contains("Field2 must not be empty"));
        assertNotNull(response.getTimestamp());
    }

    @Test
    void builder_ShouldWorkCorrectly() {
        ErrorResponse response = ErrorResponse.builder()
                .status(500)
                .message("Internal Server Error")
                .errors(Arrays.asList("Error 1"))
                .timestamp(LocalDateTime.now())
                .build();

        assertNotNull(response);
        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getMessage());
        assertEquals(1, response.getErrors().size());
    }

    @Test
    void timestamp_ShouldBeSetAutomatically() {
        ErrorResponse response = ErrorResponse.of(404, "Not Found");
        assertNotNull(response.getTimestamp());
    }

    @Test
    void errorsList_ShouldBeNullWhenNotProvided() {
        ErrorResponse response = ErrorResponse.of(400, "Bad Request");
        assertNull(response.getErrors());
    }
}
