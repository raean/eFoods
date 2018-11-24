import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MiddlewareTest {
	
	static String poFolder;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		poFolder = System.getProperty("user.home") + "/PO/";
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
		System.out.println(poFolder);
	}

	@Test
	void testMiddlewareException() {
		try {
			File dummyfile = new File("bad directory");
			Middleware testMiddle = new Middleware(dummyfile);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail("wrong exception thrown");
		}

	}

}
