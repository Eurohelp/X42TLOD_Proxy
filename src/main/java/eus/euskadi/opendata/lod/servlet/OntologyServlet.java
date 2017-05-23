package eus.euskadi.opendata.lod.servlet;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import eus.euskadi.opendata.lod.utils.HttpManager;
import eus.euskadi.opendata.lod.utils.MIMEtype;
import eus.euskadi.opendata.lod.utils.PropertiesManager;


public class OntologyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	final static Logger logger = Logger.getLogger(OntologyServlet.class);
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resourceURI = req.getRequestURI().substring(req.getRequestURI().indexOf(req.getContextPath())+ req.getContextPath().length());
		String ontologyURI = resourceURI.replaceFirst("/def/", "");
		String lang = HttpManager.getInstance().getLangFromResquest(req);
		try {
			if (req.getHeader("Accept").contains(MIMEtype.HTML.mimetypevalue()) && !"euskadi.owl".equals(ontologyURI)){
				goToDefinitionHtml(resp, ontologyURI, lang);
			}else{
				goToDefinitionOwl(req, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	
	/**
	 * Redirects to Ontology definition page
	 * @param req the request
	 * @param resp the response
	 * @throws Exception exception
	 */
	private void goToDefinitionHtml(HttpServletResponse resp, String ontology, String lang) throws Exception {
		ontology = ontology.replaceAll("/", "_");
		String page = MessageFormat.format(PropertiesManager.getInstance().getProperty("lod.webroot"),lang) +  "ontology_def.html#"+ ontology;
		resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
		resp.addHeader("Location", page);
	}
	
	/**
	 * Redirects to Ontology definition OWL file
	 * @param req the request
	 * @param resp the response
	 * @throws Exception exception
	 */
	private void goToDefinitionOwl(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		getServletContext().getRequestDispatcher("/owl/euskadi.owl").forward
           (req, resp); 
	}
}
