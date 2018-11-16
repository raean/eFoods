package model;

public class ItemBean {
	private String unit; // UNIT
	private double costPrice; // COSTPRICE
	private int supID; // SUPID
	private int catId; // CATID
	private int reorder; // REORDER
	private int onorder; // ONORDER
	private int quantity; // QTY
	private double price; // PRICE
	private String name; // NAME
	private String number; // 8-digit product code. Key

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
