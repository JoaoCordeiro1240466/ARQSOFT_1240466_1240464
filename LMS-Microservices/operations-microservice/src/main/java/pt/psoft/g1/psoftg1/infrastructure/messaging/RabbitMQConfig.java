package pt.psoft.g1.psoftg1.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // --- Exchange ---
    public static final String OPERATIONS_EXCHANGE_NAME = "operations-exchange";

    // --- GÉNEROS (Genres) ---
    public static final String GENRE_CREATE_QUEUE = "operations-create-genre-queue";
    public static final String GENRE_CREATE_ROUTING_KEY = "genre.create";

    // --- PESSOAS (Users/Readers) ---
    public static final String PEOPLE_EXCHANGE_NAME = "people-exchange";
    public static final String USER_CREATED_QUEUE = "operations-user-created-queue";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";

    // --- LIVROS (Books) ---
    public static final String LIBRARY_EXCHANGE_NAME = "library-events-exchange";

    // Created
    public static final String BOOK_CREATED_QUEUE = "lending-book-created-queue";
    public static final String BOOK_CREATED_ROUTING_KEY = "books.created";

    // Deleted (Adicionado agora para suportar o teu Listener)
    public static final String BOOK_DELETED_QUEUE = "lending-book-deleted-queue";
    public static final String BOOK_DELETED_ROUTING_KEY = "books.deleted";

    // 1. Exchanges
    @Bean
    public TopicExchange operationsExchange() {
        return new TopicExchange(OPERATIONS_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange peopleExchange() {
        return new TopicExchange(PEOPLE_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange libraryEventsExchange() {
        return new TopicExchange(LIBRARY_EXCHANGE_NAME);
    }

    // 2. Queues
    @Bean
    public Queue genreCreateQueue() {
        return new Queue(GENRE_CREATE_QUEUE, true);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue bookCreatedQueue() {
        return new Queue(BOOK_CREATED_QUEUE, true);
    }

    @Bean
    public Queue bookDeletedQueue() { // <--- NOVO
        return new Queue(BOOK_DELETED_QUEUE, true);
    }

    // 3. Bindings
    @Bean
    public Binding genreCreateBinding(Queue genreCreateQueue, @Qualifier("operationsExchange") TopicExchange exchange) {
        return BindingBuilder.bind(genreCreateQueue).to(exchange).with(GENRE_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, @Qualifier("peopleExchange") TopicExchange exchange) {
        return BindingBuilder.bind(userCreatedQueue).to(exchange).with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bookCreatedBinding(Queue bookCreatedQueue, @Qualifier("libraryEventsExchange") TopicExchange exchange) {
        return BindingBuilder.bind(bookCreatedQueue).to(exchange).with(BOOK_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bookDeletedBinding(Queue bookDeletedQueue, @Qualifier("libraryEventsExchange") TopicExchange exchange) { // <--- NOVO
        return BindingBuilder.bind(bookDeletedQueue).to(exchange).with(BOOK_DELETED_ROUTING_KEY);
    }

    // 4. Conversores
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
