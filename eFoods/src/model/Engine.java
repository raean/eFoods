package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return itemDao.getItem(itemId);
	}

	/**
	 * Returns every available item as a list.
	 * 
	 * @return a list containing every available item.
	 * @throws Exception
	 *             if an SQL exception is thrown.
	 */
	public List<ItemBean> getAllItems() throws Exception {
		return itemDao.getAllItems();
	}

	/**
	 * Returns a single categorybean containing all the information about that
	 * category.
	 * 
	 * @param catId
	 *            a valid category id
	 * @return
	 * @throws Exception
	 *             if an SQL exception is thrown.
	 */
	public CategoryBean getCategory(String catId) throws Exception {
		return catDao.getCategory(Integer.parseInt(catId));
	}

	/**
	 * Returns a list of every category.
	 * 
	 * @return a list of every category.
	 * @throws Exception
	 *             if an SQL exception is thrown.
	 */
	public List<CategoryBean> getAllCategories() throws Exception {
		return catDao.getAllCategories();
	}

	/**
	 * Creates a list of items that are in the entered category.
	 * 
	 * @param catId
	 *            a valid category Id.
	 * @return a list of items in the entered category ID.
	 * @throws Exception
	 *             if an SQL exception is thrown.
	 */
	public List<ItemBean> getCategoryItems(String catId) throws Exception {
		CategoryBean category = getCategory(catId);
		List<ItemBean> items = itemDao.getAllItems();
		List<ItemBean> categoryItems = new ArrayList<>();

		for (ItemBean item : items) {
			if (category.getId() == item.getCatId()) {
				categoryItems.add(item);
			}
		}

		return categoryItems;
	}

	public List<ItemBean> getCategoryItems(String catId, String sortBy) throws Exception {
		CategoryBean category = getCategory(catId);
		List<ItemBean> items = itemDao.getAllItems(sortBy);
		List<ItemBean> categoryItems = new ArrayList<>();

		for (ItemBean item : items) {
			if (category.getId() == item.getCatId()) {
				categoryItems.add(item);
			}
		}

		return categoryItems;
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

		if (searchInputValue.isEmpty()) {
			throw new IllegalArgumentException("Search query is empty.");
		}

		List<ItemBean> result = itemDao.search(searchInputValue);

		if (result.isEmpty()) {
			throw new Exception("No results returned.");
		}

		return result;
	}

	public List<ItemBean> doAdvanceSearch(String searchInputValue, String minCost, String maxCost, String sortBy)
			throws Exception {

		if (searchInputValue.isEmpty()) {
			throw new IllegalArgumentException("Search query is empty.");
		}

		List<ItemBean> result = itemDao.advanceSearch(searchInputValue, minCost, maxCost, sortBy);

		if (result.isEmpty()) {
			throw new Exception("No results returned.");
		}

		return result;
	}

	/**
	 * This method adds an item to the shopping cart within the session. If the item
	 * exists, it appends the original amount with the new quantity. If the item
	 * doesn't exist, it creates the item with the new quantity.
	 * 
	 * @param cart
	 *            is the cart within the session.
	 * @param item
	 *            is the item to add or append.
	 * @param quantity
	 *            is the amount of the item to be added or appended by.
	 * @return the Map of the cart after alterations (addition).
	 * @throws Exception
	 */

	public Map<String, Integer> addItemToCart(Map<String, Integer> cart, String itemNo, String quantity)
			throws Exception {
		int quantityInt = Integer.parseInt(quantity);

		if (cart.containsKey(itemNo)) {
			cart.put(itemNo, cart.get(itemNo) + quantityInt);

		} else {
			cart.put(itemNo, quantityInt);
		}

		return cart;

	}

	/**
	 * This method removes all of an item from the cart within the session. If it
	 * does not exist, it throws an exception stating so.
	 * 
	 * @param cart
	 *            is the cart within the session.
	 * @param item
	 *            is the item to be removed.
	 * @return the Map of the cart after alterations (removal).
	 */
	public Map<String, Integer> ItemFromCart(Map<String, Integer> cart, ItemBean item) {
		if (cart.containsKey(item)) {
			cart.remove(item);
		} else {
			throw new IllegalArgumentException("That item is not in the cart!");
		}
		return cart;
	}

	public Map<ItemBean, Integer> viewableCart(Map<String, Integer> cart) throws Exception {

		Map<ItemBean, Integer> viewableCart = new HashMap<ItemBean, Integer>();

		for (String s : cart.keySet()) {
			viewableCart.put(this.getItem(s), cart.get(s));
		}

		return viewableCart;
	}

	// public void updateCart(Map<ItemBean, Integer> cart, ItemBean item, int
	// quantity) {
	// if (cart.containsKey(item)) {
	// cart.put(item, cart.get(item)-quantity);
	// if (cart.get(key))
	// } else {
	// throw new IllegalArgumentException("That item is not in the cart!");
	// }
	// }

	public OrderBean makeOrder(Map<ItemBean, Integer> viewableCart, CustomerBean customer) throws Exception {
		OrderBean order = new OrderBean();
		List<ItemBean> itemList = new ArrayList<>();
		double HST, total, grandTotal, shipping;

		total = 0.0;

		for (ItemBean item : viewableCart.keySet()) {
			item.setQuantity(viewableCart.get(item));
			item.setExtended(item.getQuantity() * item.getPrice());
			itemList.add(item);
			total += item.getExtended();
		}

		if (total >= 100) {
			shipping = 0.0;
		} else {
			shipping = 5.0;
		}

		HST = (total + shipping) * 0.13;
		grandTotal = total + HST + shipping;

		order.setItems(itemList);
		order.setSubmitted(this.getTime());
		order.setCustomer(customer);

		order.setTotal(total);
		order.setHST(HST);
		order.setShipping(shipping);
		order.setGrandTotal(grandTotal);

		return order;

	}

	/**
	 * Gives the current date of the server as "yyyy-mm-dd"
	 * 
	 * @return the date formatted as "yyyy-mm-dd"
	 */
	private String getTime() {
		LocalDate currTime = LocalDate.now();
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String formattedTime = currTime.format(timeFormat);
		return formattedTime;
	}

}
