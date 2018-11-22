package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "submitted", "customer", "items", "totalFormat", "shippingFormat", "HSTFormat",
		"grandTotalFormat" })
public class OrderBean {
	@XmlAttribute
	private int id;
	@XmlAttribute
	private String submitted;

	private CustomerBean customer;
	@XmlElementWrapper
	@XmlElement(name="item")
	private List<ItemBean> items;

	@XmlTransient
	private double total;
	@XmlTransient
	private double shipping;
	@XmlTransient
	private double HST;
	@XmlTransient
	private double grandTotal;

	@XmlElement(name = "total")
	private String getTotalFormat() {
		return String.format("%.2f", total);
	}

	@XmlElement(name = "shipping")
	private String getShippingFormat() {
		return String.format("%.2f", shipping);
	}

	@XmlElement(name = "HST")
	private String getHSTFormat() {
		return String.format("%.2f", HST);
	}

	@XmlElement(name = "grandTotal")
	private String getGrandTotalFormat() {
		return String.format("%.2f", grandTotal);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubmitted() {
		return submitted;
	}

	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}

	public CustomerBean getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerBean customer) {
		this.customer = customer;
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getShipping() {
		return shipping;
	}

	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public double getHST() {
		return HST;
	}

	public void setHST(double HST) {
		this.HST = HST;
	}

	@Override
	public String toString() {
		return "OrderBean [id=" + id + ", submitted=" + submitted + ", customer=" + customer + ", items=" + items
				+ ", total=" + total + ", shipping=" + shipping + ", HST=" + HST + ", grandTotal=" + grandTotal + "]";
	}

}
