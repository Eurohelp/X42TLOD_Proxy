package eus.euskadi.opendata.lod.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eus.euskadi.opendata.lod.utils.PropertiesManager;
import eus.euskadi.opendata.lod.utils.ResponseManager;


public class SparqlServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	private Log log = LogFactory.getLog(SparqlServlet.class);
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains("text/html")){
				goToEndpoint(req, resp); 
			}else{
				ResponseManager.getInstance().redirectGetSparqlRequest(req, resp, PropertiesManager.getInstance().getProperty("sparql.endpoint"));
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains("text/html")){	
				goToEndpoint(req, resp); 
			}else{
				ResponseManager.getInstance().redirectPostSparqlRequest(req,resp, PropertiesManager.getInstance().getProperty("sparql.endpoint"));
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
		req.setAttribute("url", PropertiesManager.getInstance().getProperty("sparql.endpoint")); 
		getServletContext().getRequestDispatcher("/endpoint.jsp").forward
           (req, resp); 
	}
	
}
