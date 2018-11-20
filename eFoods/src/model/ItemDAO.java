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

/**
 * Data access layer for the item table in the database. Handles searching and
 * retrieving functionality. Should be called by the engine
 *
 */
public class ItemDAO {

	public static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	public static final String SET_SCHEMA = "set schema roumani";

	public static final String SEARCH_QUERY = "SELECT * FROM ITEM WHERE LOWER(NAME) LIKE LOWER(?)";
	public static final String ITEM_NUM_QUERY = "SELECT * FROM ITEM WHERE NUMBER = ?";
	public static final String ADVANCE_QUERY = SEARCH_QUERY + " AND PRICE >= MIN PRICE AND PRICE <= MAX PRICE";
	public static final String GET_ITEM_QUERY = "SELECT * FROM ITEM WHERE NUMBER = ?";

	// This helps prevent SQL injection attacks on the ORDER BY statement.
	public static final String[] SORT_OPTIONS = { "NONE", "PRICE ASC", "PRICE DESC", "NAME ASC", "NAME DESC" };
	public static final String[] USER_SORT_INPUT = { "NONE", "ascPrice", "descPrice", "ascName", "descName" };
	private HashMap<String, String> orderMap;

	private Connection con;

	/**
	 * Constructs an ItemDao, initializing the driver and constructing the orderMap
	 * used to prevent SQL injections when the user wants to order their sort.
	 */
	public ItemDAO() {
		this.orderMap = new HashMap<>();

		try {
			Class.forName(DERBY_DRIVER).newInstance();
			con = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		for (int i = 0; i < SORT_OPTIONS.length; i++) {
			orderMap.put(USER_SORT_INPUT[i], SORT_OPTIONS[i]);
		}
	}

	private void setSchema() throws SQLException {
		Statement setRoumani;
		setRoumani = con.createStatement();
		setRoumani.executeUpdate(SET_SCHEMA);
	}

	public List<ItemBean> search(String searchInputValue) throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		List<ItemBean> itemList;

		setSchema();

		searchStatement = con.prepareStatement(SEARCH_QUERY);
		searchStatement.setString(1, "%" + searchInputValue + "%");

		itemResults = searchStatement.executeQuery();

		itemList = makeItemList(itemResults);
		return itemList;
	}

	public List<ItemBean> advanceSearch(String searchInputValue, String minCost, String maxCost, String sortBy)
			throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		List<ItemBean> itemList;

		setSchema();

		if (sortBy.isEmpty()) {
			searchStatement = con.prepareStatement(ADVANCE_QUERY);
		} else {
			searchStatement = con.prepareStatement(ADVANCE_QUERY + " ORDER BY " + getSort(sortBy));
		}

		if (minCost.isEmpty()) {
			searchStatement.setString(2, "0.0");
		} else {
			searchStatement.setString(2, minCost);
		}

		if (maxCost.isEmpty()) {
			searchStatement.setString(3, "INF");
		} else {
			searchStatement.setString(3, maxCost);
		}

		searchStatement.setString(1, "%" + searchInputValue + "%");

		itemResults = searchStatement.executeQuery();
		itemList = makeItemList(itemResults);
		return itemList;

	}

	public List<ItemBean> getAllItems() throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		List<ItemBean> itemList;

		setSchema();

		searchStatement = con.prepareStatement(SEARCH_QUERY);
		searchStatement.setString(1, "%");

		itemResults = searchStatement.executeQuery();
		itemList = makeItemList(itemResults);

		return itemList;
	}

	public List<ItemBean> getAllItems(String sortBy) throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		List<ItemBean> itemList;

		setSchema();

		searchStatement = con.prepareStatement(SEARCH_QUERY + " ORDER BY " + getSort(sortBy));
		searchStatement.setString(1, "%");

		itemResults = searchStatement.executeQuery();
		itemList = makeItemList(itemResults);

		return itemList;
	}

	public ItemBean getItem(String itemId) throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		ItemBean item = new ItemBean();

		setSchema();

		searchStatement = con.prepareStatement(SEARCH_QUERY);
		searchStatement.setString(1, itemId);

		itemResults = searchStatement.executeQuery();

		if (itemResults.next()) {
			item = setItemBean(itemResults);
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

	/**
	 * Checks the input string against its value in a map and returns the mapped
	 * value. If there is no matching mapped value, an exception is thrown. This
	 * could indicate the user replaced the value of the options.
	 * 
	 * @param order
	 *            input string from the user from a select element
	 * @return the sort parameter.
	 */
	private String getSort(String order) {

		String sanitizedOrder = orderMap.get(order);

		if (sanitizedOrder != null)
			return orderMap.get(order);
		else
			throw new IllegalArgumentException("Attempt to inject SQL Detected.");

	}

}
