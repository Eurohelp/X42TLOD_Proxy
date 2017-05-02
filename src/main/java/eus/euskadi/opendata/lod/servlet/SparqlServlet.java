package eus.euskadi.opendata.lod.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SparqlServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	private Properties properties = null;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*if (req.getHeader("Accept").contains("text/html")){
			
		}else{
			
		}*/
		
		resp.setContentType("text/html");
		
		PrintWriter out = resp.getWriter();
		out.print(properties.getProperty("sparql.initial.message"));
		out.flush();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("proxy.properties");
		// ...
		properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
	}
}
