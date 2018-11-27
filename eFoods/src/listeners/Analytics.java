package listeners;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Watches events in the application to calculate analytics for the admin.
 *
 */
@WebListener
public class Analytics implements HttpSessionListener, HttpSessionAttributeListener, ServletRequestListener {

	// When the session is created the time of the session is recorded in the
	// session.
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		Long currentTime = System.currentTimeMillis();

		session.setAttribute("sessionStart", currentTime);
		session.setAttribute("userAddedToCart", false);
		session.setAttribute("userCheckedOut", false);
	}

	// When the user first adds an item to a cart, or checks out an order, the time
	// inbetween the session start and the event is recorded in the respective lists
	// which are stored in the context.
	@SuppressWarnings("unchecked")
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest httpReq = (HttpServletRequest) sre.getServletRequest();
		String pageVisted = httpReq.getRequestURL().toString();
		HttpSession session = httpReq.getSession();

		if (pageVisted.matches(".*Catalog.do")) {

			boolean userAddedToCart = (boolean) session.getAttribute("userAddedToCart");
			if (httpReq.getParameter("cartButton") != null && userAddedToCart == false) {
				long timeTaken = getTimeTaken(session);

				List<Integer> timeBetweenAdd = (List<Integer>) httpReq.getServletContext()
						.getAttribute("timeBetweenAdd");
				timeBetweenAdd.add((int) timeTaken);
				session.setAttribute("userAddedToCart", true);
			}

		} else if (pageVisted.matches(".*Order.do")) {

			boolean userCheckedOut = (boolean) session.getAttribute("userCheckedOut");
			if (httpReq.getParameter("confirmOrderButton") != null && userCheckedOut == false) {
				long timeTaken = getTimeTaken(session);

				List<Integer> timeBetweenCheckout = (List<Integer>) httpReq.getServletContext()
						.getAttribute("timeBetweenCheckout");
				timeBetweenCheckout.add((int) timeTaken);
				session.setAttribute("userCheckedOut", true);
			}
		}
	}

	// Calculates the seconds between the session start and the event.
	private long getTimeTaken(HttpSession session) {
		long currentTime = System.currentTimeMillis();
		long sessionStart = (long) session.getAttribute("sessionStart");

		long timeTaken = (currentTime - sessionStart) / 1000;
		return timeTaken;
	}

}
