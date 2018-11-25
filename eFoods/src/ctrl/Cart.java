package ctrl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CustomerBean;
import model.Engine;
import model.ItemBean;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart.do")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Engine engine = Engine.getInstance();
		HttpSession session = request.getSession();
		
		Map<String, Integer> cart = (Map<String, Integer>) request.getSession().getAttribute("cart");
		Map<ItemBean, Integer> viewableCart = null;
		try {
			viewableCart = engine.makeViewableCart(cart);
			request.setAttribute("viewableCart", viewableCart);		
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		if (request.getParameter("updateCartButton")!=null) {
			String[] itemIds = request.getParameterValues("itemId");
			String[] itemQuantities = request.getParameterValues("quantityInput");
			String[] deleteCheckboxes = null;
			System.out.println(itemQuantities.length + " " + itemIds.length);
			if ( request.getParameterValues("deleteCheckbox") != null ) {
				deleteCheckboxes = request.getParameterValues("deleteCheckbox");
			}
			Map<String, Integer> newCart = engine.updateCart(cart, itemIds, itemQuantities, deleteCheckboxes);
			request.getSession().setAttribute("cart", newCart);
			try {
				viewableCart = engine.makeViewableCart(newCart);
				request.setAttribute("viewableCart", viewableCart);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (engine.isCartEmpty(cart)) {
			request.setAttribute("itemsCost", 0.0);
			request.setAttribute("hstAmount", 0.0);
			request.setAttribute("shippingCost", 0.0);
		} else {
			double itemsCost = engine.getItemsCost(viewableCart);
			double hstAmount = engine.getHstAmount(viewableCart);
			double shippingCost = engine.getShippingCost(viewableCart);
			request.setAttribute("itemsCost", itemsCost);
			request.setAttribute("hstAmount", hstAmount);
			request.setAttribute("shippingCost", shippingCost);
		}
		
		if (session.getAttribute("customer") != null) {
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			request.setAttribute("username", customer.getName().toString().split(" ")[0]);
		}
		
		this.getServletContext().getRequestDispatcher("/Cart.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
