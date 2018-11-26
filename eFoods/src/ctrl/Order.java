package ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import model.CustomerBean;
import model.Engine;
import model.OrderBean;

@WebServlet("/Order.do")
public class Order extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// We have to set an attribute in the checkout.java called "orderName" and if
		// that's null, then the user
		// got the page without completing an order and we need to let them know to go
		// awy.
		Engine model = Engine.getInstance();
		HttpSession session = request.getSession();
		CustomerBean customer = (CustomerBean) session.getAttribute("customer");
		session.setAttribute("cart", new HashMap<String, Integer>());

		try {
			Map<String, OrderBean> previousOrders = model.getCustomerOrders(customer);

			if (!previousOrders.isEmpty()) {
				TreeMap<String, OrderBean> ordersTree = (TreeMap<String, OrderBean>) previousOrders;
				OrderBean lastOrder = ordersTree.lastEntry().getValue();
				session.setAttribute("lastOrder", lastOrder);
				request.setAttribute("orderName", ordersTree.lastEntry().getKey());
			}

			session.setAttribute("previousOrders", previousOrders);

		} catch (JAXBException e) {
			response.getWriter().write("Fatal error " + e.getMessage());
		}
		this.getServletContext().getRequestDispatcher("/Order.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
