package br.com.starosky.entrepreneur.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityApiResponse {

    @JsonProperty("codigo_ibge")
    private Long ibgeCode;
    
    @JsonProperty("nome")
    private String name;
}
