package pt.psoft.g1.psoftg1.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier; // <--- Importante!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE_NAME = "library-events-exchange";
    public static final String PEOPLE_EXCHANGE_NAME = "people-exchange";
    public static final String QUEUE_NAME = "people-validation-queue";
    public static final String ROUTING_KEY = "books.created";

    @Bean
    public TopicExchange libraryEventsExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange peopleExchange() {
        return new TopicExchange(PEOPLE_EXCHANGE_NAME);
    }

    @Bean
    public Queue peopleValidationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // 👇 CORREÇÃO AQUI: Adicionei o @Qualifier para desambiguar
    @Bean
    public Binding binding(Queue queue, @Qualifier("libraryEventsExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}