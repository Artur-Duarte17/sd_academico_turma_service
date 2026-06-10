package br.edu.ifgoiano.academico.sd_academico_turma_service.service;

import br.edu.ifgoiano.grpc.*;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementação do Serviço gRPC de Turmas
 * 
 * Fornece operações síncronas para:
 * - Reservar vagas em uma turma
 * - Liberar vagas de uma turma
 * 
 * Utilizado pelo Serviço de Matrículas para validar disponibilidade
 * Porto gRPC: 9093
 */
@GrpcService
public class TurmaGrpcServiceImpl extends TurmaGrpcServiceGrpc.TurmaGrpcServiceImplBase {

        private static final Logger logger = LoggerFactory.getLogger(TurmaGrpcServiceImpl.class);

        private final TurmaService turmaService;

        // Injeção de dependência
        public TurmaGrpcServiceImpl(TurmaService turmaService) {
                this.turmaService = turmaService;
        }

        /**
         * RPC - Reservar uma vaga na turma
         * Chamado pelo Serviço de Matrículas como parte do fluxo de criação
         * 
         * @param request Contém turmaId
         * @param responseObserver Callback para enviar resposta
         */
        @Override
        public void reservarVaga(
                        ReservaVagaRequest request,
                        StreamObserver<ReservaVagaResponse> responseObserver) {

                Long turmaId = request.getTurmaId();
                logger.info("[TURMA-SERVICE] gRPC ReservarVaga chamado para turma ID: {}", turmaId);

                boolean sucesso = turmaService.reservarVaga(turmaId);

                ReservaVagaResponse response = ReservaVagaResponse.newBuilder()
                                .setSucesso(sucesso)
                                .setMensagem(
                                                sucesso
                                                                ? "Vaga reservada com sucesso"
                                                                : "Falha ao reservar vaga - sem vagas disponíveis ou turma não encontrada")
                                .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();

                logger.info("[TURMA-SERVICE] gRPC ReservarVaga resultado: {}", sucesso);
        }

        /**
         * RPC - Liberar uma vaga na turma
         * Chamado pelo Serviço de Matrículas quando cancelamento é realizado
         * 
         * @param request Contém turmaId
         * @param responseObserver Callback para enviar resposta
         */
        @Override
        public void liberarVaga(
                        LiberaVagaRequest request,
                        StreamObserver<LiberaVagaResponse> responseObserver) {

                Long turmaId = request.getTurmaId();
                logger.info("[TURMA-SERVICE] gRPC LiberarVaga chamado para turma ID: {}", turmaId);

                boolean sucesso = turmaService.liberarVaga(turmaId);

                LiberaVagaResponse response = LiberaVagaResponse.newBuilder()
                                .setSucesso(sucesso)
                                .setMensagem(
                                                sucesso
                                                                ? "Vaga liberada com sucesso"
                                                                : "Falha ao liberar vaga - turma não encontrada ou sem vagas a liberar")
                                .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();

                logger.info("[TURMA-SERVICE] gRPC LiberarVaga resultado: {}", sucesso);
        }
}
