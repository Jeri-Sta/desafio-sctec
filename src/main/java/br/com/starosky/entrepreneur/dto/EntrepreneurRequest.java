package br.com.starosky.entrepreneur.dto;

import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepreneurRequest {

    @NotBlank(message = "O nome do empreendimento é obrigatório")
    @Size(max = 255, message = "O nome da empresa não pode exceder 255 caracteres")
    private String enterpriseName;

    @NotBlank(message = "O nome do empreendedor é obrigatório")
    @Size(max = 255, message = "O nome do empreendedor não pode exceder 255 caracteres")
    private String entrepreneurName;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 255, message = "A cidade não pode exceder 255 caracteres")
    private String city;

    @NotNull(message = "O segmento de atuação é obrigatório")
    private OperatingSegment operatingSegment;

    @NotBlank(message = "O contato é obrigatório")
    @Size(max = 255, message = "O contato não pode exceder 255 caracteres")
    private String contact;

    private Status status;
}
