package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemBean {

	@XmlAttribute
	private String number; // 8-digit product code. Key
	private String name; // NAME
	private double price; // PRICE
	private int quantity; // QTY
	@XmlElement(name = "extended")
	private double costPrice; // COSTPRICE

	@XmlTransient
	private String unit; // UNIT quantity per unit
	@XmlTransient
	private int catId; // CATID category Id
	@XmlTransient
	private int supID; // SUPID
	@XmlTransient
	private int reorder; // REORDER
	@XmlTransient
	private int onorder; // ONORDER

	public ItemBean() {
		super();
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public int getSupID() {
		return supID;
	}

	public void setSupID(int supID) {
		this.supID = supID;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getReorder() {
		return reorder;
	}

	public void setReorder(int reorder) {
		this.reorder = reorder;
	}

	public int getOnorder() {
		return onorder;
	}

	public void setOnorder(int onorder) {
		this.onorder = onorder;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "itemBean [unit=" + unit + ", costPrice=" + costPrice + ", supID=" + supID + ", catId=" + catId
				+ ", reorder=" + reorder + ", onorder=" + onorder + ", quantity=" + quantity + ", price=" + price
				+ ", name=" + name + ", number=" + number + "]";
	}

}
