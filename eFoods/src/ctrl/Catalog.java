package ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryBean;
import model.Engine;

/**
 * Servlet implementation class Catalog
 */
@WebServlet("/Catalog.do")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Regular instantiation:
		request.getSession(true);
		Engine engine = Engine.getInstance();
		
		// Getting categories to populate the users page with category options:
		try {
			List<CategoryBean> result = engine.getCategories();
			request.setAttribute("catalogList", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// If a category is clicked, return only items of that category:
		if (request.getParameter("catalogId") != null) {
			
		} else { // Otherwise, return all items.
			
		}
		
		this.getServletContext().getRequestDispatcher("/Catalog.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
