package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * Data access layer for the category table.
 *
 */
public class CategoryDAO {

	// Derby information
	public static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String SET_SCHEMA = "set schema roumani";
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";

	// Query strings to get categories
	public static final String ALL_CATEGORIES_QUERY = "SELECT * FROM CATEGORY";
	public static final String SINGLE_CATEGORY_QUERY = "SELECT * FROM CATEGORY WHERE ID = ?";

	private Connection con;

	// Encodes the picture bytes into a Base64 String.
	private Encoder picEncoder;

	/**
	 * Constructs the DAO, initializing the database driver and creating the
	 * encoder.
	 */
	public CategoryDAO() {

		try {
			Class.forName(DERBY_DRIVER).newInstance();
			con = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		picEncoder = Base64.getEncoder();
	}

	// Sets the schema before each database call.
	private void setSchema() throws SQLException {
		Statement setRoumani;
		setRoumani = con.createStatement();
		setRoumani.executeUpdate(SET_SCHEMA);
	}

	/**
	 * Gets all the categories in the database and returns them in a list.
	 * 
	 * @return list containing all categories
	 * @throws Exception
	 *             if an SQL occurs or if there are no categories.
	 */
	public List<CategoryBean> getAllCategories() throws Exception {
		PreparedStatement searchStatement;

		ResultSet categoryResults;
		List<CategoryBean> categoryList;

		setSchema();

		searchStatement = con.prepareStatement(ALL_CATEGORIES_QUERY);

		categoryResults = searchStatement.executeQuery();

		categoryList = makeCategoryList(categoryResults);
		return categoryList;
	}

	/**
	 * Returns the category that has the same key as the catId.
	 * 
	 * @param catId
	 *            a valid category id number.
	 * @return a CategoryBean matching the category id.
	 * @throws SQLException
	 *             if there is a database error or if there are no categories with
	 *             that number.
	 */
	public CategoryBean getCategory(int catId) throws SQLException {
		PreparedStatement searchStatement;
		ResultSet categoryResults;

		setSchema();

		searchStatement = con.prepareStatement(SINGLE_CATEGORY_QUERY);
		searchStatement.setInt(1, catId);

		categoryResults = searchStatement.executeQuery();
		categoryResults.next();

		CategoryBean category = setCategoryBean(categoryResults);
		return category;
	}

	// Loops through the result and makes the list.
	private List<CategoryBean> makeCategoryList(ResultSet r) throws SQLException {
		List<CategoryBean> categoryList = new ArrayList<>();

		while (r.next()) {
			CategoryBean category = setCategoryBean(r);

			categoryList.add(category);
		}
		return categoryList;
	}

	// Populates the category bean with the current pointer of the result set.
	private CategoryBean setCategoryBean(ResultSet r) throws SQLException {
		CategoryBean category = new CategoryBean();

		category.setDescription(r.getString("DESCRIPTION"));
		category.setName(r.getString("NAME"));
		category.setId(r.getInt("ID"));
		category.setPicture(getCategoryPicture(r.getBytes("PICTURE")));

		return category;
	}

	// Encodes the picture byte array as a Base64 string.
	private String getCategoryPicture(byte[] picture) {
		byte[] encoded = picEncoder.encode(picture);

		String encodedString = new String(encoded);

		return encodedString;
	}

}
