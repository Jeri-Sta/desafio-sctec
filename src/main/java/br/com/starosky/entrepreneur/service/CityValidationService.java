package br.com.starosky.entrepreneur.service;

import br.com.starosky.entrepreneur.dto.CityApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CityValidationService {

    private static final String BRASIL_API_BASE_URL = "https://brasilapi.com.br/api/ibge/municipios/v1";
    private static final String SANTA_CATARINA_STATE_CODE = "42";

    private final WebClient webClient;

    @Value("${app.city-validation.enabled:true}")
    private boolean validationEnabled;

    public CityValidationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BRASIL_API_BASE_URL).build();
    }

    /**
     * Valida se a cidade pertence ao estado de Santa Catarina (código 42).
     *
     * @param cityName Nome da cidade a ser validada
     * @return true se a cidade é válida (pertence a Santa Cataria), false caso contrário
     */
    public boolean isValidCity(String cityName) {
        if (!validationEnabled) {
            return true;
        }

        if (cityName == null || cityName.trim().isEmpty()) {
            return false;
        }

        try {
            List<CityApiResponse> cities = fetchCitiesFromApi();
            return cities.stream()
                    .anyMatch(city -> city.getName().equalsIgnoreCase(cityName.trim()));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao validar a cidade: " + e.getMessage(), e);
        }
    }

    /**
     * Busca todas as cidades de Santa Cataria do BrasilAPI.
     *
     * @return lista de cidades de Santa Catarina
     */
    private List<CityApiResponse> fetchCitiesFromApi() {
        Mono<List<CityApiResponse>> response = webClient.get()
                .uri("/" + SANTA_CATARINA_STATE_CODE)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<CityApiResponse>>() {});

        return response.block();
    }
}
