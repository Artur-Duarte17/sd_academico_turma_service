package br.edu.ifgoiano.academico.sd_academico_turma_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifgoiano.academico.sd_academico_turma_service.entity.Turma;
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
    public ResponseEntity<Turma> criar(@RequestBody Turma turma) {
        logger.info("[TURMA-SERVICE] Criando turma: {}", turma.getCodigoTurma());
        Turma turmaSalva = service.salvar(turma);
        logger.info("[TURMA-SERVICE] Turma criada com ID: {}", turmaSalva.getId());
        return ResponseEntity.ok(turmaSalva);
    }

    /**
     * Listar todas as turmas
     * @return Lista de turmas
     */
    @GetMapping
    public ResponseEntity<List<Turma>> listar() {
        logger.info("[TURMA-SERVICE] Listando todas as turmas");
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Buscar turma por ID
     * @param id ID da turma
     * @return Turma encontrada ou 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        logger.info("[TURMA-SERVICE] Buscando turma ID: {}", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
