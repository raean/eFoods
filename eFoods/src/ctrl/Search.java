package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.ItemBean;

@WebServlet("/Search.do")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("searchButton") != null) {
			Engine engine = Engine.getInstance();
			String searchInputValue = request.getParameter("searchInput");
			if (!searchInputValue.isEmpty()) {
				try {
					List<ItemBean> result = engine.doSearch(searchInputValue);
					request.setAttribute("result",  result);
				} catch (Exception e) {
					// I don't know what we're meant to do here.
					System.out.println(e.getMessage() + "poop");
				}
			} else {
				
			}
		}
		this.getServletContext().getRequestDispatcher("/Search.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
