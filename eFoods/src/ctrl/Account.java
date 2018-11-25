package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CustomerBean;
import model.Engine;

/**
 * Servlet implementation class Account
 */
@WebServlet("/Account.do")
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Engine engine = Engine.getInstance();
		HttpSession session = request.getSession();
		request.setAttribute("cart", session.getAttribute("cart"));
		if (session.getAttribute("customer") != null) {
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			request.setAttribute("username", customer.getName().toString().split(" ")[0]);
		}
		this.getServletContext().getRequestDispatcher("/Account.jspx").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
