package pt.psoft.g1.psoftg1.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Profile("base65")
public class RandomBase65UserIdGenerator implements UserIdGenerator {

    private static final String BASE65_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~!*"; // 65 chars
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(BASE65_ALPHABET.charAt(RANDOM.nextInt(BASE65_ALPHABET.length())));
        }
        return sb.toString();
    }
}
