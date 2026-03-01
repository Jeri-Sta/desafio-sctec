package br.com.starosky.entrepreneur.controller;

import br.com.starosky.entrepreneur.dto.EntrepreneurRequest;
import br.com.starosky.entrepreneur.dto.EntrepreneurResponse;
import br.com.starosky.entrepreneur.service.EntrepreneurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
