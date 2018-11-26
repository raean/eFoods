import java.io.File;
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

	private File[] inFiles;
	private File[] outFiles;

	private Marshaller reportMarshaller;
	private Unmarshaller orderUnMarshaller;

	public Middleware(File poDir) {
		setJAXB(ORDER_BEAN, REPORT_BEAN);
		setInOutDir(poDir);
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

	private void setInOutDir(File poDir) {
		this.poPath = poDir.getPath();

		inDir = new File(poPath + IN_PO_PATH);
		outDir = new File(poPath + OUT_PO_PATH);

		if (!inDir.isDirectory() || !outDir.isDirectory()) {
			throw new IllegalArgumentException("There is no inPO or outPO directory in the PO folder, exiting");
		}

		inFiles = inDir.listFiles();
		outFiles = outDir.listFiles();
	}

	// TODO The files should be moved right after being read into the array.
	public List<OrderBean> listInboxFiles() throws JAXBException {
		List<OrderBean> orderList = new ArrayList<>();

		for (File fileName : inFiles) {
			OrderBean order = (OrderBean) orderUnMarshaller.unmarshal(fileName);
			orderList.add(order);
			System.out.println(order.getId());
		}

		return orderList;
	}

	public ReportBean consolidateOrders(List<OrderBean> orders) throws Exception {
		ReportBean report = new ReportBean();
		List<TotalItemsBean> totalItems = new LinkedList<>();
		Map<String, Integer> mappedOrders = new HashMap<>();

		return report;
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Usage: java Middleware <PO Folder Path>");
			System.exit(1);
		}

		File poDir = new File(args[0]);

		if (!poDir.isDirectory()) {
			System.err.println("There is no PO directory, exiting");
			System.exit(1);
		}

		Middleware b2c = new Middleware(poDir);
		List<OrderBean> orders = b2c.listInboxFiles();

	}

}
