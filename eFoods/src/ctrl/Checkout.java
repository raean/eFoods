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
import model.OrderBean;

@WebServlet("/Checkout.do")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Engine engine = Engine.getInstance();
		request.setAttribute("cart", session.getAttribute("cart"));
		if ((boolean) session.getAttribute("authenticated")) {
			Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			try {	
				// Let's get the cart from the session's information:
				Map<ItemBean, Integer> viewableCart = engine.makeViewableCart(cart);
				request.setAttribute("viewableCart", viewableCart);
				if (request.getParameter("checkoutButton") != null && !((Map<String, Integer>) session.getAttribute("cart")).isEmpty()) {
					OrderBean order = engine.makeOrder(viewableCart, customer);
					engine.checkOut(order);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.getServletContext().getRequestDispatcher("/Checkout.jspx").forward(request, response);
		} else {
			response.sendRedirect("Auth.do");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
