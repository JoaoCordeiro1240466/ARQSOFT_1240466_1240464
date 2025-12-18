package pt.psoft.g1.psoftg1.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Profile("timestamp-hexa")
public class TimestampHexaUserIdGenerator implements UserIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateId() {
        String timestampHex = Long.toHexString(System.currentTimeMillis());
        int random = RANDOM.nextInt(0xFFFFFF); // 6 hex digits
        return timestampHex + String.format("%06x", random);
    }
}
