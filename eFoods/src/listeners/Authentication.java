package listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public class Authentication implements HttpSessionListener, HttpSessionAttributeListener {

	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("authenticated", false);
		System.out.println("AUTHETNICATED IS " + se.getSession().getAttribute("authenticated"));
	}

	public void sessionDestroyed(HttpSessionEvent se) {
	}

	public void attributeAdded(HttpSessionBindingEvent se) {

	}

	public void attributeRemoved(HttpSessionBindingEvent se) {

	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
		System.out.println("AUTHETNICATED");

	}

}
