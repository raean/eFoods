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

	// Queries
	public static final String SEARCH_QUERY = "SELECT * FROM ITEM WHERE LOWER(NAME) LIKE LOWER(?)";
	public static final String ADVANCE_QUERY = SEARCH_QUERY + " AND PRICE BETWEEN ? AND ?";
	public static final String GET_ITEM_QUERY = "SELECT * FROM ITEM WHERE NUMBER = ?";

	// This helps prevent SQL injection attacks on the ORDER BY statement.
	public static final String[] SORT_OPTIONS = { "NUMBER", "PRICE ASC", "PRICE DESC", "NAME ASC", "NAME DESC" };
	public static final String[] USER_SORT_INPUT = { "NONE", "Price - Low to High", "Price - High to Low", "A to Z",
			"Z to A" };
	private HashMap<String, String> orderMap;

	private final double MAX_RANGE_VALUE = 1000000.00;

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

	// Sets schema for every database call.
	private void setSchema() throws SQLException {
		Statement setRoumani;
		setRoumani = con.createStatement();
		setRoumani.executeUpdate(SET_SCHEMA);
	}

	/**
	 * Queries for items that contain the search query.
	 * 
	 * @param searchInputValue
	 * @return a list of itembeans that match.
	 * @throws Exception
	 */
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

	/**
	 * Does an advanced search that allows for more constraints on what is returned.
	 * 
	 * @param searchInputValue
	 * @param minCost
	 * @param maxCost
	 * @param sortBy
	 * @return a list of items that match the entered arguments.
	 * @throws Exception
	 */
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
			searchStatement.setDouble(2, 0.0);
		} else {
			searchStatement.setDouble(2, Double.parseDouble(minCost));
		}

		if (maxCost.isEmpty()) {
			searchStatement.setDouble(3, MAX_RANGE_VALUE);
		} else {
			searchStatement.setDouble(3, Double.parseDouble(maxCost));
		}

		searchStatement.setString(1, "%" + searchInputValue + "%");
		System.out.println(searchStatement);
		itemResults = searchStatement.executeQuery();
		itemList = makeItemList(itemResults);
		return itemList;

	}

	/**
	 * Retrieves all items from the database.
	 * 
	 * @return a list of all items.
	 * @throws Exception
	 */
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

	/**
	 * Gets all items in the database in the order of the entered argument.
	 * 
	 * @param sortBy
	 *            a select option from html.
	 * @return all items sorted.
	 * @throws Exception
	 */
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

	/**
	 * Gets a single item from the database as an ItemBean, based on it's unique
	 * number.
	 * 
	 * @param itemId
	 *            a valid item number.
	 * @return a single ItemBean corresponding to the item retrieved.
	 * @throws Exception
	 */
	public ItemBean getItem(String itemId) throws Exception {
		PreparedStatement searchStatement;

		ResultSet itemResults;
		ItemBean item = new ItemBean();

		setSchema();

		searchStatement = con.prepareStatement(GET_ITEM_QUERY);
		searchStatement.setString(1, itemId);

		itemResults = searchStatement.executeQuery();

		if (itemResults.next()) {
			item = setItemBean(itemResults);
		} else {
			throw new IllegalArgumentException(itemId + " is not a valid item number.");
		}

		return item;
	}

	// Loops through the ResultSet and populates the list of ItemBeans
	private List<ItemBean> makeItemList(ResultSet r) throws SQLException {

		List<ItemBean> itemList = new ArrayList<>();

		while (r.next()) {
			ItemBean item = setItemBean(r);
			itemList.add(item);
		}

		return itemList;
	}

	// Sets each attribute on the ItemBean from the database values.
	private ItemBean setItemBean(ResultSet r) throws SQLException {
		ItemBean item = new ItemBean();

		item.setUnit(r.getString("UNIT"));
		item.setCatId(r.getInt("CATID"));
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
