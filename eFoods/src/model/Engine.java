package model;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Back-end logic singleton for the webstore app. Returns data from the DAO and
 * handles the business logic of the eFoods application.
 *
 */
public class Engine {

	private static Engine instance = null;
	private ItemDAO itemDao;
	private CategoryDAO catDao;

	private long fileCount;
	private static final String PO_PATH = System.getProperty("user.home") + "/PO/";
	private static final String IN_PO = PO_PATH + "inPO/";
	private static final String OUT_PO = PO_PATH + "outPO";

	private JAXBContext orderContext;
	private Marshaller orderMarshaller;
	private Unmarshaller orderUnMarshaller;

	private static final double SHIPPING_FEE = 5.0;
	private static final double HST = 0.13;
	private static final String itemMatcher = "([0-9]{4}[a-z|A-Z][0-9]{3})";

	private Engine() {
		this.itemDao = new ItemDAO();
		this.catDao = new CategoryDAO();
		initPoFolder();

		try {
			this.orderContext = JAXBContext.newInstance(OrderBean.class);
			this.orderMarshaller = orderContext.createMarshaller();
			this.orderUnMarshaller = orderContext.createUnmarshaller();

			this.orderMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (JAXBException e) {
			System.err.println("Fatal error " + e.getMessage());
		}

	}

	private void initPoFolder() {
		File poDir = new File(PO_PATH);
		File inDir = new File(IN_PO);
		File outDir = new File(OUT_PO);

		poDir.mkdirs();
		inDir.mkdir();
		outDir.mkdir();

		this.fileCount = inDir.listFiles().length + outDir.listFiles().length;
	}

	// TESTING METHOD FOR CREATING ORDER FILES ON DISK
	public void testPathNonsense() throws Exception {
		OrderBean order;
		CustomerBean customer = new CustomerBean();

		customer.setAccount("adamzis");
		customer.setName("Adam Adjindji");

		ItemBean item1 = getItem("0905A044");
		ItemBean item2 = getItem("0905A112");
		ItemBean item3 = getItem("0905A123");

		Map<ItemBean, Integer> viewableCart = new HashMap<>();
		viewableCart.put(item1, 3);
		viewableCart.put(item2, 1);
		viewableCart.put(item3, 2);

		order = makeOrder(viewableCart, customer);

		checkOut(order);

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
	 * Returns every available item as a list, sorted by the input.
	 * 
	 * @param sortBy
	 *            an input from the select tag in html
	 * @return a list containing every available item sorted by what the user wants.
	 * @throws Exceptionif
	 *             an SQL exception is thrown.
	 */
	public List<ItemBean> getAllItems(String sortBy) throws Exception {
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
		List<ItemBean> result = new ArrayList<>();
		if (searchInputValue.isEmpty()) {
			throw new IllegalArgumentException("");
		}
		if (searchInputValue.matches(itemMatcher)) {
			result.add(getItem(searchInputValue));
		} else {
			result = itemDao.search(searchInputValue);

			if (result.isEmpty()) {
				throw new Exception("No results found.");
			}
		}
		return result;
	}

	public List<ItemBean> doAdvanceSearch(String searchInputValue, String minCost, String maxCost, String sortBy)
			throws Exception {
		List<ItemBean> result = new ArrayList<>();
		if (searchInputValue.isEmpty()) {
			throw new IllegalArgumentException("Search query is empty.");
		}
		if (searchInputValue.matches(itemMatcher)) {
			result = itemDao.advanceSearch((getItem(searchInputValue).getName()), minCost, maxCost, sortBy);
		} else {
			System.out.println("IN else");
			result = itemDao.advanceSearch(searchInputValue, minCost, maxCost, sortBy);
			System.out.println(result);
		}
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

	/**
	 * This method is in support of the view. It creates a cart that is viewable as
	 * it contains information such as the price, name, etc. of the item as opposed
	 * to the cart that is stored in the session that only contains IDs. It gets the
	 * rest of the information using the item ID string by calling the getItem
	 * method in this Engine.
	 * 
	 * @param cart
	 *            is the cart within the session.
	 * @return is a Map that is viewable since it has the entire ItemBean along with
	 *         the Integer quantity.
	 * @throws Exception
	 *             is thrown if there is an issue getting the item with the ItemNo
	 *             id.
	 */
	public Map<ItemBean, Integer> makeViewableCart(Map<String, Integer> cart) throws Exception {

		Map<ItemBean, Integer> viewableCart = new LinkedHashMap<ItemBean, Integer>();

		for (String s : cart.keySet()) {
			viewableCart.put(this.getItem(s), cart.get(s));
		}

		return viewableCart;
	}

	/**
	 * Creates an OrderBean from the viewableCart and a customerBean. The OrderBean
	 * contains the customerBean and a list of ItemBeans where the quantity and
	 * total price (extended) are set. The orderBean also contains shipping, HST,
	 * total, and grand total pricing easily accessible.
	 * 
	 * TODO: Remove calculations into their own methods.
	 * 
	 * @param viewableCart
	 *            a non empty viewableCart.
	 * @param customer
	 *            a non-empty customerBean
	 * @return
	 * @throws Exception
	 */
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
		order.setSubmitted(this.getDate());
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
	private String getDate() {
		LocalDate currTime = LocalDate.now();
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String formattedTime = currTime.format(timeFormat);
		return formattedTime;
	}

	/**
	 * Turns an orderBean into an XML file on disk (A recieved order). The OrderBean
	 * should be removed from the session and cart emptied after calling this
	 * method.
	 * 
	 * @param order
	 *            a populated orderBean
	 * @throws Exception
	 */
	public void checkOut(OrderBean order) throws Exception {
		String fileCountString = makeOrderId();

		order.setId(Integer.parseInt(fileCountString));
		String poName = "po" + order.getCustomer().getAccount() + "_" + fileCountString + ".xml";
		File newPo = new File(IN_PO + poName);

		newPo.createNewFile();
		orderMarshaller.marshal(order, newPo);
	}

	/**
	 * Creates a 2+ digit orderId for the orderBean. Used in the filename, and
	 * inside the P XML.
	 * 
	 * @return
	 */
	private String makeOrderId() {
		String fileCountString;
		if (++fileCount < 10) {
			fileCountString = "0" + fileCount;
		} else {
			fileCountString = Long.toString(fileCount);
		}

		return fileCountString;
	}

	/**
	 * Generates a list of customer orders based on the CustomerBean.
	 * 
	 * @param customer
	 *            a populated customerBean.
	 * @return A List of orders the customer made, may be empty if the customer has
	 *         made no orders.
	 * @throws JAXBException
	 * @throws Exception
	 */
	public Map<String, OrderBean> getCustomerOrders(CustomerBean customer) throws JAXBException {
		Map<String, OrderBean> customerOrders = new TreeMap<>();
		File inPODir[] = new File(IN_PO).listFiles();
		File outPODir[] = new File(OUT_PO).listFiles();

		for (File file : inPODir) {
			if (isCustomerOrder(file.getName(), customer.getAccount())) {
				OrderBean customerOrder = (OrderBean) orderUnMarshaller.unmarshal(file);
				customerOrders.put(file.getName(), customerOrder);
			}
		}

		for (File file : outPODir) {
			if (isCustomerOrder(file.getName(), customer.getAccount())) {
				OrderBean customerOrder = (OrderBean) orderUnMarshaller.unmarshal(file);
				customerOrders.put(file.getName(), customerOrder);
			}
		}

		return customerOrders;
	}

	public boolean isCustomerOrder(String fileName, String accountName) {
		if (fileName.contains(accountName)) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param cart
	 * @param itemIds
	 * @param itemQuantities
	 * @return
	 */
	public Map<String, Integer> updateCart(Map<String, Integer> cart, String[] itemIds, String[] itemQuantities,
			String[] deleteCheckboxes) throws Exception {
		
			for (int i = 0; i < itemIds.length; i++) {
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
	 * 
	 * @param cart
	 * @return
	 */
	public boolean isCartEmpty(Map<String, Integer> cart) {
		return cart.isEmpty();
	}

	/**
	 * Returns the cost of all items, cost and HST.
	 * 
	 * @param cart
	 * @return
	 */
	public double getItemsCost(Map<ItemBean, Integer> cart) {
		double itemsCost = 0;
		for (ItemBean i : cart.keySet()) {
			itemsCost = itemsCost + i.getPrice() * cart.get(i);
		}
		return itemsCost;
	}

	/**
	 * Get's HST amount.
	 * 
	 * @param cart
	 * @return
	 */
	public double getHstAmount(Map<ItemBean, Integer> cart) {
		return this.getItemsCost(cart) * HST;
	}

	/**
	 * Get's shipping cost.
	 * 
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
