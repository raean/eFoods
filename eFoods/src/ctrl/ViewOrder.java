package ctrl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.OrderBean;

/**
 * Servlet implementation class ViewOrder
 */
@WebServlet("/ViewOrder.do")
public class ViewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String orderFileName = request.getParameter("order");
		boolean authenticated = (boolean) session.getAttribute("authenticated");

		if (orderFileName != null && authenticated) {

			Map<String, OrderBean> orders = (Map<String, OrderBean>) session.getAttribute("previousOrders");
			OrderBean requestedOrder = orders.get(request.getParameter("order"));

			request.setAttribute("requestedOrder", requestedOrder);
			request.setAttribute("orderFileName", orderFileName);

			this.getServletContext().getRequestDispatcher("/ViewOrder.jspx").forward(request, response);

		} else {
			response.getWriter().write("You are not authorized to view this page");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
