/**
 * 
 */
package eus.euskadi.opendata.lod.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author grozadilla
 *
 */
public class PropertiesManager {

	private Log log = LogFactory.getLog(PropertiesManager.class);
	private static PropertiesManager INSTANCE = null;
	private Properties prop = null;
	
	public synchronized static PropertiesManager getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new PropertiesManager();
			INSTANCE.loadProperties();
			
		}
		return INSTANCE;
	}
	
	public void loadProperties () throws IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("proxy.properties");
		prop = new Properties();
		prop.load(input);
		log.info("Properties file loaded"); 
	}
	
	public String getProperty(String property){
		return prop.getProperty(property);
	}
}
