import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MiddlewareTest {

	static final String PO_FOLDER = System.getProperty("user.home") + "/PO/";
	static final File PO_FILE = new File(PO_FOLDER);

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testMiddleware() {

		try {
			Middleware b2c = new Middleware(PO_FILE);
			assertTrue(true);
		} catch (Exception e) {
			fail("Error thrown " + e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "bad directory", "/eecs/adamzis/test" })
	void testMiddlewareException(String nonsense) {
		try {
			File dummyfile = new File(nonsense);

			Middleware testMiddle = new Middleware(dummyfile);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail("wrong exception thrown");
		}

	}

}
