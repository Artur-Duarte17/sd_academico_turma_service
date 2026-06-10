package br.edu.ifgoiano.academico.sd_academico_turma_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Serviço de Turmas - Parte do Sistema Acadêmico Distribuído
 * 
 * Responsabilidades:
 * - Gerenciar turmas de disciplinas
 * - Controlar vagas disponíveis
 * - Expor serviço gRPC para reservar/liberar vagas
 * - Registrar-se no Eureka como descoberta de serviços
 * 
 * Portas:
 * - HTTP: 8083
 * - gRPC: 9093
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SdAcademicoTurmaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdAcademicoTurmaServiceApplication.class, args);
	}

}
