package pt.psoft.g1.psoftg1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("base65")
class PsoftG1ApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
