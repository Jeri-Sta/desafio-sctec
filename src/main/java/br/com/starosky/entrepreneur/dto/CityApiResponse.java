package br.com.starosky.entrepreneur.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityApiResponse {

    private Long ibgeCode;
    private String name;

    @JsonProperty("microrregiao")
    private MicroRegion microRegion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MicroRegion {
        private Long id;
        private String name;

        @JsonProperty("mesorregiao")
        private MesoRegion mesoRegion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MesoRegion {
        private Long id;
        private String name;

        @JsonProperty("UF")
        private State state;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private Long id;
        private String name;
        private String abbreviation;
    }
}
