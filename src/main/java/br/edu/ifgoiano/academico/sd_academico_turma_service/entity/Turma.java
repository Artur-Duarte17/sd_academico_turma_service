package br.edu.ifgoiano.academico.sd_academico_turma_service.entity;

import jakarta.persistence.*;

/**
 * Entidade de Turma
 * 
 * Representa uma turma de uma disciplina com informações sobre:
 * - Vínculo com disciplina
 * - Código e semestre
 * - Professor responsável
 * - Controle de vagas (total e ocupadas)
 * - Status da turma
 */
@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID da disciplina a que a turma pertence
    private Long disciplinaId;

    // Código único da turma (ex: ADS-2026-1)
    private String codigoTurma;

    // Semestre oferecido (ex: 2026/1)
    private String semestre;

    // Nome do professor responsável
    private String professor;

    // Total de vagas disponíveis
    private Integer vagasTotal;

    // Número de vagas já ocupadas
    private Integer vagasOcupadas;

    // Status: ATIVA, LOTADA, CANCELADA
    private String status;

    // Construtor padrão
    public Turma() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Integer getVagasTotal() {
        return vagasTotal;
    }

    public void setVagasTotal(Integer vagasTotal) {
        this.vagasTotal = vagasTotal;
    }

    public Integer getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void setVagasOcupadas(Integer vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
