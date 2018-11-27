package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CategoryBean;
import model.CustomerBean;
import model.Engine;

@WebServlet(name = "Controller", urlPatterns = { "/index.html", "/Dash.do" })
public class Dash extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Engine engine = Engine.getInstance();
		HttpSession session = request.getSession();

		try {
			List<CategoryBean> result = engine.getAllCategories();
			request.setAttribute("catalogList", result);
			request.setAttribute("cart", session.getAttribute("cart"));	
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getSession(true);
		this.getServletContext().getRequestDispatcher("/Dash.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
