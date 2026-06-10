package br.edu.ifgoiano.academico.sd_academico_turma_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingRabbitMQConfig {

    @Bean
    public Declarables loggingDeclarables() {
        TopicExchange logsExchange = new TopicExchange("logs.academico", true, false);
        Queue logsQueue = new Queue("logs.all", true);
        Binding logsBinding = BindingBuilder.bind(logsQueue).to(logsExchange).with("logs.#");
        return new Declarables(logsExchange, logsQueue, logsBinding);
    }
}
