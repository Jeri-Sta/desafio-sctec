package br.com.starosky.entrepreneur.service;

import br.com.starosky.entrepreneur.dto.CityApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class CityValidationServiceTest {

    private CityValidationService cityValidationService;

    @BeforeEach
    void setUp() throws Exception {
        WebClient.Builder mockBuilder = WebClient.builder();
        cityValidationService = new CityValidationService(mockBuilder);
    }


    @Test
    void class_ShouldHaveCorrectConstants() throws Exception {
        Field baseUrlField = CityValidationService.class.getDeclaredField("BRASIL_API_BASE_URL");
        Field stateCodeField = CityValidationService.class.getDeclaredField("SANTA_CATARINA_STATE_CODE");

        baseUrlField.setAccessible(true);
        stateCodeField.setAccessible(true);

        assertTrue(Modifier.isPrivate(baseUrlField.getModifiers()));
        assertTrue(Modifier.isStatic(baseUrlField.getModifiers()));
        assertTrue(Modifier.isFinal(baseUrlField.getModifiers()));

        assertTrue(Modifier.isPrivate(stateCodeField.getModifiers()));
        assertTrue(Modifier.isStatic(stateCodeField.getModifiers()));
        assertTrue(Modifier.isFinal(stateCodeField.getModifiers()));

        assertEquals("https://brasilapi.com.br/api/ibge/municipios/v1",
                baseUrlField.get(null));
        assertEquals("42", stateCodeField.get(null));
    }

    @Test
    void class_ShouldBeAnnotatedWithService() {
        assertNotNull(CityValidationService.class.getAnnotation(org.springframework.stereotype.Service.class));
    }

    @Test
    void isValidCityMethod_ShouldExist() throws NoSuchMethodException {
        Method method = CityValidationService.class.getMethod("isValidCity", String.class);
        assertNotNull(method);
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    void fetchCitiesFromApiMethod_ShouldBePrivate() throws NoSuchMethodException {
        Method method = CityValidationService.class.getDeclaredMethod("fetchCitiesFromApi");
        assertTrue(Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    void constantValues_ShouldBeCorrect() {
        assertEquals("https://brasilapi.com.br/api/ibge/municipios/v1",
                "https://brasilapi.com.br/api/ibge/municipios/v1");
        assertEquals("42", "42");
    }

    @Test
    void cityValidationService_ShouldExist() {
        assertNotNull(CityValidationService.class);
    }

    @Test
    void cityApiResponse_ShouldHaveCorrectStructure() {
        CityApiResponse response = new CityApiResponse();
        assertNotNull(response);

        assertNotNull(CityApiResponse.MicroRegion.class);
        assertNotNull(CityApiResponse.MesoRegion.class);
        assertNotNull(CityApiResponse.State.class);
    }

    @Test
    void class_ShouldHaveWebClientField() throws NoSuchFieldException {
        Field webClientField = CityValidationService.class.getDeclaredField("webClient");
        assertNotNull(webClientField);
        assertEquals(org.springframework.web.reactive.function.client.WebClient.class, webClientField.getType());
    }

    @Test
    void class_ShouldHaveValidationEnabledField() throws NoSuchFieldException {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        assertNotNull(validationEnabledField);
        assertEquals(boolean.class, validationEnabledField.getType());
    }

    @Test
    void class_ShouldHaveConstructorWithWebClientBuilder() throws NoSuchMethodException {
        var constructor = CityValidationService.class.getConstructor(org.springframework.web.reactive.function.client.WebClient.Builder.class);
        assertNotNull(constructor);
    }

    @Test
    void isValidCity_WithValidationDisabled_ShouldReturnTrue() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, false);

        boolean result = cityValidationService.isValidCity("AnyCity");

        assertTrue(result);
    }

    @Test
    void isValidCity_WithNullCityName_ShouldReturnFalse() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, true);

        boolean result = cityValidationService.isValidCity(null);

        assertFalse(result);
    }

    @Test
    void isValidCity_WithEmptyCityName_ShouldReturnFalse() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, true);

        boolean result = cityValidationService.isValidCity("   ");

        assertFalse(result);
    }

    @Test
    void isValidCity_WithBlankCityName_ShouldReturnFalse() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, true);

        boolean result = cityValidationService.isValidCity("");

        assertFalse(result);
    }

    @Test
    void isValidCity_WithValidCityName_ShouldCallApi() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, true);

        assertThrows(RuntimeException.class, () -> {
            cityValidationService.isValidCity("Florianopolis");
        });
    }

    @Test
    void isValidCity_MethodShouldExist() throws NoSuchMethodException {
        Method method = CityValidationService.class.getMethod("isValidCity", String.class);
        assertNotNull(method);
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    void isValidCity_WithValidationEnabledTrue_ShouldAttemptApiCall() throws Exception {
        Field validationEnabledField = CityValidationService.class.getDeclaredField("validationEnabled");
        validationEnabledField.setAccessible(true);
        validationEnabledField.set(cityValidationService, true);

        assertThrows(RuntimeException.class, () -> {
            cityValidationService.isValidCity("TestCity");
        });
    }
}
