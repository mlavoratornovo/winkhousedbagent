package winkhousedbagent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import winkhousedbagent.Activator;
import winkhousedbagent.util.WinkhouseDBAgentUtils;



public class ConnectionManager {

	private static ConnectionManager instance = null;
	private Connection connection = null;
	private boolean errorMessage = true; 
	private Text console = null;
	
	private ConnectionManager(){
		
	}
	
	public static ConnectionManager getInstance(){
		if (instance == null){
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	public Connection getConnection(){
					
			String driver = "org.hsqldb.jdbcDriver";
			String posizioneDB = (WinkhouseDBAgentUtils.getInstance()
													   .getPreferenceStore()
													   .getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI)
													   .equalsIgnoreCase(""))
								  ? WinkhouseDBAgentUtils.getInstance()
										  				 .getPreferenceStore()
										  				 .getDefaultString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI)
							      : WinkhouseDBAgentUtils.getInstance()
							      						 .getPreferenceStore()
							      						 .getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI);

			String userpws = (WinkhouseDBAgentUtils.getInstance()
												   .getPreferenceStore()
												   .getString(WinkhouseDBAgentUtils.USERDBPWD)
												   .equalsIgnoreCase(""))
							  ? WinkhouseDBAgentUtils.getInstance()
													 .getPreferenceStore()
													 .getDefaultString(WinkhouseDBAgentUtils.USERDBPWD)
							  : WinkhouseDBAgentUtils.getInstance()
										      		 .getPreferenceStore()
										      		 .getString(WinkhouseDBAgentUtils.USERDBPWD);
			if (!userpws.equalsIgnoreCase("")){										 
				userpws = WinkhouseDBAgentUtils.getInstance().DecryptString(userpws);
			}
													 
										  				 
			String url = "jdbc:hsqldb:hsql://"+posizioneDB+"/winkhouse";
			
			String username = "sa";			

//			if (console != null){
//				console.append("STRINGA CONNESSIONE :" + url + "\n");
//				console.append("USERNAME :" + username + "\n");
//				console.append("PASSWORD :" + userpws + "\n");
//			}
					  				   			
		    try {
				Class.forName(driver);
				connection= DriverManager.getConnection(url, username, userpws);
				connection.setAutoCommit(false);	
//				connectionSelectConnection.setReadOnly(true);
			} catch (ClassNotFoundException e) {
				if (errorMessage){
					MessageBox mb = new MessageBox(Activator.getDefault()
															.getWorkbench().getActiveWorkbenchWindow().getShell(),								
												    SWT.ERROR);
					errorMessage = false;
					mb.setMessage("Driver database non trovati");
					mb.open();			
				}
			} catch (SQLException e) {
				if (errorMessage){
					if (console != null){
						console.append(e.getStackTrace().toString() + "\n");
					}
					errorMessage = false;
				}								
			}		  
		
		return connection;
	}

	public Text getConsole() {
		return console;
	}

	public void setConsole(Text console) {
		this.console = console;
	}
	
	
}
