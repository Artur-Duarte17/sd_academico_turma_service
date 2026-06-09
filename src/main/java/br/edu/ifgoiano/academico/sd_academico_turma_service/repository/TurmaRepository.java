package br.edu.ifgoiano.academico.sd_academico_turma_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifgoiano.academico.sd_academico_turma_service.entity.Turma;

/**
 * Repositório de Turmas
 * 
 * Fornece acesso aos dados de turmas no banco H2
 * Suporta operações básicas CRUD herdadas de JpaRepository
 */
public interface TurmaRepository extends JpaRepository<Turma, Long> {
}
