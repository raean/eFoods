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
	@XmlElement(name = "item")
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
	private String totalFormat;
	@XmlElement(name = "shipping")
	private String shippingFormat;
	@XmlElement(name = "HST")
	private String HSTFormat;
	@XmlElement(name = "grandTotal")
	private String grandTotalFormat;

	public String getTotalFormat() {
		return this.totalFormat;
	}

	public String getShippingFormat() {
		return this.shippingFormat;
	}

	public String getHSTFormat() {
		return this.HSTFormat;
	}

	public String getGrandTotalFormat() {
		return this.grandTotalFormat;
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
		this.totalFormat = String.format("%.2f", total);
	}

	public double getShipping() {
		return shipping;
	}

	public void setShipping(double shipping) {
		this.shipping = shipping;
		this.shippingFormat = String.format("%.2f", shipping);
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
		this.grandTotalFormat = String.format("%.2f", grandTotal);
	}

	public double getHST() {
		return HST;
	}

	public void setHST(double HST) {
		this.HST = HST;
		this.HSTFormat = String.format("%.2f", HST);
	}

	@Override
	public String toString() {
		return "OrderBean [id=" + id + ", submitted=" + submitted + ", customer=" + customer + ", items=" + items
				+ ", total=" + total + ", shipping=" + shipping + ", HST=" + HST + ", grandTotal=" + grandTotal + "]";
	}

}
