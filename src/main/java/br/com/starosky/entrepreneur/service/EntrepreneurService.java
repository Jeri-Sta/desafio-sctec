package br.com.starosky.entrepreneur.service;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.entity.Entrepreneur;
import br.com.starosky.entrepreneur.enums.Status;
import br.com.starosky.entrepreneur.exception.ValidationException;
import br.com.starosky.entrepreneur.repository.EntrepreneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * Busca um empreendedor pelo ID.
     *
     * @param id ID do empreendedor
     * @return dados do empreendedor encontrado
     */
    @Transactional(readOnly = true)
    public Optional<EntrepreneurResponse> findById(Long id) {
        return entrepreneurRepository.findById(id)
                .map(EntrepreneurResponse::fromEntity);
    }

    /**
     * Lista todos os empreendedores com paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de empreendedores
     */
    @Transactional(readOnly = true)
    public Page<EntrepreneurResponse> findAll(Pageable pageable) {
        return entrepreneurRepository.findAll(pageable)
                .map(EntrepreneurResponse::fromEntity);
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

    /**
     * Atualiza um empreendedor existente.
     *
     * @param id ID do empreendedor a ser atualizado
     * @param request novos dados do empreendedor
     * @return dados do empreendedor atualizado
     * @throws ValidationException se o empreendedor não for encontrado
     */
    @Transactional
    public EntrepreneurResponse update(Long id, EntrepreneurRequest request) {
        Entrepreneur entrepreneur = entrepreneurRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Empreendedor não encontrado"));

        validateRequest(request);

        entrepreneur.setEnterpriseName(request.getEnterpriseName());
        entrepreneur.setEntrepreneurName(request.getEntrepreneurName());
        entrepreneur.setCity(request.getCity());
        entrepreneur.setOperatingSegment(request.getOperatingSegment());
        entrepreneur.setContact(request.getContact());
        if (request.getStatus() != null) {
            entrepreneur.setStatus(request.getStatus());
        }

        Entrepreneur updated = entrepreneurRepository.save(entrepreneur);
        return EntrepreneurResponse.fromEntity(updated);
    }
}
