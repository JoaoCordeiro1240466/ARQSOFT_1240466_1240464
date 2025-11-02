package pt.psoft.g1.psoftg1.usermanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class RandomBase65UserIdGeneratorTest {

    // Constante necessária para os testes de caixa-branca
    private static final String BASE65_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~!*";

    private RandomBase65UserIdGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBase65UserIdGenerator();
    }

    // --- Testes Opaque-Box (Caixa-Preta) ---

    /**
     * Teste Opaque-Box: Verifica o requisito funcional básico.
     * O ID gerado não deve ser nulo nem vazio.
     */
    @Test
    void testGenerateId_IsNotNullOrEmpty() {
        String id = generator.generateId();
        assertNotNull(id, "O ID não deve ser nulo.");
        assertFalse(id.isEmpty(), "O ID não deve estar vazio.");
    }

    /**
     * Teste Opaque-Box: Verifica o requisito funcional principal (unicidade).
     */
    @Test
    void testGenerateId_GeneratesUniqueIds() {
        int numberOfIdsToGenerate = 10_000;
        Set<String> generatedIds = new HashSet<>();

        for (int i = 0; i < numberOfIdsToGenerate; i++) {
            String id = generator.generateId();
            assertTrue(generatedIds.add(id), "Foi detectada uma colisão de ID: " + id);
        }

        assertEquals(numberOfIdsToGenerate, generatedIds.size());
    }

    // --- Testes Transparent-Box (Caixa-Branca) ---

    /**
     * Teste Transparent-Box: Validamos o comprimento exato (baseado no 'for' loop).
     */
    @Test
    void testGenerateId_HasCorrectLength() {
        String id = generator.generateId();
        assertEquals(6, id.length(), "O ID deve ter exatamente 6 caracteres.");
    }

    /**
     * Teste Transparent-Box: Validamos o conjunto de caracteres (baseado no 'BASE65_ALPHABET').
     */
    @Test
    void testGenerateId_ContainsOnlyValidBase65Characters() {
        String id = generator.generateId();

        for (char c : id.toCharArray()) {
            assertTrue(BASE65_ALPHABET.indexOf(c) != -1,
                    "O ID contém um caracter inválido: '" + c + "'");
        }
    }

    /**
     * Teste Transparent-Box (Alternativo): Usando Regex para validar formato e caracteres.
     */
    @Test
    void testGenerateId_MatchesRegexFormat() {
        String id = generator.generateId();
        String regex = "^[A-Za-z0-9_.~!*-]{6}$";
        assertTrue(id.matches(regex), "O ID '" + id + "' não corresponde ao formato Base65 esperado.");
    }
}