package ctrl;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Order.do")
public class Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// We have to set an attribute in the checkout.java called "orderName" and if that's null, then the user
		// got the page without completing an order and we need to let them know to go awy.
		HttpSession session = request.getSession();
		session.setAttribute("cart", new HashMap<String, Integer>());
		this.getServletContext().getRequestDispatcher("/Order.jspx").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
