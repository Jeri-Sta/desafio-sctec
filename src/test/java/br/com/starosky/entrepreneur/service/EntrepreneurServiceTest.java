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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void findById_WhenExists_ShouldReturnEntrepreneur() {
        Long id = 1L;
        Entrepreneur entrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Tech Solutions")
                .entrepreneurName("John Doe")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("john@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(entrepreneur));

        Optional<EntrepreneurResponse> result = entrepreneurService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Tech Solutions", result.get().getEnterpriseName());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Long id = 999L;
        when(entrepreneurRepository.findById(id)).thenReturn(Optional.empty());

        Optional<EntrepreneurResponse> result = entrepreneurService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnPageOfEntrepreneurs() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Entrepreneur> entrepreneurs = Arrays.asList(
                Entrepreneur.builder().id(1L).enterpriseName("Company 1").entrepreneurName("Person 1")
                        .city("City 1").operatingSegment(OperatingSegment.TECHNOLOGY).contact("email1@test.com")
                        .status(Status.ACTIVE).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),
                Entrepreneur.builder().id(2L).enterpriseName("Company 2").entrepreneurName("Person 2")
                        .city("City 2").operatingSegment(OperatingSegment.COMMERCE).contact("email2@test.com")
                        .status(Status.ACTIVE).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
        );
        Page<Entrepreneur> page = new PageImpl<>(entrepreneurs, pageable, entrepreneurs.size());

        when(entrepreneurRepository.findAll(pageable)).thenReturn(page);

        Page<EntrepreneurResponse> result = entrepreneurService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedEntrepreneur() {
        Long id = 1L;
        Entrepreneur existingEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Old Company")
                .entrepreneurName("Old Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("old@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(existingEntrepreneur));
        when(cityValidationService.isValidCity("Blumenau")).thenReturn(true);

        EntrepreneurRequest updateRequest = EntrepreneurRequest.builder()
                .enterpriseName("New Company")
                .entrepreneurName("New Name")
                .city("Blumenau")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("new@example.com")
                .status(Status.ACTIVE)
                .build();

        Entrepreneur updatedEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("New Company")
                .entrepreneurName("New Name")
                .city("Blumenau")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("new@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(updatedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.update(id, updateRequest);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("New Company", response.getEnterpriseName());
        assertEquals("New Name", response.getEntrepreneurName());
        assertEquals("Blumenau", response.getCity());
        assertEquals(OperatingSegment.COMMERCE, response.getOperatingSegment());
        assertEquals("new@example.com", response.getContact());

        verify(entrepreneurRepository, times(1)).save(any(Entrepreneur.class));
    }

    @Test
    void update_WithInvalidCity_ShouldThrowValidationException() {
        Long id = 1L;
        Entrepreneur existingEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Old Company")
                .entrepreneurName("Old Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("old@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(existingEntrepreneur));
        when(cityValidationService.isValidCity("InvalidCity")).thenReturn(false);

        EntrepreneurRequest updateRequest = EntrepreneurRequest.builder()
                .enterpriseName("New Company")
                .entrepreneurName("New Name")
                .city("InvalidCity")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("new@example.com")
                .status(Status.ACTIVE)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            entrepreneurService.update(id, updateRequest);
        });

        assertEquals("A cidade deve ser um munícipio pertencente á Santa Catarina", exception.getMessage());
        verify(entrepreneurRepository, never()).save(any(Entrepreneur.class));
    }

    @Test
    void update_WhenNotExists_ShouldThrowValidationException() {
        Long id = 999L;
        when(entrepreneurRepository.findById(id)).thenReturn(Optional.empty());

        EntrepreneurRequest updateRequest = EntrepreneurRequest.builder()
                .enterpriseName("New Company")
                .entrepreneurName("New Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("new@example.com")
                .status(Status.ACTIVE)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            entrepreneurService.update(id, updateRequest);
        });

        assertEquals("Empreendedor não encontrado", exception.getMessage());
        verify(entrepreneurRepository, never()).save(any(Entrepreneur.class));
    }

    @Test
    void update_WithStatusChange_ShouldUpdateStatus() {
        Long id = 1L;
        Entrepreneur existingEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Company")
                .entrepreneurName("Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("email@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(existingEntrepreneur));
        when(cityValidationService.isValidCity("Florianopolis")).thenReturn(true);

        EntrepreneurRequest updateRequest = EntrepreneurRequest.builder()
                .enterpriseName("Company")
                .entrepreneurName("Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("email@example.com")
                .status(Status.INACTIVE)
                .build();

        Entrepreneur updatedEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Company")
                .entrepreneurName("Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("email@example.com")
                .status(Status.INACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(updatedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.update(id, updateRequest);

        assertNotNull(response);
        assertEquals(Status.INACTIVE, response.getStatus());
    }

    @Test
    void update_WithoutStatus_ShouldKeepExistingStatus() {
        Long id = 1L;
        Entrepreneur existingEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Company")
                .entrepreneurName("Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("email@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(existingEntrepreneur));
        when(cityValidationService.isValidCity("Florianopolis")).thenReturn(true);

        EntrepreneurRequest updateRequest = EntrepreneurRequest.builder()
                .enterpriseName("Updated Company")
                .entrepreneurName("Updated Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("updated@example.com")
                .build();

        Entrepreneur updatedEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Updated Company")
                .entrepreneurName("Updated Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.COMMERCE)
                .contact("updated@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(updatedEntrepreneur);

        EntrepreneurResponse response = entrepreneurService.update(id, updateRequest);

        assertNotNull(response);
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    @Test
    void delete_WithValidId_ShouldChangeStatusToInactive() {
        Long id = 1L;
        Entrepreneur existingEntrepreneur = Entrepreneur.builder()
                .id(id)
                .enterpriseName("Company")
                .entrepreneurName("Name")
                .city("Florianopolis")
                .operatingSegment(OperatingSegment.TECHNOLOGY)
                .contact("email@example.com")
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(entrepreneurRepository.findById(id)).thenReturn(Optional.of(existingEntrepreneur));
        when(entrepreneurRepository.save(any(Entrepreneur.class))).thenReturn(existingEntrepreneur);

        entrepreneurService.delete(id);

        verify(entrepreneurRepository, times(1)).save(any(Entrepreneur.class));
    }

    @Test
    void delete_WhenNotExists_ShouldThrowValidationException() {
        Long id = 999L;
        when(entrepreneurRepository.findById(id)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            entrepreneurService.delete(id);
        });

        assertEquals("Empreendedor não encontrado", exception.getMessage());
        verify(entrepreneurRepository, never()).save(any(Entrepreneur.class));
    }
}
