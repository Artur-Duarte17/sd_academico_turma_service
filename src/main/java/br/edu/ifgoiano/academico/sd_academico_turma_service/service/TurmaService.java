package br.edu.ifgoiano.academico.sd_academico_turma_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifgoiano.academico.sd_academico_turma_service.entity.Turma;
import br.edu.ifgoiano.academico.sd_academico_turma_service.repository.TurmaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de Turmas
 * 
 * Responsabilidades:
 * - Gerenciar CRUD de turmas
 * - Controlar vagas (reservar e liberar)
 * - Validações de negócio
 */
@Service
public class TurmaService {

    private static final Logger logger = LoggerFactory.getLogger(TurmaService.class);

    private final TurmaRepository repository;

    // Injeção de dependência
    public TurmaService(TurmaRepository repository) {
        this.repository = repository;
    }

    /**
     * Salvar nova turma
     * Define valores padrão se não informados
     * @param turma Turma a ser salva
     * @return Turma salva com ID gerado
     */
    public Turma salvar(Turma turma) {
        
        // Inicializar vagas ocupadas se não informadas
        if (turma.getVagasOcupadas() == null) {
            turma.setVagasOcupadas(0);
        }

        // Inicializar status se não informado
        if (turma.getStatus() == null) {
            turma.setStatus("ATIVA");
        }

        return repository.save(turma);
    }

    /**
     * Listar todas as turmas
     * @return Lista de turmas
     */
    public List<Turma> listar() {
        return repository.findAll();
    }

    /**
     * Buscar turma por ID
     * @param id ID da turma
     * @return Optional contendo a turma se encontrada
     */
    public Optional<Turma> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Reservar uma vaga na turma (via gRPC)
     * Verifica se há vagas disponíveis antes de incrementar
     * @param turmaId ID da turma
     * @return true se conseguiu reservar, false caso contrário
     */
    @Transactional
    public boolean reservarVaga(Long turmaId) {
        logger.info("[TURMA-SERVICE] Reservando vaga para turma ID: {}", turmaId);

        Optional<Turma> optionalTurma = repository.findById(turmaId);

        if (optionalTurma.isEmpty()) {
            logger.warn("[TURMA-SERVICE] Turma ID {} não encontrada", turmaId);
            return false;
        }

        Turma turma = optionalTurma.get();

        if (turma.getVagasOcupadas() >= turma.getVagasTotal()) {
            logger.warn("[TURMA-SERVICE] Turma ID {} sem vagas disponíveis ({}/{})",
                    turmaId, turma.getVagasOcupadas(), turma.getVagasTotal());
            return false;
        }

        turma.setVagasOcupadas(turma.getVagasOcupadas() + 1);

        if (turma.getVagasOcupadas() >= turma.getVagasTotal()) {
            turma.setStatus("LOTADA");
        }

        repository.save(turma);

        logger.info("[TURMA-SERVICE] Vaga reservada para turma ID: {} | Vagas: {}/{}",
                turmaId, turma.getVagasOcupadas(), turma.getVagasTotal());

        return true;
    }

    /**
     * Liberar uma vaga na turma (via gRPC)
     * Verifica se há vagas ocupadas antes de decrementar
     * @param turmaId ID da turma
     * @return true se conseguiu liberar, false caso contrário
     */
    @Transactional
    public boolean liberarVaga(Long turmaId) {
        logger.info("[TURMA-SERVICE] Liberando vaga para turma ID: {}", turmaId);

        Optional<Turma> optionalTurma = repository.findById(turmaId);

        if (optionalTurma.isEmpty()) {
            logger.warn("[TURMA-SERVICE] Turma ID {} não encontrada", turmaId);
            return false;
        }

        Turma turma = optionalTurma.get();

        if (turma.getVagasOcupadas() <= 0) {
            logger.warn("[TURMA-SERVICE] Nenhuma vaga para liberar na turma ID: {}", turmaId);
            return false;
        }

        turma.setVagasOcupadas(turma.getVagasOcupadas() - 1);
        turma.setStatus("ATIVA");

        repository.save(turma);

        logger.info("[TURMA-SERVICE] Vaga liberada para turma ID: {} | Vagas: {}/{}",
                turmaId, turma.getVagasOcupadas(), turma.getVagasTotal());

        return true;
    }
}
