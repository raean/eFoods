package ctrl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CustomerBean;

/**
 * Servlet implementation class Auth
 */
@WebServlet("/Auth.do")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String REDIRECT = "https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=http://localhost:4413/eFoods/Auth.do";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		if (request.getParameter("name") == null && request.getParameter("user") == null
				&& request.getParameter("hash") == null) {
			String referer = request.getHeader("referer");
			session.setAttribute("referer", referer);
			response.sendRedirect(REDIRECT);
		} else {
			CustomerBean customer = new CustomerBean();

			customer.setAccount(request.getParameter("user"));
			customer.setName(request.getParameter("name"));

			session.setAttribute("customer", customer);
			session.setAttribute("authenticated", true);

			String referer = (String) session.getAttribute("referer");
			response.sendRedirect(referer);

		}
		// this.getServletContext().getRequestDispatcher("/Dash.jspx").forward(request,
		// response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
