package br.edu.ifgoiano.academico.sd_academico_turma_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifgoiano.academico.sd_academico_turma_service.dto.TurmaRequestDTO;
import br.edu.ifgoiano.academico.sd_academico_turma_service.dto.TurmaResponseDTO;
import br.edu.ifgoiano.academico.sd_academico_turma_service.service.TurmaService;

import java.util.List;

/**
 * Controller REST para Turmas
 *
 * Endpoints:
 * - POST /turmas - Criar nova turma
 * - GET /turmas - Listar todas as turmas
 * - GET /turmas/{id} - Buscar turma por ID
 */
@RestController
@RequestMapping("/turmas")
@Tag(name = "Turmas", description = "Cadastro, consulta e controle de vagas de turmas")
public class TurmaController {

    private static final Logger logger = LoggerFactory.getLogger(TurmaController.class);

    private final TurmaService service;

    // Injeção de dependência do serviço de turmas
    public TurmaController(TurmaService service) {
        this.service = service;
    }

    /**
     * Criar uma nova turma
     * @param turma Dados da turma a ser criada
     * @return Turma criada com ID gerado
     */
    @PostMapping
    @Operation(summary = "Criar turma",
            description = "Cria uma nova turma. A disciplina informada precisa existir no disciplina-service.")
    public ResponseEntity<?> criar(@RequestBody TurmaRequestDTO request) {
        logger.info("[TURMA-SERVICE] Criando turma: {}", request.getCodigoTurma());
        try {
            TurmaResponseDTO turmaSalva = service.salvar(request);
            logger.info("[TURMA-SERVICE] Turma criada com ID: {}", turmaSalva.getId());
            return ResponseEntity.ok(turmaSalva);
        } catch (IllegalArgumentException dadosInvalidos) {
            // disciplina inexistente ou não informada
            return ResponseEntity.badRequest().body(dadosInvalidos.getMessage());
        } catch (IllegalStateException servicoIndisponivel) {
            // disciplina-service fora do ar
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(servicoIndisponivel.getMessage());
        }
    }

    /**
     * Listar todas as turmas
     * @return Lista de turmas
     */
    @GetMapping
    @Operation(summary = "Listar turmas", description = "Retorna todas as turmas cadastradas.")
    public ResponseEntity<List<TurmaResponseDTO>> listar() {
        logger.info("[TURMA-SERVICE] Listando todas as turmas");
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Buscar turma por ID
     * @param id ID da turma
     * @return Turma encontrada ou 404
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar turma por ID", description = "Retorna a turma com o ID informado, ou 404 se não existir.")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID da turma", example = "1") @PathVariable Long id) {
        logger.info("[TURMA-SERVICE] Buscando turma ID: {}", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
