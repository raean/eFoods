package model;

import java.util.List;

/**
 * Back-end logic singleton for the webstore app. Mainly functions to retrieve
 * information from the database.
 *
 */
public class Engine {

	private static Engine instance = null;
	private ItemDAO itemDao;
	private CategoryDAO catDao;

	private Engine() {
		itemDao = new ItemDAO();
		catDao = new CategoryDAO();
	}

	/**
	 * Gets the instance of the singleton class.
	 * 
	 * @return the single Engine object.
	 */
	public static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
		}

		return instance;
	}

	/**
	 * Searches for items that match an input, and then returns the list.
	 * 
	 * @param searchInputValue
	 *            a string to search from.
	 * @return a list containing itemBeans that contain a substring of the search
	 *         string.
	 * @throws Exception
	 *             if there is an SQL error or if the list returned is empty.
	 */
	public List<ItemBean> doSearch(String searchInputValue) throws Exception {

		List<ItemBean> result = itemDao.search(searchInputValue);

		if (result.isEmpty()) {
			throw new Exception("No results returned.");
		}

		return result;
	}

	public List<CategoryBean> getCategories() throws Exception {

		List<CategoryBean> categories = catDao.getAllCategories();
		return categories;

	}
	
	

	public List<ItemBean> getCategoryItems() throws Exception {
		return null;
	}

	/**
	 * Returns a single itemBean matched with the unique item code.
	 * 
	 * @param itemId
	 *            a valid 8 character ID.
	 * @return an ItemBean corresponding to the entered item id.
	 * @throws Exception
	 *             If the itemId does not correspond to any item, or if there is a
	 *             backend exception.
	 */
	public ItemBean getItem(String itemId) throws Exception {
		ItemBean item = itemDao.getItem(itemId);
		return item;
	}

	public CategoryBean getCategory(String catId) throws Exception {
		CategoryBean category = catDao.getCategory(Integer.parseInt(catId));
		return category;
	}

}
