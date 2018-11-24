import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class Middleware {

	private static final String PO_PATH = System.getProperty("user.home") + "/PO/";
	private static final String IN_PO = PO_PATH + "inPO/";
	private static final String OUT_PO = PO_PATH + "outPO";

	private JAXBContext orderContext;
	private Marshaller orderMarshaller;
	private Unmarshaller orderUnMarshaller;

	public Middleware() {

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

	public static void main(String[] args) {
		File poDir = new File(PO_PATH);

		if (!(poDir.exists() && poDir.isDirectory())) {
			System.err.println("There is no PO file in the directory, exiting");
			System.exit(1);
		}

	}

}
