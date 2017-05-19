package eus.euskadi.opendata.lod.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import eus.euskadi.opendata.lod.utils.HttpManager;
import eus.euskadi.opendata.lod.utils.MIMEtype;
import eus.euskadi.opendata.lod.utils.PropertiesManager;

/**
 * This servlet resolves sparql calls
 * @author grozadilla
 *
 */
public class SparqlServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	final static Logger logger = Logger.getLogger(SparqlServlet.class);
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains(MIMEtype.HTML.mimetypevalue())){
				if(logger.isDebugEnabled()){
				    logger.debug("Load Yasgui component");
				}
				goToEndpoint(req, resp); 
			}else{
				HttpManager.getInstance().redirectGetRequest(req, resp, PropertiesManager.getInstance().getProperty("lod.triplestore.url"), null);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains(MIMEtype.HTML.mimetypevalue())){	
				goToEndpoint(req, resp); 
			}else{
				HttpManager.getInstance().redirectPostRequest(req,resp, PropertiesManager.getInstance().getProperty("lod.triplestore.url"));
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}	
	}
	
	/**
	 * Redirects to Sparql endpoint page
	 * @param req the request
	 * @param resp the response
	 * @throws Exception exception
	 */
	private void goToEndpoint(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		req.setAttribute("url", PropertiesManager.getInstance().getProperty("lod.sparql.endpoint")); 
		getServletContext().getRequestDispatcher("/pages/endpoint.jsp").forward
           (req, resp); 
	}
	
}