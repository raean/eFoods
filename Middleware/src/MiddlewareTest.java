import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

	static String poPath;
	static File poFolder;
	static Middleware middleware;

	static File[] inDir;
	static File[] outDir;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		poPath = System.getProperty("user.home") + "/POTest/";
		poFolder = new File(poPath);
		middleware = new Middleware(poFolder);

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		File[] outFiles = middleware.getOutDir().listFiles();
		File inDir = middleware.getInDir();

		for (File outFile : outFiles) {
			File inFile = new File(inDir.getPath() + "/" + outFile.getName());
			outFile.renameTo(inFile);

			System.out.println(inFile.exists());
			System.out.println(outFile.exists());
		}
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testMiddleware() {
		try {
			Middleware testConstruct = new Middleware(poFolder);
			assertTrue(true);
		} catch (Exception e) {
			fail("Error thrown " + e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "bad directory", "/cs/adamzis/test", "/eecs/home/adamzis/PO/inPO", "505",
			"/eecs/home/adamzis/PO/ouPO" })
	void testMiddlewareException(String nonsense) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			File dummyfile = new File(nonsense);
			Middleware badMiddle = new Middleware(dummyfile);
		});

		assertEquals("There is no inPO or outPO directory in the PO folder, exiting", exception.getMessage());

	}

	@Test
	void testListPoFiles() throws Exception {
		List<OrderBean> returnedOrders = middleware.listInboxFiles();

		assertAll("Check list", () -> assertTrue(!returnedOrders.isEmpty()),
				() -> assertEquals(middleware.getInDir().listFiles().length, returnedOrders.size()));

		for (OrderBean order : returnedOrders) {
			assertNotNull(order);
			assertEquals("adamzis", order.getCustomer().getAccount());
		}
	}

	@Test
	void testInOutDirectories() {

		assertAll("Check that inPo and outPO point to right directories",
				() -> assertEquals("/cs/home/adamzis/POTest/inPO", middleware.getInDir().getPath()),
				() -> assertEquals("/cs/home/adamzis/POTest/outPO", middleware.getOutDir().getPath()));

		assertAll("Check that inPo and outPo are directories", () -> assertTrue(middleware.getInDir().isDirectory()),
				() -> assertTrue(middleware.getOutDir().isDirectory()));

	}

	@Test
	void testConsolidateOrders() throws Exception {
		List<OrderBean> orderList = middleware.listInboxFiles();
		Map<String, TotalItemsBean> quantityMap = middleware.consolidateOrders(orderList);

		assertTrue(!quantityMap.isEmpty());

		assertAll("check that Map contains the three keys", () -> assertTrue(quantityMap.containsKey("0905A123")),
				() -> assertTrue(quantityMap.containsKey("0905A112")),
				() -> assertTrue(quantityMap.containsKey("0905A044")));

		// Make sure each item in the map is not null.
		for (TotalItemsBean item : quantityMap.values()) {
			assertNotNull(item);
			assertTrue(item.getQuantity() > 0);
		}

		TotalItemsBean firstItem = quantityMap.get("0905A112");
		assertEquals(5, firstItem.getQuantity());
	}

	@Test
	void testMakeReport() throws Exception {
		List<OrderBean> orderList = middleware.listInboxFiles();
		Map<String, TotalItemsBean> quantityMap = middleware.consolidateOrders(orderList);

		ReportBean report = middleware.makeReport(quantityMap);
		List<TotalItemsBean> totalItems = report.getItems();

		assertNotNull(report);

		for (int i = 0; i < totalItems.size(); i++) {
			for (int j = 0; j < totalItems.size(); j++) {
				if (j != i) {
					assertNotEquals(totalItems.get(j).getNumber(), totalItems.get(i).getNumber());
				}
			}
		}
	}

	@Test
	void testMarshallReport() throws Exception {
		List<OrderBean> orderList = middleware.listInboxFiles();
		Map<String, TotalItemsBean> quantityMap = middleware.consolidateOrders(orderList);
		ReportBean report = middleware.makeReport(quantityMap);

		File[] poDir = poFolder.listFiles();
		int totalFiles = poDir.length;

		middleware.marshallReport(report);

		poDir = poFolder.listFiles();

		// Test that the file has been created
		assertEquals(totalFiles + 1, poDir.length);

		File latestReport = poDir[poDir.length - 1];
		assertTrue(latestReport.exists());
		assertTrue(!latestReport.isDirectory());

		inDir = middleware.getInDir().listFiles();
		
		assertTrue(inDir.length == 0);

	}

}
