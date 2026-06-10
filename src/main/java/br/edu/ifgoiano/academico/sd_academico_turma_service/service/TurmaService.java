package br.edu.ifgoiano.academico.sd_academico_turma_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifgoiano.academico.sd_academico_turma_service.client.DisciplinaClient;
import br.edu.ifgoiano.academico.sd_academico_turma_service.dto.TurmaRequestDTO;
import br.edu.ifgoiano.academico.sd_academico_turma_service.dto.TurmaResponseDTO;
import br.edu.ifgoiano.academico.sd_academico_turma_service.entity.Turma;
import br.edu.ifgoiano.academico.sd_academico_turma_service.repository.TurmaRepository;

import feign.FeignException;

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
    private final DisciplinaClient disciplinaClient;

    // Injeção de dependência
    public TurmaService(TurmaRepository repository, DisciplinaClient disciplinaClient) {
        this.repository = repository;
        this.disciplinaClient = disciplinaClient;
    }

    /**
     * Salvar nova turma
     * Define valores padrão se não informados
     *
     * @param turma Turma a ser salva
     * @return Turma salva com ID gerado
     */
    public TurmaResponseDTO salvar(TurmaRequestDTO request) {

        // Garante que a disciplina informada realmente existe antes de criar a turma
        validarDisciplina(request.getDisciplinaId());

        Turma turma = new Turma();
        turma.setDisciplinaId(request.getDisciplinaId());
        turma.setCodigoTurma(request.getCodigoTurma());
        turma.setSemestre(request.getSemestre());
        turma.setProfessor(request.getProfessor());
        turma.setVagasTotal(request.getVagasTotal());

        // Inicializar vagas ocupadas se não informadas
        turma.setVagasOcupadas(request.getVagasOcupadas() != null ? request.getVagasOcupadas() : 0);

        // Define o status de acordo com a quantidade de vagas
        atualizarStatus(turma);

        return paraResponse(repository.save(turma));
    }

    /**
     * Valida, junto ao disciplina-service, se a disciplina informada existe.
     *
     * @throws IllegalArgumentException se a disciplina não for informada ou não existir
     * @throws IllegalStateException    se o disciplina-service estiver indisponível
     */
    private void validarDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("O id da disciplina é obrigatório.");
        }

        try {
            disciplinaClient.buscarPorId(disciplinaId);
        } catch (FeignException.NotFound naoEncontrada) {
            throw new IllegalArgumentException(
                    "Disciplina " + disciplinaId + " não existe.");
        } catch (FeignException indisponivel) {
            logger.error("[TURMA-SERVICE] Falha ao consultar o disciplina-service para a disciplina {}: {}",
                    disciplinaId, indisponivel.getMessage());
            throw new IllegalStateException(
                    "Não foi possível validar a disciplina no momento. Tente novamente em instantes.");
        }
    }

    /**
     * Listar todas as turmas
     *
     * @return Lista de turmas
     */
    public List<TurmaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::paraResponse)
                .toList();
    }

    /**
     * Buscar turma por ID
     *
     * @param id ID da turma
     * @return Optional contendo a turma (como DTO) se encontrada
     */
    public Optional<TurmaResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::paraResponse);
    }

    /**
     * Reservar uma vaga na turma (via gRPC).
     *
     * A reserva é feita com um UPDATE condicional ATÔMICO no banco
     * (incrementa vagasOcupadas somente se ainda houver vaga), o que evita
     * "lost update" quando há reservas concorrentes.
     *
     * @param turmaId ID da turma
     * @return true se conseguiu reservar, false caso contrário
     */
    @Transactional
    public boolean reservarVaga(Long turmaId) {
        logger.info("[TURMA-SERVICE] Reservando vaga para turma ID: {}", turmaId);

        int linhasAfetadas = repository.reservarVagaAtomico(turmaId);

        if (linhasAfetadas == 0) {
            if (!repository.existsById(turmaId)) {
                logger.warn("[TURMA-SERVICE] Turma ID {} não encontrada", turmaId);
            } else {
                logger.warn("[TURMA-SERVICE] Turma ID {} sem vagas disponíveis", turmaId);
            }
            return false;
        }

        // Atualiza o status (LOTADA/DISPONIVEL) de acordo com a nova ocupação
        repository.sincronizarStatus(turmaId);

        logger.info("[TURMA-SERVICE] Vaga reservada para turma ID: {}", turmaId);
        return true;
    }

    /**
     * Liberar uma vaga na turma (via gRPC).
     *
     * Usa um UPDATE condicional ATÔMICO (decrementa somente se houver vaga
     * ocupada), evitando contagem negativa e condições de corrida.
     *
     * @param turmaId ID da turma
     * @return true se conseguiu liberar, false caso contrário
     */
    @Transactional
    public boolean liberarVaga(Long turmaId) {
        logger.info("[TURMA-SERVICE] Liberando vaga para turma ID: {}", turmaId);

        int linhasAfetadas = repository.liberarVagaAtomico(turmaId);

        if (linhasAfetadas == 0) {
            if (!repository.existsById(turmaId)) {
                logger.warn("[TURMA-SERVICE] Turma ID {} não encontrada", turmaId);
            } else {
                logger.warn("[TURMA-SERVICE] Nenhuma vaga para liberar na turma ID: {}", turmaId);
            }
            return false;
        }

        // Ao liberar uma vaga, a turma pode voltar a ficar disponível
        repository.sincronizarStatus(turmaId);

        logger.info("[TURMA-SERVICE] Vaga liberada para turma ID: {}", turmaId);
        return true;
    }

    /**
     * Converte a entidade Turma no DTO de resposta exposto pela API.
     */
    private TurmaResponseDTO paraResponse(Turma turma) {
        TurmaResponseDTO response = new TurmaResponseDTO();
        response.setId(turma.getId());
        response.setDisciplinaId(turma.getDisciplinaId());
        response.setCodigoTurma(turma.getCodigoTurma());
        response.setSemestre(turma.getSemestre());
        response.setProfessor(turma.getProfessor());
        response.setVagasTotal(turma.getVagasTotal());
        response.setVagasOcupadas(turma.getVagasOcupadas());
        response.setStatus(turma.getStatus());
        return response;
    }

    /**
     * Atualiza o status da turma de acordo com a quantidade de vagas (uso na criação).
     */
    private void atualizarStatus(Turma turma) {

        if (turma.getVagasOcupadas() >= turma.getVagasTotal()) {
            turma.setStatus("LOTADA");
        } else {
            turma.setStatus("DISPONIVEL");
        }
    }

}
