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

import model.Engine;
import model.ItemBean;

@WebServlet("/Search.do")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Engine engine = Engine.getInstance();
		String searchInputValue = request.getParameter("searchInput");
		HttpSession session = request.getSession();
		request.setAttribute("cart", session.getAttribute("cart"));
		
		if (request.getParameter("searchButton") != null) {
			if (!searchInputValue.isEmpty()) {
				try {
					List<ItemBean> result = engine.doSearch(searchInputValue);
					request.setAttribute("result",  result);
					request.setAttribute("searchInputValue", searchInputValue);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			this.getServletContext().getRequestDispatcher("/Search.jspx").forward(request, response);
		} else if (request.getParameter("advancedSearchButton") != null) {
			
			String min = request.getParameter("minInput");
			String max = request.getParameter("maxInput");
			String sort = request.getParameter("sortBy");
			if (!searchInputValue.isEmpty()) {
				try {
					List<ItemBean> result = engine.doAdvanceSearch(searchInputValue, min, max, sort);
					request.setAttribute("searchInputValue", searchInputValue);
					request.setAttribute("result",  result);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			this.getServletContext().getRequestDispatcher("/Search.jspx").forward(request, response);
		} else if (request.getParameter("cartButton") != null) { // If the add to cart item is clicked
			// We similarly resort the page 
			
			Map<String, Integer> cart = (Map<String, Integer>) request.getSession().getAttribute("cart");
			String item = request.getParameter("hiddenItemNo");
			String quantity = request.getParameter("addQuantity");
			try {
				Map<String, Integer> newCart = engine.addItemToCart(cart, item, quantity);
				request.getSession().setAttribute("cart", newCart);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			response.sendRedirect("Cart.do");
		}
		//this.getServletContext().getRequestDispatcher("/Search.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
