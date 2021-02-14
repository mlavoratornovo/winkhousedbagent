package winkhousedbagent.configuration;

import java.io.InputStream;

import javolution.util.FastMap;
import javolution.xml.XMLObjectReader;

public class EnvSettingsFactory {
	
	private static EnvSettingsFactory instance = null;
	private FastMap<String,String> queries = null;
	
	private EnvSettingsFactory(){
		
	}
	
	public static EnvSettingsFactory getInstance(){
		if (instance == null){
			instance = new EnvSettingsFactory();
		}
		return instance;
	}
	
	public FastMap<String, String> getQueries(){
		
		try {
					
			if (queries == null){
				
				InputStream is = getClass().getClassLoader()
										   .getResourceAsStream("winkhousedbagent/configuration/DBQueries.xml");
				
				XMLObjectReader or = XMLObjectReader.newInstance(is);
				queries = (FastMap<String,String>)or.read();
				or.close();
				
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return queries;
	}
	}
