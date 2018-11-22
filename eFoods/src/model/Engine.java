package model;

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
	private static final double SHIPPING_FEE = 5.0;
	private static final double HST = 0.13;

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
	
	public List<ItemBean> getAllItems(String sortBy) throws Exception{
		return itemDao.getAllItems(sortBy);
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

	public Map<String, Integer> addItemToCart(Map<String, Integer> cart, String itemNo, String quantity) throws Exception {
		int quantityInt = Integer.parseInt(quantity);
		
		if (cart.containsKey(itemNo)) {
			cart.put(itemNo, cart.get(itemNo)+quantityInt);

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

	/**
	 * This method is in support of the view. It creates a cart that is viewable
	 * as it contains information such as the price, name, etc. of the item as opposed
	 * to the cart that is stored in the session that only contains IDs.
	 * It gets the rest of the information using the item ID string by calling the
	 * getItem method in this Engine.
	 * @param cart is the cart within the session.
	 * @return is a Map that is viewable since it has the entire ItemBean along with the Integer
	 * quantity.
	 * @throws Exception is thrown if there is an issue getting the item with the ItemNo id.
	 */
	public Map<ItemBean, Integer> makeViewableCart(Map<String, Integer> cart) throws Exception {
		
		Map<ItemBean, Integer> viewableCart = new HashMap<ItemBean, Integer>();
		
		for (String s : cart.keySet()) {
			viewableCart.put(this.getItem(s), cart.get(s));
		}
		
		return viewableCart;
	}

	/**
	 * 
	 * @param cart
	 * @param itemIds
	 * @param itemQuantities
	 * @return
	 */
	public Map<String, Integer> updateCart(Map<String, Integer> cart, String[] itemIds, String[] itemQuantities, String[] deleteCheckboxes) {
		for (int i = 0 ; i < itemIds.length ; i++) {
			if (0 == Integer.parseInt(itemQuantities[i])) {
				cart.remove(itemIds[i]);
			} else if (cart.get(itemIds[i]) != Integer.parseInt(itemQuantities[i])) {
				cart.put(itemIds[i], Integer.parseInt(itemQuantities[i]));
			}
		}

		if (deleteCheckboxes != null) {
			for (String s : deleteCheckboxes) {
				if (cart.containsKey(s)) {
					cart.remove(s);
				}
			}
		}
		
		return cart;
	}
	
	/**
	 * Checks if the session's cart is empty.
	 * @param cart
	 * @return
	 */
	public boolean isCartEmpty(Map<String, Integer> cart) {
		return cart.isEmpty();
	}

	/**
	 * Returns the cost of all items, cost and HST. 
	 * @param cart
	 * @return
	 */
	public double getItemsCost(Map<ItemBean, Integer> cart) {
		double itemsCost = 0;
		for (ItemBean i : cart.keySet()) {
			itemsCost = itemsCost + i.getPrice()*cart.get(i);
		}
		return itemsCost;
	}

	/**
	 * Get's HST amount.
	 * @param cart
	 * @return
	 */
	public double getHstAmount(Map<ItemBean, Integer> cart) {
		return this.getItemsCost(cart)*HST;
	}

	/**
	 * Get's shipping cost.
	 * @param cart
	 * @return
	 */
	public double getShippingCost(Map<ItemBean, Integer> cart) {
		double itemsCost = this.getItemsCost(cart);
		if (itemsCost >= 100) {
			return 0;
		} else {
			return SHIPPING_FEE;
		}
	}	

}
