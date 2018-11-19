package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDAO {

	public static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";

	public static final String SEARCH_QUERY = "SELECT * FROM ITEM WHERE LOWER(NAME) LIKE LOWER(?)";
	public static final String GET_ITEM_QUERY = "SELECT * FROM ITEM WHERE NUMBER = ?";

	public static final String[] COLUMNS = { "NONE", "PRICE ASC", "PRICE DESC", "NAME ASC", "NAME DESC" };

	private Connection con;
	// This helps prevent SQL injection attacks on the ORDER BY statement.
	private HashMap<String, String> orderMap;

	public ItemDAO() {
		this.orderMap = new HashMap<String, String>();

		for (String order : COLUMNS) {
			orderMap.put(order, order);
		}

		try {
			Class.forName(DERBY_DRIVER).newInstance();
			con = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<ItemBean> search(String searchInputValue) throws Exception {

		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS;
		preS = con.prepareStatement(SEARCH_QUERY);

		preS.setString(1, "%" + searchInputValue + "%");

		ResultSet r = preS.executeQuery();

		List<ItemBean> itemList = makeItemList(r);
		return itemList;
	}

	public List<ItemBean> advanceSearch(String searchInputValue, String sortBy, String maxCost, String minCost)
			throws Exception {
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS;
		preS = con.prepareStatement(SEARCH_QUERY);

		preS.setString(1, "%" + searchInputValue + "%");

		ResultSet r = preS.executeQuery();

		List<ItemBean> itemList = makeItemList(r);
		return itemList;

	}

	public List<ItemBean> getAllItems() throws Exception {
		List<ItemBean> itemList = new ArrayList<>();

		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS;
		preS = con.prepareStatement(SEARCH_QUERY);

		preS.setString(1, "%");

		ResultSet r = preS.executeQuery();
		itemList = makeItemList(r);

		return itemList;
	}

	public ItemBean getItem(String itemId) throws Exception {
		ItemBean item = new ItemBean();

		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS;
		preS = con.prepareStatement(SEARCH_QUERY);
		preS.setString(1, itemId);

		ResultSet r = preS.executeQuery();

		if (r.next()) {
			item = setItemBean(r);
		} else {
			throw new IllegalArgumentException(itemId + " is not a valid item number.");
		}

		return item;
	}

	private List<ItemBean> makeItemList(ResultSet r) throws SQLException {

		List<ItemBean> itemList = new ArrayList<>();

		while (r.next()) {
			ItemBean item = setItemBean(r);
			itemList.add(item);
		}

		return itemList;
	}

	private ItemBean setItemBean(ResultSet r) throws SQLException {
		ItemBean item = new ItemBean();

		item.setUnit(r.getString("UNIT"));
		item.setCostPrice(r.getDouble("COSTPRICE"));
		item.setSupID(r.getInt("SUPID"));
		item.setCatId(r.getInt("CATID"));
		item.setReorder(r.getInt("REORDER"));
		item.setOnorder(r.getInt("ONORDER"));
		item.setQuantity(r.getInt("QTY"));
		item.setPrice(r.getDouble("PRICE"));
		item.setName(r.getString("NAME"));
		item.setNumber(r.getString("NUMBER"));

		return item;
	}

	// Checks the input string against its value in a map and returns the mapped
	// value. If there is no matching mapped value, an exception is thrown. This
	// could indicate the user replaced the value of the options.
	private String getSort(String order) {

		String sanitizedOrder = orderMap.get(order);

		if (sanitizedOrder != null)
			return orderMap.get(order);
		else
			throw new IllegalArgumentException("Attempt to inject SQL Detected.");

	}

}
