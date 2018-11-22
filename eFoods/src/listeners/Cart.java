package listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import model.ItemBean;

/**
 * Application Lifecycle Listener implementation class Cart
 *
 */
@WebListener
public class Cart implements HttpSessionListener, HttpSessionAttributeListener {

	public void sessionCreated(HttpSessionEvent se) {
		Map<String, Integer> cart = new HashMap<>();
		se.getSession().setAttribute("cart", cart);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
	}

	public void attributeAdded(HttpSessionBindingEvent se) {
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

}
