package listeners;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import model.Engine;

/**
 * Initializes attributes upon context and session initialization.
 *
 */
@WebListener
public class Init implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

	// Initializes the engine upon server creation, and adds the lists for
	// analytics.
	public void contextInitialized(ServletContextEvent sce) {
		Engine.getInstance();
		ServletContext context = sce.getServletContext();

		List<Integer> timeBetweenAdd = new LinkedList<>();
		List<Integer> timeBetweenCheckout = new LinkedList<>();

		context.setAttribute("timeBetweenAdd", timeBetweenAdd);
		context.setAttribute("timeBetweenCheckout", timeBetweenCheckout);
	}

	// Creates the cart in the session and sets authenticated to false.
	public void sessionCreated(HttpSessionEvent se) {
		Map<String, Integer> cart = new HashMap<>();
		
		se.getSession().setAttribute("cart", cart);
		se.getSession().setAttribute("authenticated", false);
	}

}
