package pt.psoft.g1.psoftg1.usermanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class TimestampHexaUserIdGeneratorTest {

    private TimestampHexaUserIdGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new TimestampHexaUserIdGenerator();
    }

    // --- Testes Opaque-Box (Caixa-Preta) ---

    /**
     * Teste Opaque-Box: Requisito básico (não-nulo, não-vazio).
     */
    @Test
    void testGenerateId_IsNotNullOrEmpty() {
        String id = generator.generateId();
        assertNotNull(id, "O ID não deve ser nulo.");
        assertFalse(id.isEmpty(), "O ID não deve estar vazio.");
    }

    /**
     * Teste Opaque-Box: Requisito de unicidade.
     */
    @Test
    void testGenerateId_GeneratesUniqueIds() {
        int numberOfIdsToGenerate = 10_000;
        Set<String> generatedIds = new HashSet<>();

        for (int i = 0; i < numberOfIdsToGenerate; i++) {
            assertTrue(generatedIds.add(generator.generateId()), "Foi detectada uma colisão de ID.");
        }
    }

    /**
     * Teste Opaque-Box: Requisito funcional de ordenação (baseado em timestamp).
     */
    @Test
    void testGenerateId_AreRoughlySortableByTime() throws InterruptedException {
        String id1 = generator.generateId();
        Thread.sleep(2); // Garante que o timestamp mudou
        String id2 = generator.generateId();

        assertTrue(id2.compareTo(id1) > 0, "IDs sequenciais não estão ordenados corretamente.");
    }

    // --- Testes Transparent-Box (Caixa-Branca) ---

    /**
     * Teste Transparent-Box: Valida o formato geral (Regex) baseado no código.
     * (Hex de timestamp + 6 hex aleatórios).
     */
    @Test
    void testGenerateId_MatchesHexFormat() {
        String id = generator.generateId();
        String regex = "^[0-9a-f]+[0-9a-f]{6}$";

        assertTrue(id.matches(regex), "O ID '" + id + "' não corresponde ao formato hexadecimal esperado.");
    }

    /**
     * Teste Transparent-Box: Valida os componentes (Timestamp e Aleatório).
     */
    @Test
    void testGenerateId_ComponentsAreValid() {
        long timeBefore = System.currentTimeMillis();
        String id = generator.generateId();
        long timeAfter = System.currentTimeMillis();

        assertTrue(id.length() > 6, "O ID é demasiado curto.");

        // Valida parte aleatória (últimos 6 caracteres)
        String randomPart = id.substring(id.length() - 6);
        assertTrue(randomPart.matches("^[0-9a-f]{6}$"), "A parte aleatória não são 6 dígitos hex.");

        // Valida parte do timestamp (tudo menos os últimos 6)
        String timestampPart = id.substring(0, id.length() - 6);
        try {
            long timestamp = Long.parseLong(timestampPart, 16); // Converte de Hex para Long

            assertTrue(timestamp >= timeBefore && timestamp <= timeAfter,
                    "O prefixo do ID não corresponde a um timestamp válido.");

        } catch (NumberFormatException e) {
            fail("O prefixo do ID '" + timestampPart + "' não é um hexadecimal válido.");
        }
    }
}