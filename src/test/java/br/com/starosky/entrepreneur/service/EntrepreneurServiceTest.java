package br.com.starosky.entrepreneur.service;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.entity.Entrepreneur;
import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;
import br.com.starosky.entrepreneur.exception.ValidationException;
import br.com.starosky.entrepreneur.repository.EntrepreneurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntrepreneurServiceTest {

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private CityValidationService cityValidationService;

    @InjectMocks
    private EntrepreneurService entrepreneurService;

    private EntrepreneurRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = EntrepreneurRequest.builder()
                .enterpriseName("Tech Solutions")
                .entrepreneurName("John Doe")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("john@example.com")
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void create_WithValidData_ShouldReturnCreatedEntrepreneur() {
        when(cityValidationService.isValidCity("Florianopolis")).thenReturn(true);

        Entrepreneur savedEntrepreneur = Entrepreneur.builder()
                .id(1L)
                .enterpriseName("Tech Solutions")
                .entrepreneurName("John Doe")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("john@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(savedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.create(validRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Tech Solutions", response.getEnterpriseName());
        assertEquals("John Doe", response.getEntrepreneurName());
        assertEquals("Florianopolis", response.getCity());
        assertEquals(OperatingSegment.TECHNOLOGY, response.getOperatingSegment());
        assertEquals("john@example.com", response.getContact());
        assertEquals(Status.ACTIVE, response.getStatus());

        verify(entrepreneurRepository, times(1)).save(any(Entrepreneur.class));
    }

    @Test
    void create_WithInvalidCity_ShouldThrowValidationException() {
        when(cityValidationService.isValidCity("InvalidCity")).thenReturn(false);

        EntrepreneurRequest requestWithInvalidCity = EntrepreneurRequest.builder()
                .enterpriseName("Tech Solutions")
                .entrepreneurName("John Doe")
                .city("InvalidCity")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("john@example.com")
                .status(Status.ACTIVE)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            entrepreneurService.create(requestWithInvalidCity);
        });

        assertEquals("A cidade deve ser um munícipio pertencente á Santa Catarina", exception.getMessage());
        verify(entrepreneurRepository, never()).save(any(Entrepreneur.class));
    }

    @Test
    void create_WithDefaultStatus_ShouldSetActiveStatus() {
        when(cityValidationService.isValidCity("Joinville")).thenReturn(true);

        EntrepreneurRequest requestWithoutStatus = EntrepreneurRequest.builder()
                .enterpriseName("Commerce Store")
                .entrepreneurName("Jane Doe")
                .city("Joinville")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("jane@example.com")
                .build();

        Entrepreneur savedEntrepreneur = Entrepreneur.builder()
                .id(1L)
                .enterpriseName("Commerce Store")
                .entrepreneurName("Jane Doe")
                .city("Joinville")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("jane@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(savedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.create(requestWithoutStatus);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    @Test
    void create_WithAllOperatingSegments_ShouldWork() {
        OperatingSegment[] segments = OperatingSegment.values();

        for (OperatingSegment segment : segments) {
            when(cityValidationService.isValidCity("Blumenau")).thenReturn(true);

            EntrepreneurRequest request = EntrepreneurRequest.builder()
                    .enterpriseName("Test Company")
                    .entrepreneurName("Test Person")
                    .city("Blumenau")
                    .operatingSegment(segment)
                    .contact("test@example.com")
                    .status(Status.ACTIVE)
                    .build();

            Entrepreneur savedEntrepreneur = Entrepreneur.builder()
                    .id(1L)
                    .enterpriseName("Test Company")
                    .entrepreneurName("Test Person")
                    .city("Blumenau")
                    .operatingSegment(segment)
                    .contact("test@example.com")
                    .status(Status.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(savedEntrepreneur);

            EntrepreneurResponse response = entrepreneurService.create(request);

            assertNotNull(response);
            assertEquals(segment, response.getOperatingSegment());
        }
    }

    @Test
    void create_WithInactiveStatus_ShouldSetInactiveStatus() {
        when(cityValidationService.isValidCity("Criciuma")).thenReturn(true);

        EntrepreneurRequest requestWithInactiveStatus = EntrepreneurRequest.builder()
                .enterpriseName("Inactive Company")
                .entrepreneurName("Inactive Person")
                .city("Criciuma")
                .operatingSegment(OperatingSegment.INDUSTRY)
                .contact("inactive@example.com")
                .status(Status.INACTIVE)
                .build();

        Entrepreneur savedEntrepreneur = Entrepreneur.builder()
                .id(1L)
                .enterpriseName("Inactive Company")
                .entrepreneurName("Inactive Person")
                .city("Criciuma")
                .operatingSegment(OperatingSegment.INDUSTRY)
                .contact("inactive@example.com")
                .status(Status.INACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(savedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.create(requestWithInactiveStatus);

        assertNotNull(response);
        assertEquals(Status.INACTIVE, response.getStatus());
    }
}
