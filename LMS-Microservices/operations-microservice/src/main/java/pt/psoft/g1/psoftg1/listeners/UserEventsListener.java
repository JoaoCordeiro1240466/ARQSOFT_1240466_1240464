package pt.psoft.g1.psoftg1.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.model.Reader;
import pt.psoft.g1.psoftg1.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.events.UserCreatedEvent;

import java.util.Optional;

@Component
public class UserEventsListener {

    private final ReaderRepository readerRepository;

    public UserEventsListener(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @RabbitListener(queues = "operations-user-created-queue")
    public void receiveUserCreated(UserCreatedEvent event) {
        System.out.println("Received UserCreatedEvent: " + event);

        Optional<Reader> existingReader = readerRepository.findByDatabaseId(event.getDatabaseId());

        if (existingReader.isPresent()) {
            // Update existing reader
            Reader readerToUpdate = existingReader.get();
            readerToUpdate.setUsername(event.getUsername());
            readerToUpdate.setFullName(event.getFullName());
            readerToUpdate.setRole(event.getRole());
            readerRepository.save(readerToUpdate);
            System.out.println("Updated existing Reader: " + readerToUpdate);
        } else {
            // Create new reader
            Reader newReader = new Reader(
                    event.getDatabaseId(),
                    event.getUsername(),
                    event.getFullName(),
                    event.getRole()
            );
            readerRepository.save(newReader);
            System.out.println("Created new Reader: " + newReader);
        }
    }
}
