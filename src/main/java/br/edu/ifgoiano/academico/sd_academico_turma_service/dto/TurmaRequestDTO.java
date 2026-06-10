package br.edu.ifgoiano.academico.sd_academico_turma_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dados de entrada para criação de uma turma.
 * vagasOcupadas é opcional; quando não informado, assume 0.
 * O status é derivado da ocupação e não é informado pelo cliente.
 */
@Getter
@Setter
@NoArgsConstructor
public class TurmaRequestDTO {
    private Long disciplinaId;
    private String codigoTurma;
    private String semestre;
    private String professor;
    private Integer vagasTotal;
    private Integer vagasOcupadas;
}
