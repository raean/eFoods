package adhoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import model.Engine;
import model.ItemBean;

/**
 * Servlet Filter implementation class reference
 */
@WebFilter({ "/Cart.do" })
public class Reference implements Filter {

	private static Map<String, String> crossRef = new TreeMap<>();
	
	public static Map<String, String> getCrossRef() {
		return crossRef;
	}
	
	public static void setCrossRef(Map<String, String> crossRef) {
		Reference.crossRef = crossRef;
	}
	
	public void refBuilder() {
		
		Map<String, String> temp = new TreeMap<>();
		temp.put("1409S413", "2002H712"); 
		temp.put("0905A365", "0905A811");
		temp.put("0905A112", "2002H341");
		temp.put("2002H136", "2910h074");
		temp.put("1409S381", "0905A363");
		temp.put("2002H063", "0905A044");
		temp.put("0905A343", "2910h244");
		temp.put("1409S974", "1409S811");
		temp.put("2910h785", "2002H924");
		setCrossRef(temp);
	}
	public String itemToRef(String itemId) {
		String result = "";
		refBuilder();
		Map<String, String> items = getCrossRef();
		if(items.containsKey(itemId)) {
			result = items.get(itemId);
		}
		return result;
	}
	

	/**
	 * Default constructor.
	 */
	public Reference() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		Engine engine = Engine.getInstance();
		Map<String, Integer> cart = (Map<String, Integer>) hreq.getSession().getAttribute("cart");
		
		
		boolean inCart = false;
		//items in to ref
		for(String id : cart.keySet()) {
			if(itemToRef(id) != "") {
				inCart = true;
			}
		}
		
	
		if (hreq.getRequestURI().matches(".*Cart.do")) {
			if (inCart == true) 
			{
				List<ItemBean> iBeans = new ArrayList<>();
				List<ItemBean> sBeans = new ArrayList<>();
				
				for(String items : cart.keySet()) {
					if(getCrossRef().containsKey(items)) {
						try {
							iBeans.add(engine.getItem(items));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				for(ItemBean items : iBeans) {
					try {
						sBeans.add(engine.getItem(itemToRef(items.getNumber())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				Map<ItemBean, Integer> viewableCart = null;
				try {
					viewableCart = engine.makeViewableCart(cart);
					hreq.setAttribute("viewableCart", viewableCart);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (hreq.getParameter("updateCartButton") != null) {
					String[] itemIds = hreq.getParameterValues("itemId");
					String[] itemQuantities = hreq.getParameterValues("quantityInput");
					String[] deleteCheckboxes = null;
					System.out.println(itemQuantities.length + " " + itemIds.length);
					if (hreq.getParameterValues("deleteCheckbox") != null) {
						deleteCheckboxes = hreq.getParameterValues("deleteCheckbox");
					}
					Map<String, Integer> newCart = null;
					try {
						newCart = engine.updateCart(cart, itemIds, itemQuantities, deleteCheckboxes);
						hreq.getSession().setAttribute("cart", newCart);
						try {
							viewableCart = engine.makeViewableCart(newCart);
							hreq.setAttribute("viewableCart", viewableCart);
						} catch (Exception e) {
							hreq.setAttribute("error", "Please enter a valid input");
						}
					} catch (Exception e) {
						hreq.setAttribute("error", "Please enter a valid input");
					}
				}

				if (engine.isCartEmpty(cart)) {
					hreq.setAttribute("itemsCost", 0.0);
					hreq.setAttribute("hstAmount", 0.0);
					hreq.setAttribute("shippingCost", 0.0);
				} else {
					double itemsCost = engine.getItemsCost(viewableCart);
					double hstAmount = engine.getHstAmount(viewableCart);
					double shippingCost = engine.getShippingCost(viewableCart);
					hreq.setAttribute("itemsCost", itemsCost);
					hreq.setAttribute("hstAmount", hstAmount);
					hreq.setAttribute("shippingCost", shippingCost);
					hreq.setAttribute("suggestedList", iBeans);
					hreq.setAttribute("suggestedCart", sBeans);
				}

				hreq.setAttribute("cart", hreq.getAttribute("cart"));
				hreq.getServletContext().getRequestDispatcher("/SuggestedCart.jspx").forward(request, response);
			} 
			
			else 
			{
				chain.doFilter(request, response);
			}

		} else if (hreq.getRequestURI().matches("/eFoods/Search.do")) {
			if (request.getParameter("cartButton") != null) { // If the add to cart item is clicked
				// We similarly resort the page

				
				String item = request.getParameter("hiddenItemNo");
				String quantity = request.getParameter("addQuantity");
				try {
					Map<String, Integer> newCart = engine.addItemToCart(cart, item, quantity);
					hreq.getSession().setAttribute("cart", newCart);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				hreq.getServletContext().getRequestDispatcher("/Cart.do").forward(request, response);
			}else {
				chain.doFilter(request, response);
			}
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
