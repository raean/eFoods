package ctrl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin.do")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Engine model = Engine.getInstance();
		ServletContext context = this.getServletContext();

		List<Integer> timeBetweenAdd = (List<Integer>) context.getAttribute("timeBetweenAdd");
		List<Integer> timeBetweenCheckout = (List<Integer>) context.getAttribute("timeBetweenCheckout");

		int averageAdd = model.getAverageTime(timeBetweenAdd);
		int averageCheckout = model.getAverageTime(timeBetweenCheckout);

		this.getServletContext().getRequestDispatcher("/Admin.jspx").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
