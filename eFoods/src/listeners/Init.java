package listeners;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.Engine;

/**
 * Application Lifecycle Listener implementation class Init
 *
 */
@WebListener
public class Init implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

	public void contextInitialized(ServletContextEvent sce) {
		Engine.getInstance();
		ServletContext context = sce.getServletContext();

		List<Integer> timeBetweenAdd = new LinkedList<>();
		List<Integer> timeBetweenCheckout = new LinkedList<>();

		context.setAttribute("timeBetweenAdd", timeBetweenAdd);
		context.setAttribute("timeBetweenCheckout", timeBetweenCheckout);
	}

}
