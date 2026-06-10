package br.edu.ifgoiano.academico.sd_academico_turma_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Dados para criação de uma turma")
public class TurmaRequestDTO {

    @Schema(description = "ID da disciplina (deve existir no disciplina-service)", example = "1")
    private Long disciplinaId;

    @Schema(description = "Código único da turma", example = "ADS-2026-2")
    private String codigoTurma;

    @Schema(description = "Semestre da oferta", example = "2026/2")
    private String semestre;

    @Schema(description = "Professor responsável", example = "Carlos Silva")
    private String professor;

    @Schema(description = "Total de vagas da turma", example = "40")
    private Integer vagasTotal;

    @Schema(description = "Vagas já ocupadas (opcional; padrão 0)", example = "0")
    private Integer vagasOcupadas;
}
