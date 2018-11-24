import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import model.OrderBean;

class MiddlewareTest {

	static final String PO_FOLDER = System.getProperty("user.home") + "/PO/";
	static final File PO_FILE = new File(PO_FOLDER);
	static Middleware b2c;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		b2c = new Middleware(PO_FILE);
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
			Middleware testConstruct = new Middleware(PO_FILE);
			assertTrue(true);
		} catch (Exception e) {
			fail("Error thrown " + e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "bad directory", "/cs/adamzis/test", "/eecs/home/adamzis/PO/inPO", "505",
			"/eecs/home/adamzis/PO/ouPO" })
	void testMiddlewareException(String nonsense) {
		try {
			File dummyfile = new File(nonsense);
			Middleware badMiddle = new Middleware(dummyfile);

			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail("Wrong exception thrown");
		}

	}

	@Test
	void testListPoFiles() throws JAXBException {
		List<OrderBean> returnedOrders = b2c.listInboxFiles();
		assertTrue(!returnedOrders.isEmpty());
	}

}
