import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class Middleware {

	private static final String IN_PO_PATH = "/inPO/";
	private static final String OUT_PO_PATH = "/outPO/";

	private static final Class<OrderBean> ORDER_BEAN = OrderBean.class;
	private static final Class<ReportBean> REPORT_BEAN = ReportBean.class;

	private String poPath;
	private File inDir;
	private File outDir;

	private File[] poFiles;
	private File[] inFiles;

	private Marshaller reportMarshaller;
	private Unmarshaller orderUnMarshaller;

	public Middleware(File poDir) {
		this.poFiles = poDir.listFiles();

		setJAXB(ORDER_BEAN, REPORT_BEAN);
		setInOutDirFiles(poDir);
	}

	private void setJAXB(Class<OrderBean> orderBean, Class<ReportBean> reportBean) {
		try {
			JAXBContext reportContext = JAXBContext.newInstance(reportBean);
			JAXBContext orderContext = JAXBContext.newInstance(orderBean);

			this.reportMarshaller = reportContext.createMarshaller();
			this.orderUnMarshaller = orderContext.createUnmarshaller();

			this.reportMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (JAXBException e) {
			System.err.println("Fatal JAXB error " + e.getMessage());
			System.exit(1);
		}
	}

	private void setInOutDirFiles(File poDir) {
		this.poPath = poDir.getPath();

		this.inDir = new File(poPath + IN_PO_PATH);
		this.outDir = new File(poPath + OUT_PO_PATH);

		if (!inDir.isDirectory() || !outDir.isDirectory()) {
			throw new IllegalArgumentException("There is no inPO or outPO directory in the PO folder, terminating.");
		}

		this.inFiles = inDir.listFiles();
	}

	// TODO The files should be moved right after being read into the array or after
	// report made

	/**
	 * O(n) time
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderBean> getInboxOrders() throws Exception {
		List<OrderBean> orderList = new ArrayList<>();

		if (inFiles.length == 0) {
			throw new Exception("Inbox folder is empty, terminating.");
		}

		for (File fileName : inFiles) {
			OrderBean order = (OrderBean) orderUnMarshaller.unmarshal(fileName);
			orderList.add(order);
		}

		return orderList;
	}

	public Map<String, TotalItemsBean> consolidateOrders(List<OrderBean> orderList) {
		Map<String, TotalItemsBean> quantityMap = new HashMap<>();

		for (OrderBean order : orderList) {
			List<ItemBean> orderItems = order.getItems();

			for (ItemBean orderItem : orderItems) {
				String orderItemNumber = orderItem.getNumber();
				String orderItemName = orderItem.getName();
				int orderItemQty = orderItem.getQuantity();

				if (!quantityMap.containsKey(orderItemNumber)) {
					TotalItemsBean totalItem = new TotalItemsBean();

					totalItem.setNumber(orderItemNumber);
					totalItem.setName(orderItemName);
					totalItem.setQuantity(orderItemQty);

					quantityMap.put(orderItemNumber, totalItem);
				} else {
					TotalItemsBean totalItem = quantityMap.get(orderItemNumber);
					totalItem.setQuantity(totalItem.getQuantity() + orderItemQty);
					quantityMap.put(orderItemNumber, totalItem);
				}
			}
		}

		return quantityMap;
	}

	public ReportBean makeReport(Map<String, TotalItemsBean> quantityMap) {
		ReportBean report = new ReportBean();

		List<TotalItemsBean> totalItemsList = new LinkedList<>(quantityMap.values());
		report.setItems(totalItemsList);

		return report;
	}

	public void marshallReport(ReportBean report) throws JAXBException, IOException {
		File reportFile = new File(poPath + "/report" + (poFiles.length - 1) + ".xml");
		reportFile.createNewFile();

		reportMarshaller.marshal(report, reportFile);

		moveCompletedOrders();

	}

	private void moveCompletedOrders() {

		for (File inFile : inFiles) {
			File outFile = new File(outDir.getPath() + "/" + inFile.getName());
			inFile.renameTo(outFile);
		}

	}

	public File getInDir() {
		return inDir;
	}

	public File getOutDir() {
		return outDir;
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java Middleware <PO Folder Path>");
			System.exit(1);
		}

		File poDir = new File(args[0]);

		if (!poDir.isDirectory()) {
			System.err.println("There is no PO directory, exiting");
			System.exit(1);
		}

		try {
			Middleware b2c = new Middleware(poDir);
			List<OrderBean> orderList = b2c.getInboxOrders();
			Map<String, TotalItemsBean> quantityMap = b2c.consolidateOrders(orderList);
			ReportBean report = b2c.makeReport(quantityMap);
			b2c.marshallReport(report);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		System.out.println("Program complete, exiting.");
	}

}
