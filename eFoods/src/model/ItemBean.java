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
	private double extended; // Total price, equal to quantity * price

	@XmlTransient
	private String unit; // UNIT quantity per unit
	@XmlTransient
	private int catId; // CATID category Id

	public ItemBean() {
		super();
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getExtended() {
		return extended;
	}

	public void setExtended(double extended) {
		this.extended = extended;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
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
		return "itemBean [unit=" + unit + ", costPrice=" + extended + ", catId=" + catId + ", quantity=" + quantity
				+ ", price=" + price + ", name=" + name + ", number=" + number + "]";
	}

}
