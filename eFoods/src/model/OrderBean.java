package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class OrderBean {
	@XmlAttribute
	private int id;
	@XmlAttribute
	private String submitted;

	private CustomerBean customer;
	private List<ItemBean> items;

	private double total;
	private double shipping;
	private double grandTotal;

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

	@Override
	public String toString() {
		return "OrderBean [id=" + id + ", submitted=" + submitted + ", customer=" + customer + ", items=" + items
				+ ", total=" + total + ", shipping=" + shipping + ", grandTotal=" + grandTotal + "]";
	}
}
