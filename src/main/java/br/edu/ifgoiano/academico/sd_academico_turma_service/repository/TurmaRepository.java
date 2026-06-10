package br.edu.ifgoiano.academico.sd_academico_turma_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifgoiano.academico.sd_academico_turma_service.entity.Turma;

/**
 * Repositório de Turmas
 *
 * Fornece acesso aos dados de turmas no banco H2.
 * Suporta operações básicas CRUD herdadas de JpaRepository.
 *
 * Inclui ainda operações ATÔMICAS de reserva/liberação de vaga: o controle de
 * vagas é feito diretamente no banco (UPDATE condicional), evitando o problema
 * de "lost update" que ocorreria com o padrão ler-modificar-salvar quando há
 * reservas concorrentes.
 */
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    /**
     * Reserva uma vaga de forma atômica: só incrementa se ainda houver vaga.
     *
     * @return 1 se a vaga foi reservada; 0 se não havia vaga ou a turma não existe.
     */
    @Modifying
    @Query("UPDATE Turma t SET t.vagasOcupadas = t.vagasOcupadas + 1 "
            + "WHERE t.id = :id AND t.vagasOcupadas < t.vagasTotal")
    int reservarVagaAtomico(@Param("id") Long id);

    /**
     * Libera uma vaga de forma atômica: só decrementa se houver vaga ocupada.
     *
     * @return 1 se a vaga foi liberada; 0 se não havia vaga ocupada ou a turma não existe.
     */
    @Modifying
    @Query("UPDATE Turma t SET t.vagasOcupadas = t.vagasOcupadas - 1 "
            + "WHERE t.id = :id AND t.vagasOcupadas > 0")
    int liberarVagaAtomico(@Param("id") Long id);

    /**
     * Sincroniza o status da turma de acordo com a ocupação atual.
     */
    @Modifying
    @Query("UPDATE Turma t SET t.status = "
            + "CASE WHEN t.vagasOcupadas >= t.vagasTotal THEN 'LOTADA' ELSE 'DISPONIVEL' END "
            + "WHERE t.id = :id")
    int sincronizarStatus(@Param("id") Long id);
}
