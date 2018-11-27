package listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class Authentication implements HttpSessionListener, HttpSessionAttributeListener {

	// When the session is created, the user is not authenticated.
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("authenticated", false);
	}

}
