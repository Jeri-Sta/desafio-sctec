package br.com.starosky.entrepreneur.dto;

import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;
import br.com.starosky.entrepreneur.entity.Entrepreneur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepreneurResponse {

    private Long id;
    private String enterpriseName;
    private String entrepreneurName;
    private String city;
    private OperatingSegment operatingSegment;
    private String contact;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EntrepreneurResponse fromEntity(Entrepreneur entrepreneur) {
        return EntrepreneurResponse.builder()
                .id(entrepreneur.getId())
                .enterpriseName(entrepreneur.getEnterpriseName())
                .entrepreneurName(entrepreneur.getEntrepreneurName())
                .city(entrepreneur.getCity())
                .operatingSegment(entrepreneur.getOperatingSegment())
                .contact(entrepreneur.getContact())
                .status(entrepreneur.getStatus())
                .createdAt(entrepreneur.getCreatedAt())
                .updatedAt(entrepreneur.getUpdatedAt())
                .build();
    }
}
