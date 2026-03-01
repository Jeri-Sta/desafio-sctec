package br.com.starosky.entrepreneur.controller;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.service.EntrepreneurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entrepreneurs")
@RequiredArgsConstructor
public class EntrepreneurController {

    private final EntrepreneurService entrepreneurService;

    /**
     * Cria um novo empreendedor.
     *
     * @param request Dados do empreendedor
     * @return Dados do empreendedor criado
     */
    @PostMapping
    public ResponseEntity<EntrepreneurResponse> create(@Valid @RequestBody EntrepreneurRequest request) {
        EntrepreneurResponse response = entrepreneurService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca um empreendedor pelo ID.
     *
     * @param id ID do empreendedor
     * @return Dados do empreendedor encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntrepreneurResponse> findById(@PathVariable Long id) {
        return entrepreneurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os empreendedores com paginação.
     *
     * @param pageable Parâmetros de paginação
     * @return Página de empreendedores
     */
    @GetMapping
    public ResponseEntity<Page<EntrepreneurResponse>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<EntrepreneurResponse> page = entrepreneurService.findAll(pageable);
        return ResponseEntity.ok(page);
    }
}
