package listeners;

import java.util.Enumeration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class Authentication implements HttpSessionListener, HttpSessionAttributeListener {

	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("authenticated", false);
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
