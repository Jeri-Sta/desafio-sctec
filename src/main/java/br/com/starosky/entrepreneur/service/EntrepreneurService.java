package br.com.starosky.entrepreneur.service;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.entity.Entrepreneur;
import br.com.starosky.entrepreneur.enums.Status;
import br.com.starosky.entrepreneur.exception.ValidationException;
import br.com.starosky.entrepreneur.repository.EntrepreneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntrepreneurService {

    private final EntrepreneurRepository entrepreneurRepository;
    private final CityValidationService cityValidationService;

    /**
     * Cria um novo empreendedor.
     *
     * @param request dados do empreendedor
     * @return dados do empreendedor criado
     */
    @Transactional
    public EntrepreneurResponse create(EntrepreneurRequest request) {
        validateRequest(request);

        Entrepreneur entrepreneur = Entrepreneur.builder()
                .enterpriseName(request.getEnterpriseName())
                .entrepreneurName(request.getEntrepreneurName())
                .city(request.getCity())
                .operatingSegment(request.getOperatingSegment())
                .contact(request.getContact())
                .status(request.getStatus() != null ? request.getStatus() : Status.ACTIVE)
                .build();

        Entrepreneur saved = entrepreneurRepository.save(entrepreneur);
        return EntrepreneurResponse.fromEntity(saved);
    }

    /**
     * Valida os dados do empreendedor
     *
     * @param request dados a serem validados
     * @throws ValidationException se a validação falhar
     */
    private void validateRequest(EntrepreneurRequest request) {
        if (!cityValidationService.isValidCity(request.getCity())) {
            throw new ValidationException("A cidade deve ser um munícipio pertencente á Santa Catarina");
        }
    }
}
