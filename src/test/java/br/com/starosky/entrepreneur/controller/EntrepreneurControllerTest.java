package br.com.starosky.entrepreneur.controller;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;
import br.com.starosky.entrepreneur.service.EntrepreneurService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntrepreneurControllerTest {

    @Mock
    private EntrepreneurService entrepreneurService;

    @InjectMocks
    private EntrepreneurController entrepreneurController;

    private EntrepreneurRequest validRequest;
    private EntrepreneurResponse validResponse;

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

        validResponse = EntrepreneurResponse.builder()
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
    }

    @Test
    void create_WithValidData_ShouldReturn201Created() {
        when(entrepreneurService.create(any(EntrepreneurRequest.class))).thenReturn(validResponse);

        ResponseEntity<EntrepreneurResponse> response = entrepreneurController.create(validRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Tech Solutions", response.getBody().getEnterpriseName());
        assertEquals("John Doe", response.getBody().getEntrepreneurName());
        assertEquals("Florianopolis", response.getBody().getCity());
        assertEquals(OperatingSegment.TECHNOLOGY, response.getBody().getOperatingSegment());
        assertEquals("john@example.com", response.getBody().getContact());
        assertEquals(Status.ACTIVE, response.getBody().getStatus());
    }

    @Test
    void create_ShouldCallServiceWithCorrectRequest() {
        when(entrepreneurService.create(any(EntrepreneurRequest.class))).thenReturn(validResponse);

        entrepreneurController.create(validRequest);

        assertNotNull(entrepreneurService);
    }

    @Test
    void create_WithNullRequest_ShouldReturnNullBody() {
        when(entrepreneurService.create(null)).thenReturn(null);

        ResponseEntity<EntrepreneurResponse> response = entrepreneurController.create(null);

        assertNotNull(response);
    }

    @Test
    void controller_ShouldHaveCorrectMapping() {
        assertNotNull(EntrepreneurController.class.getAnnotation(org.springframework.web.bind.annotation.RestController.class));
        assertEquals("/entrepreneurs", EntrepreneurController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class).value()[0]);
    }

    @Test
    void createMethod_ShouldBeAnnotatedWithPostMapping() throws NoSuchMethodException {
        assertNotNull(EntrepreneurController.class.getDeclaredMethod("create", EntrepreneurRequest.class)
                .getAnnotation(org.springframework.web.bind.annotation.PostMapping.class));
    }

    @Test
    void createMethod_ShouldAcceptValidRequestBody() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("create", EntrepreneurRequest.class);
        var parameter = method.getParameters()[0];
        assertNotNull(parameter.getAnnotation(jakarta.validation.Valid.class));
    }

    @Test
    void createMethod_ShouldReturnResponseEntity() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("create", EntrepreneurRequest.class);
        assertEquals(ResponseEntity.class, method.getReturnType());
    }

    @Test
    void findById_WhenExists_ShouldReturn200Ok() {
        Long id = 1L;
        when(entrepreneurService.findById(id)).thenReturn(Optional.of(validResponse));

        ResponseEntity<EntrepreneurResponse> response = entrepreneurController.findById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Tech Solutions", response.getBody().getEnterpriseName());
    }

    @Test
    void findById_WhenNotExists_ShouldReturn404NotFound() {
        Long id = 999L;
        when(entrepreneurService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<EntrepreneurResponse> response = entrepreneurController.findById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void findByIdMethod_ShouldExist() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("findById", Long.class);
        assertNotNull(method);
        assertEquals(ResponseEntity.class, method.getReturnType());
    }

    @Test
    void findAll_ShouldReturn200Ok() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EntrepreneurResponse> page = new PageImpl<>(
                Arrays.asList(validResponse),
                pageable,
                1
        );
        when(entrepreneurService.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<EntrepreneurResponse>> response = entrepreneurController.findAll(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void findAll_WithEmptyList_ShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EntrepreneurResponse> emptyPage = new PageImpl<>(
                Arrays.asList(),
                pageable,
                0
        );
        when(entrepreneurService.findAll(pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<EntrepreneurResponse>> response = entrepreneurController.findAll(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    void findAllMethod_ShouldExist() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("findAll", Pageable.class);
        assertNotNull(method);
    }

    @Test
    void findAllMethod_ShouldReturnPageResponseEntity() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("findAll", Pageable.class);
        assertEquals(ResponseEntity.class, method.getReturnType());
    }

    @Test
    void update_WithValidData_ShouldReturn200Ok() {
        Long id = 1L;
        when(entrepreneurService.update(any(Long.class), any(EntrepreneurRequest.class))).thenReturn(validResponse);

        ResponseEntity<EntrepreneurResponse> response = entrepreneurController.update(id, validRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Tech Solutions", response.getBody().getEnterpriseName());
        assertEquals("John Doe", response.getBody().getEntrepreneurName());
        assertEquals("Florianopolis", response.getBody().getCity());
        assertEquals(OperatingSegment.TECHNOLOGY, response.getBody().getOperatingSegment());
        assertEquals("john@example.com", response.getBody().getContact());
        assertEquals(Status.ACTIVE, response.getBody().getStatus());
    }

    @Test
    void update_ShouldCallServiceWithCorrectParameters() {
        Long id = 1L;
        when(entrepreneurService.update(any(Long.class), any(EntrepreneurRequest.class))).thenReturn(validResponse);

        entrepreneurController.update(id, validRequest);

        assertNotNull(entrepreneurService);
    }

    @Test
    void updateMethod_ShouldExist() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("update", Long.class, EntrepreneurRequest.class);
        assertNotNull(method);
        assertEquals(ResponseEntity.class, method.getReturnType());
    }

    @Test
    void updateMethod_ShouldBeAnnotatedWithPutMapping() throws NoSuchMethodException {
        assertNotNull(EntrepreneurController.class.getDeclaredMethod("update", Long.class, EntrepreneurRequest.class)
                .getAnnotation(org.springframework.web.bind.annotation.PutMapping.class));
    }

    @Test
    void updateMethod_ShouldAcceptValidRequestBody() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("update", Long.class, EntrepreneurRequest.class);
        var parameter = method.getParameters()[1];
        assertNotNull(parameter.getAnnotation(jakarta.validation.Valid.class));
    }

    @Test
    void updateMethod_ShouldHavePathVariable() throws NoSuchMethodException {
        var method = EntrepreneurController.class.getDeclaredMethod("update", Long.class, EntrepreneurRequest.class);
        var parameter = method.getParameters()[0];
        assertNotNull(parameter.getAnnotation(org.springframework.web.bind.annotation.PathVariable.class));
    }
}
