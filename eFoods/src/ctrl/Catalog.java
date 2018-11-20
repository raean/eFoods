package ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryBean;
import model.Engine;
import model.ItemBean;

/**
 * Servlet implementation class Catalog
 */
@WebServlet("/Catalog.do")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Regular instantiation:
		request.getSession(true);
		Engine engine = Engine.getInstance();
		
		// We get the categories that exist to populate the user page with options.
		try {
			List<CategoryBean> result = engine.getAllCategories();
			request.setAttribute("catalogList", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// We check if the user is coming for a catalog option selection. Populate the items only of that selected category.
		if (request.getParameter("catalogId") != null) {
			String catId = request.getParameter("catalogId");
			request.setAttribute("catalogId", catId);
			try {
				request.setAttribute("selectedCatalogName", engine.getCategory(catId).getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<ItemBean> itemList;
			try {
				itemList = engine.getCategoryItems(catId);
				request.setAttribute("itemList", itemList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			request.setAttribute("selectedCatalogName", "All items");
			try {
				List<ItemBean> itemList = engine.getAllItems();
				request.setAttribute("itemList", itemList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// If the sorted button is clicked, then get the previously selected catalog ID and return the sorted category.
		if (request.getParameter("sortByButton") != null) {
			if (!request.getParameter("catalogId").equals("")) { // If a catalog was selected, sort the catalog items specifically.
				String catId = request.getParameter("catalogId");
				System.out.println(catId + ",  Here it be!");
				request.setAttribute("catalogId", catId);
				try {
					request.setAttribute("selectedCatalogName", engine.getCategory(catId).getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { // If no catalog is selected, sort all the items.
				request.setAttribute("selectedCatalogName", "All items");
				try {
					List<ItemBean> itemList = engine.getAllItems();
					request.setAttribute("itemList", itemList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// If the add to cart button is clicked, then we must add it to the cart:
		if (request.getParameter("cartButton") != null) {
			Map<String, Integer> cart = (Map<String, Integer>) request.getSession().getAttribute("cart");
			String item = request.getParameter("hiddenItemBeanId");
			String quantity = request.getParameter("addQuantity");
			try {
				Map<String, Integer> newCart = engine.addItemToCart(cart, item, quantity);
				request.getSession().setAttribute("cart", newCart);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//request.setAttribute("cart", newCart);
		}
				
		this.getServletContext().getRequestDispatcher("/Catalog.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
