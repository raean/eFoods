package listeners;

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
	
	public static final String PO_FOLDER = "/WEB-INF/PO/";

    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         String poPath = sce.getServletContext().getRealPath(PO_FOLDER);
         Engine firstEngine = Engine.getInstance();
         firstEngine.initPoFolder(poPath);
    }
	
}
