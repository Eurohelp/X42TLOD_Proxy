package eus.euskadi.opendata.lod.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import eus.euskadi.opendata.lod.utils.HttpManager;
import eus.euskadi.opendata.lod.utils.PropertiesManager;


public class DataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	final static Logger logger = Logger.getLogger(DataServlet.class);
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		String url = PropertiesManager.getInstance().getProperty("lod.triplestore.url");
		String resourceURI = req.getRequestURI().substring(req.getRequestURI().indexOf(req.getContextPath())+ req.getContextPath().length());
		
		String lang = "es";
		String basePath = MessageFormat.format(PropertiesManager.getInstance().getProperty("lod.data.url.base"),lang);
		String completeURI = basePath + resourceURI.replaceFirst("/data/", "/id/");
		//add query as parameter to URL
		String query = MessageFormat.format(PropertiesManager.getInstance().getProperty("lod.sparql.query.describe"), completeURI);
		url = url + "?query="+URLEncoder.encode(query, "UTF-8");
		
		
		try {
			HttpManager.getInstance().redirectGetRequest(req, resp, url);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	
	
}
