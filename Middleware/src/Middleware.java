import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class Middleware {

	private String poPath;
	private String inPoPath;
	private String outPoPath;

	private JAXBContext orderContext;
	private Marshaller orderMarshaller;
	private Unmarshaller orderUnMarshaller;

	public Middleware(File poDir) {
		setOrderJAXB();
		setPoDirs(poDir);
	}

	private void setOrderJAXB() {
		try {
			this.orderContext = JAXBContext.newInstance(OrderBean.class);
			this.orderMarshaller = orderContext.createMarshaller();
			this.orderUnMarshaller = orderContext.createUnmarshaller();

			this.orderMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (JAXBException e) {
			System.err.println("Fatal error " + e.getMessage());
			System.exit(1);
		}
	}

	private void setPoDirs(File poDir) {
		this.poPath = poDir.getPath();
		this.inPoPath = poPath + "/inPO/";
		this.outPoPath = poPath + "/outPO/";

	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java Middleware <PO Folder Path>");
			System.exit(1);
		}

		File poDir = new File(args[0]);

		if (!(poDir.exists() && poDir.isDirectory())) {
			System.err.println("There is no PO directory, exiting");
			System.exit(1);
		}

		Middleware service = new Middleware(poDir);

	}

}
