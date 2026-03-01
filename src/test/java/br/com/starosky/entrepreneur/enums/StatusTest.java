package br.com.starosky.entrepreneur.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StatusTest {

    @Test
    void fromValue_WithValidValue_ShouldReturnCorrectEnum() {
        assertEquals(Status.ACTIVE, Status.fromValue("active"));
        assertEquals(Status.INACTIVE, Status.fromValue("inactive"));
    }

    @Test
    void fromValue_WithUpperCaseValue_ShouldReturnCorrectEnum() {
        assertEquals(Status.ACTIVE, Status.fromValue("ACTIVE"));
        assertEquals(Status.INACTIVE, Status.fromValue("INACTIVE"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "", "active2", "inactive2"})
    void fromValue_WithInvalidValue_ShouldThrowException(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.fromValue(invalidValue);
        });
    }

    @Test
    void getValue_ShouldReturnCorrectValue() {
        assertEquals("active", Status.ACTIVE.getValue());
        assertEquals("inactive", Status.INACTIVE.getValue());
    }
}
