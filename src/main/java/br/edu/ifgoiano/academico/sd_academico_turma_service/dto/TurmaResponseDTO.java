package br.edu.ifgoiano.academico.sd_academico_turma_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dados de saída ao expor uma turma pela API.
 */
@Getter
@Setter
@NoArgsConstructor
public class TurmaResponseDTO {
    private Long id;
    private Long disciplinaId;
    private String codigoTurma;
    private String semestre;
    private String professor;
    private Integer vagasTotal;
    private Integer vagasOcupadas;
    private String status;
}
