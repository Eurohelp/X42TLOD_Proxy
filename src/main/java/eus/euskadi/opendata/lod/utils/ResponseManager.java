/**
 * 
 */
package eus.euskadi.opendata.lod.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author grozadilla
 *
 */
public class ResponseManager {

	private Log log = LogFactory.getLog(ResponseManager.class);
	private static ResponseManager INSTANCE = null;
	
	public synchronized static ResponseManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ResponseManager();
		}
		return INSTANCE;
	}
	
	public void redirectPostSparqlRequest(HttpServletRequest theReq, HttpServletResponse theResp, String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = null;
		HttpResponse response = null;

		String theServerHost = url;

		String theReqUrl = theServerHost;
		if (log.isDebugEnabled()) log.debug("> Sparql-proxy: "+ theReqUrl); 

		try {
			httppost = new HttpPost(theReqUrl);
			String body = getBody(theReq);
			HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
			httppost.setEntity(entity);
			
			//headers
			Enumeration<String> aHeadersEnum = theReq.getHeaderNames();
			while (aHeadersEnum.hasMoreElements()) {
				String aHeaderName = aHeadersEnum.nextElement();
				String aHeaderVal = theReq.getHeader(aHeaderName);
				if (!"content-length".equals(aHeaderName.toLowerCase())){
					httppost.setHeader(aHeaderName, aHeaderVal);
				}
			}
			if (log.isDebugEnabled()) log.debug("executing request " + httppost.getURI());

			// Create a response handler
			response = httpclient.execute(httppost);

			// set the same Headers
			for(Header aHeader : response.getAllHeaders()) {
				theResp.setHeader(aHeader.getName(), aHeader.getValue());
			}

			// set the same locale
			theResp.setLocale(response.getLocale());

			// set the content
			theResp.setContentLength((int) response.getEntity().getContentLength());
			theResp.setContentType(response.getEntity().getContentType().getValue());

			// set the same status
			theResp.setStatus(response.getStatusLine().getStatusCode());

			// redirect the output
			InputStream aInStream = null;
			OutputStream aOutStream = null;
			try {
				aInStream = response.getEntity().getContent();
				aOutStream = theResp.getOutputStream();

				int data = aInStream.read();
				while(data != -1) {
					aOutStream.write(data);

					data = aInStream.read();
				}
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
			finally {
				if (aInStream != null)
					aInStream.close();
				if (aOutStream != null) 
					aOutStream.close();
			}

		} finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().shutdown();
		}
	}
    
	public void redirectGetSparqlRequest(HttpServletRequest theReq, HttpServletResponse theResp, String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = null;
		HttpResponse response = null;

		String theServerHost = url;

		String theReqUrl = theServerHost;
		if (log.isDebugEnabled()) log.debug("> Sparql-proxy: "+ theReqUrl); 

		try {
			//parameters 
			Enumeration<String> aParamsEnum = theReq.getParameterNames();
			if (aParamsEnum.hasMoreElements()){
				theReqUrl = theReqUrl + "?";
			}
			while (aParamsEnum.hasMoreElements()) {
				String aParamName = aParamsEnum.nextElement();
				String aParamVal = theReq.getParameter(aParamName);
				if (aParamName.equals("query")){
					aParamVal = URLEncoder.encode(theReq.getParameter(aParamName), "UTF-8");
				}
				theReqUrl = theReqUrl +aParamName + "=" + aParamVal;
				if (aParamsEnum.hasMoreElements()){
					theReqUrl = theReqUrl + "&";
				}
			}
			
			
			
			httpget = new HttpGet(theReqUrl);
			
			//headers
			Enumeration<String> aHeadersEnum = theReq.getHeaderNames();
			while (aHeadersEnum.hasMoreElements()) {
				String aHeaderName = aHeadersEnum.nextElement();
				String aHeaderVal = theReq.getHeader(aHeaderName);

				httpget.setHeader(aHeaderName, aHeaderVal);
			}
			
			

			if (log.isDebugEnabled()) log.debug("executing request " + httpget.getURI());

			// Create a response handler
			response = httpclient.execute(httpget);

			// set the same Headers
			for(Header aHeader : response.getAllHeaders()) {
				theResp.setHeader(aHeader.getName(), aHeader.getValue());
			}

			// set the same locale
			theResp.setLocale(response.getLocale());

			// set the content
			theResp.setContentLength((int) response.getEntity().getContentLength());
			theResp.setContentType(response.getEntity().getContentType().getValue());

			// set the same status
			theResp.setStatus(response.getStatusLine().getStatusCode());

			// redirect the output
			InputStream aInStream = null;
			OutputStream aOutStream = null;
			try {
				aInStream = response.getEntity().getContent();
				aOutStream = theResp.getOutputStream();

				int data = aInStream.read();
				while(data != -1) {
					aOutStream.write(data);

					data = aInStream.read();
				}
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
			finally {
				if (aInStream != null)
					aInStream.close();
				if (aOutStream != null) 
					aOutStream.close();
			}

		} finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	private static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

	
}
