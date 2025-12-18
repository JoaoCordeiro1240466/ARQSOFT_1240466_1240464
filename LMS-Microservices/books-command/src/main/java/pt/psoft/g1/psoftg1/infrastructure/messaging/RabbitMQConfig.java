package pt.psoft.g1.psoftg1.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("library-events-exchange");
    }

    @Bean
    public Queue authorValidationQueue() {
        return new Queue("books-author-validation-queue");
    }

    @Bean
    public Binding authorValidationBinding(Queue authorValidationQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(authorValidationQueue).to(topicExchange).with("authors.validated");
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange peopleExchange() {
        return new TopicExchange("people-exchange");
    }

    @Bean
    public TopicExchange operationsExchange() {
        return new TopicExchange("operations-exchange");
    }
}
