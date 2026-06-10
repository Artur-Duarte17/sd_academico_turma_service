package br.edu.ifgoiano.academico.sd_academico_turma_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dados de saída ao expor uma turma pela API.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Representação de uma turma retornada pela API")
public class TurmaResponseDTO {

    @Schema(description = "Identificador da turma", example = "1")
    private Long id;

    @Schema(description = "ID da disciplina vinculada", example = "1")
    private Long disciplinaId;

    @Schema(description = "Código único da turma", example = "ADS-2026-2")
    private String codigoTurma;

    @Schema(description = "Semestre da oferta", example = "2026/2")
    private String semestre;

    @Schema(description = "Professor responsável", example = "Carlos Silva")
    private String professor;

    @Schema(description = "Total de vagas da turma", example = "40")
    private Integer vagasTotal;

    @Schema(description = "Vagas já ocupadas", example = "5")
    private Integer vagasOcupadas;

    @Schema(description = "Status da turma (DISPONIVEL/LOTADA)", example = "DISPONIVEL")
    private String status;
}
