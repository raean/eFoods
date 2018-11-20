package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class OrderBean {
	@XmlAttribute
	private int id;
	@XmlAttribute
	private String submitted;

	private CustomerBean customer;

	private double total;
	private double shipping;
	private double grandTotal;
}
