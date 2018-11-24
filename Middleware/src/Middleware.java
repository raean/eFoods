import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class Middleware {

	private static final String IN_PO_PATH = "/inPO/";
	private static final String OUT_PO_PATH = "/outPO/";
	private static final Class<OrderBean> ORDERBEAN = OrderBean.class;
	private String poPath;

	private File inDir;
	private File outDir;

	private File[] inFiles;
	private File[] outFiles;

	private JAXBContext orderContext;
	private Marshaller orderMarshaller;
	private Unmarshaller orderUnMarshaller;

	public Middleware(File poDir) {
		setOrderJAXB(ORDERBEAN);
		setInOutDir(poDir);
	}

	private void setOrderJAXB(Class<OrderBean> orderBean) {
		try {
			this.orderContext = JAXBContext.newInstance(orderBean);
			this.orderMarshaller = orderContext.createMarshaller();
			this.orderUnMarshaller = orderContext.createUnmarshaller();

			this.orderMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
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

	public void listPoFiles() {
		for (File fileName : inFiles) {
			System.out.println(fileName.getName());
		}
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

		Middleware b2c = new Middleware(poDir);
		b2c.listPoFiles();

	}

}
