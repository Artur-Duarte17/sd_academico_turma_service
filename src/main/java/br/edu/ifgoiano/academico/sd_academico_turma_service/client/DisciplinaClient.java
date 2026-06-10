package br.edu.ifgoiano.academico.sd_academico_turma_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente HTTP para o disciplina-service.
 *
 * Usado para validar, ao criar uma turma, se a disciplina informada realmente
 * existe (evita turmas órfãs apontando para disciplinas inexistentes).
 *
 * O nome "disciplina-service" corresponde ao spring.application.name do
 * microsserviço de disciplinas registrado no Eureka.
 */
@FeignClient(name = "disciplina-service")
public interface DisciplinaClient {

    /**
     * Consulta a disciplina por ID.
     *
     * Só nos interessa o resultado HTTP: 200 (existe) ou 404 (não existe, o Feign
     * lança {@code FeignException.NotFound}). O corpo da resposta é ignorado.
     *
     * @param id identificador da disciplina
     */
    @GetMapping("/disciplinas/{id}")
    void buscarPorId(@PathVariable("id") Long id);
}
