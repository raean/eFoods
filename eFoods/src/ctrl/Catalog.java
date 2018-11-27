package ctrl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CategoryBean;
import model.Engine;
import model.ItemBean;

/**
 * Servlet implementation class Catalog
 */
@WebServlet("/Catalog.do")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Regular instantiation before anything occurs:
		Engine engine = Engine.getInstance();
		HttpSession session = request.getSession();
		request.setAttribute("cart", session.getAttribute("cart"));
		request.setAttribute("sortBy", "NONE");

		// We get the categories that exist to populate the user page with options.
		try {
			List<CategoryBean> result = engine.getAllCategories();
			request.setAttribute("catalogList", result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If the sort by button is clicked:
		if (request.getParameter("sortByButton") != null) {
			String sortBy = request.getParameter("sortBy");
			request.setAttribute("sortBy", sortBy);
			// if a catalog was selected sort the catalog items specifically.
			if (request.getParameter("catalogId") != null && !request.getParameter("catalogId").equals("")) {

				String catalogId = (String) request.getParameter("catalogId");
				try {
					// Lets the user know which category we are looking at.
					request.setAttribute("selectedCatalogName", engine.getCategory(catalogId).getName());
					request.setAttribute("catalogId", catalogId);
					List<ItemBean> itemList = engine.getCategoryItems(catalogId, sortBy);
					request.setAttribute("itemList", itemList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { // If no catalog is selected, then it should sort all the items
				try {
					// Lets the user know which category we are looking at.
					request.setAttribute("selectedCatalogName", "All items");
					request.setAttribute("catalogId", null);
					List<ItemBean> itemList = engine.getAllItems(sortBy);
					request.setAttribute("itemList", itemList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (request.getParameter("cartButton") != null) { // If the add to cart item is clicked
			String sortBy = request.getParameter("sortBy");
			request.setAttribute("sortBy", sortBy);
			// We similarly resort the page
			// If a catalog was selected sort the catalog items specifically.
			if (request.getParameter("catalogId") != null && !request.getParameter("catalogId").equals("")) {

				String catalogId = (String) request.getParameter("catalogId");
				try {
					// Lets the user know which category we are looking at.
					request.setAttribute("selectedCatalogName", engine.getCategory(catalogId).getName());
					request.setAttribute("catalogId", catalogId);
					List<ItemBean> itemList = engine.getCategoryItems(catalogId, sortBy);
					request.setAttribute("itemList", itemList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					// Lets the user know which category we are looking at.
					request.setAttribute("selectedCatalogName", "All items");
					request.setAttribute("catalogId", null);
					List<ItemBean> itemList = engine.getAllItems(sortBy);
					request.setAttribute("itemList", itemList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Map<String, Integer> cart = (Map<String, Integer>) request.getSession().getAttribute("cart");
			String item = request.getParameter("hiddenItemBeanId");
			String quantity = request.getParameter("addQuantity");
			try {
				Map<String, Integer> newCart = engine.addItemToCart(cart, item, quantity);
				request.getSession().setAttribute("cart", newCart);
				request.setAttribute("sortBy", sortBy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (request.getParameter("catalogId") == null) {
				try {
					List<ItemBean> itemList = engine.getAllItems();
					request.setAttribute("itemList", itemList);
					request.setAttribute("selectedCatalogName", "All items");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				String catalogId = request.getParameter("catalogId");
				request.setAttribute("catalogId", catalogId);
				try {
					List<ItemBean> itemList = engine.getCategoryItems(catalogId);
					request.setAttribute("itemList", itemList);
					request.setAttribute("selectedCatalogName", engine.getCategory(catalogId).getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.getServletContext().getRequestDispatcher("/Catalog.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
