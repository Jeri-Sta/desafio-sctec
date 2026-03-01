package br.com.starosky.entrepreneur.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OperatingSegmentTest {

    @Test
    void fromValue_WithValidValue_ShouldReturnCorrectEnum() {
        assertEquals(OperatingSegment.TECHNOLOGY, OperatingSegment.fromValue("technology"));
        assertEquals(OperatingSegment.COMMERCE, OperatingSegment.fromValue("commerce"));
        assertEquals(OperatingSegment.INDUSTRY, OperatingSegment.fromValue("industry"));
        assertEquals(OperatingSegment.SERVICES, OperatingSegment.fromValue("services"));
        assertEquals(OperatingSegment.AGRIBUSINESS, OperatingSegment.fromValue("agribusiness"));
    }

    @Test
    void fromValue_WithUpperCaseValue_ShouldReturnCorrectEnum() {
        assertEquals(OperatingSegment.TECHNOLOGY, OperatingSegment.fromValue("TECHNOLOGY"));
        assertEquals(OperatingSegment.COMMERCE, OperatingSegment.fromValue("COMMERCE"));
        assertEquals(OperatingSegment.INDUSTRY, OperatingSegment.fromValue("INDUSTRY"));
        assertEquals(OperatingSegment.SERVICES, OperatingSegment.fromValue("SERVICES"));
        assertEquals(OperatingSegment.AGRIBUSINESS, OperatingSegment.fromValue("AGRIBUSINESS"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "", "tech", "commercial"})
    void fromValue_WithInvalidValue_ShouldThrowException(String invalidValue) {
        assertThrows(IllegalArgumentException.class, () -> {
            OperatingSegment.fromValue(invalidValue);
        });
    }

    @Test
    void getValue_ShouldReturnCorrectValue() {
        assertEquals("technology", OperatingSegment.TECHNOLOGY.getValue());
        assertEquals("commerce", OperatingSegment.COMMERCE.getValue());
        assertEquals("industry", OperatingSegment.INDUSTRY.getValue());
        assertEquals("services", OperatingSegment.SERVICES.getValue());
        assertEquals("agribusiness", OperatingSegment.AGRIBUSINESS.getValue());
    }
}
