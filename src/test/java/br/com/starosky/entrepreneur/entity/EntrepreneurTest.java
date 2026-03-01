package br.com.starosky.entrepreneur.entity;

import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

class EntrepreneurTest {

    private Entrepreneur entrepreneur;

    @BeforeEach
    void setUp() {
        entrepreneur = Entrepreneur.builder()
                .id(1L)
                .enterpriseName("Tech Solutions")
                .entrepreneurName("John Doe")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("john@example.com")
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void builder_ShouldCreateEntrepreneurWithAllFields() {
        assertNotNull(entrepreneur);
        assertEquals(1L, entrepreneur.getId());
        assertEquals("Tech Solutions", entrepreneur.getEnterpriseName());
        assertEquals("John Doe", entrepreneur.getEntrepreneurName());
        assertEquals("Florianopolis", entrepreneur.getCity());
        assertEquals(OperatingSegment.TECHNOLOGY, entrepreneur.getOperatingSegment());
        assertEquals("john@example.com", entrepreneur.getContact());
        assertEquals(Status.ACTIVE, entrepreneur.getStatus());
    }

    @Test
    void prePersist_ShouldSetDefaultStatusToActive() {
        entrepreneur = Entrepreneur.builder()
                .enterpriseName("Test")
                .entrepreneurName("Test")
                .city("Test")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("test@test.com")
                .build();

        entrepreneur.onCreate();

        assertEquals(Status.ACTIVE, entrepreneur.getStatus());
        assertNotNull(entrepreneur.getCreatedAt());
        assertNotNull(entrepreneur.getUpdatedAt());
    }

    @Test
    void prePersist_ShouldSetCreatedAtAndUpdatedAt() {
        entrepreneur.onCreate();

        assertNotNull(entrepreneur.getCreatedAt());
        assertNotNull(entrepreneur.getUpdatedAt());
    }

    @Test
    void preUpdate_ShouldUpdateUpdatedAt() {
        LocalDateTime originalUpdatedAt = LocalDateTime.now().minusDays(1);
        entrepreneur.setUpdatedAt(originalUpdatedAt);

        entrepreneur.onUpdate();

        assertTrue(entrepreneur.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void defaultStatus_ShouldBeActive() {
        assertEquals(Status.ACTIVE, Status.ACTIVE);
    }
}
