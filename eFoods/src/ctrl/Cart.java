package ctrl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ItemBean;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart.do")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("cartButton") != null) {
			// waiting for adam to make a add to cart method that returns a set of item beans
		}
		Map<ItemBean, Integer> cart = (Map<ItemBean, Integer>) request.getSession().getAttribute("cart");
		request.setAttribute("cart", cart);
		System.out.println(cart.isEmpty());
		this.getServletContext().getRequestDispatcher("/Cart.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
