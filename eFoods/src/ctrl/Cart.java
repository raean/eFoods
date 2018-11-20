package ctrl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.ItemBean;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart.do")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Engine engine = Engine.getInstance();
		
		Map<String, Integer> cart = (Map<String, Integer>) request.getSession().getAttribute("cart");
		try {
			Map<ItemBean, Integer> viewableCart = engine.viewableCart(cart);
			request.setAttribute("viewableCart", viewableCart);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		if (request.getParameter("updateCartButton")!=null) {
			System.out.println(request.getParameterValues("deleteCheckbox")[0] +", " + request.getParameterValues("deleteCheckbox")[1] + ", " 
					+ request.getParameterValues("quantityInput")[0] +",  " + request.getParameterValues("quantityInput")[1]);
		}
		
		this.getServletContext().getRequestDispatcher("/Cart.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
