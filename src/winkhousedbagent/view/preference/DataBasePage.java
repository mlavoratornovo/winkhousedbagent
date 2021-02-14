package winkhousedbagent.view.preference;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhousedbagent.Activator;
import winkhousedbagent.helper.HSQLDBHelper;
import winkhousedbagent.util.WinkhouseDBAgentUtils;


public class DataBasePage extends PreferencePage {
		
	private Text nomeDatabaseText = null;
	private Text cartellaDatiPathText = null;
	private Text passwordDatabaseText = null;
	
	private Form form = null;
	public static String DEFAULT_DB_FOLDER = "\\winkhouse";
	
	public DataBasePage() {				
		setPreferenceDefaults();
	}

	public DataBasePage(String title) {
		super(title);	
		setPreferenceDefaults();
	}

	public DataBasePage(String title, ImageDescriptor image) {
		super(title, image);				
		setPreferenceDefaults();
	}
	

	private void setPreferenceDefaults(){
		WinkhouseDBAgentUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseDBAgentUtils.CARTELLADATI, 
									Activator.getDefault().getStateLocation().toFile().toString()+DEFAULT_DB_FOLDER);
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WinkhouseDBAgentUtils.getInstance()
						.getPreferenceStore()
						.setDefault(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI, (addr != null)?addr.getHostAddress():"");
		/*WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setDefault(WinkhouseDBAgentUtils.PASSWORD, "");
		WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setDefault(WinkhouseDBAgentUtils.IMAGEPATH, "");
		WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setDefault(WinkhouseDBAgentUtils.ALLEGATIPATH, "");
		WinkhouseDBAgentUtils.getInstance().getPreferenceStore().setDefault(WinkhouseDBAgentUtils.REPORTTEMPLATEPATH, "");*/
	}
	
	@Override
	protected Control createContents(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		
		form.getBody().setLayout(gridLayout);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);		
//		form.getBody().setLayoutData(gridData);
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Label usernameDatabaseLabel = toolkit.createLabel(form.getBody(), "Nome/ip server dati ");
		usernameDatabaseLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		nomeDatabaseText = toolkit.createText(form.getBody(), 
				                                  (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI).equalsIgnoreCase(""))
				                                   ? (addr != null)?addr.getHostAddress():""
				                                   : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI),
				                                  SWT.FLAT);
		nomeDatabaseText.setLayoutData(gridData);
		

		Label imagePathLabel = toolkit.createLabel(form.getBody(), "Cartella dati  ");
		imagePathLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite cimm = toolkit.createComposite(form.getBody());
		cimm.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		GridLayout gridLayoutImm = new GridLayout(2, false);
		gridLayoutImm.marginWidth = 0;
		gridLayoutImm.marginHeight = 0;
		gridLayoutImm.verticalSpacing = 0;
		gridLayoutImm.horizontalSpacing = 0;		
		
		GridData gridDataImm = new GridData(SWT.FILL, SWT.FILL, true, false);
		cimm.setLayout(gridLayoutImm);		
		cimm.setLayoutData(gridDataImm);
		cartellaDatiPathText = toolkit.createText(cimm, 
				                           (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI).equalsIgnoreCase(""))
				                           ? Activator.getDefault().getStateLocation().toFile().toString()+DEFAULT_DB_FOLDER
				                           : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI),
				                           SWT.FLAT);
		cartellaDatiPathText.setLayoutData(gridData);
		
		ImageHyperlink ihConferma = toolkit.createImageHyperlink(cimm, SWT.WRAP);		
		ihConferma.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		ihConferma.setImage(Activator.getImageDescriptor("icons/folder.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("icons/folder_over.png").createImage());
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd = new DirectoryDialog(form.getShell());
				dd.setFilterPath(cartellaDatiPathText.getText());

		        dd.setText("Percorso DATI");

		        dd.setMessage("Seleziona la cartella dove sono i file del database");

		        String dir = dd.open();
		        if (dir != null) {
		        	cartellaDatiPathText.setText(dir);
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		gridDataImm.minimumHeight = 16;				
		gridDataImm.heightHint = 16;
		
		Label passwordDatabase = toolkit.createLabel(form.getBody(), "Password accesso base dati");
		passwordDatabase.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		String cryptPws = WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.USERDBPWD);
		if (!cryptPws.equalsIgnoreCase("")){
			cryptPws = WinkhouseDBAgentUtils.getInstance().DecryptString(cryptPws);
		}
		
		passwordDatabaseText = toolkit.createText(form.getBody(), "", SWT.PASSWORD);
		passwordDatabaseText.setText(cryptPws);
		
		toolkit.paintBordersFor(cimm);
		toolkit.paintBordersFor(form.getBody());
		
		return form;
	}

	@Override
	protected void performApply() {
		
        String pathdatiCurrent = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI).equalsIgnoreCase(""))
        				   		  ? Activator.getDefault().getStateLocation().toFile().toString()+DEFAULT_DB_FOLDER
        				   		  : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI);

        String pathdatiNew = cartellaDatiPathText.getText();
        
		if (!pathdatiCurrent.equalsIgnoreCase(pathdatiNew)){
			
			boolean response = MessageDialog.openConfirm(getShell(), 
														 "spostamento dati", 
														 "Stai per spostare i file contenenti i dati, \n" +
														 "prima di procedere chiudere tutti i winkhouse aperti, \n" +
														 "Il servizio dati verrà disattivato per essere riattivato a spostamento avvenuto.\n" +
														 "Continuare ?");
			
			if (response){
				HSQLDBHelper.getInstance().chiudiDatabase();
				WinkhouseDBAgentUtils.getInstance()
									 .getPreferenceStore()
									 .setValue(WinkhouseDBAgentUtils.CARTELLADATI,cartellaDatiPathText.getText().trim());
				
				//copia i file
				WinkhouseDBAgentUtils.getInstance().copiaFile(pathdatiCurrent+File.separator+"winkhouse.script", 
															  pathdatiNew+File.separator+"winkhouse.script");
				WinkhouseDBAgentUtils.getInstance().copiaFile(pathdatiCurrent+File.separator+"winkhouse.log", 
						  									  pathdatiNew+File.separator+"winkhouse.log");
				WinkhouseDBAgentUtils.getInstance().copiaFile(pathdatiCurrent+File.separator+"winkhouse.properties", 
						  									  pathdatiNew+File.separator+"winkhouse.properties");

				HSQLDBHelper.getInstance().startHSQLDB();
				
				
			}
			

			
		}
		
		WinkhouseDBAgentUtils.getInstance()
							 .getPreferenceStore()
							 .setValue(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI,nomeDatabaseText.getText().trim());
		
		String encPws = WinkhouseDBAgentUtils.getInstance().EncryptString(passwordDatabaseText.getText());
		
		HSQLDBHelper.getInstance().updateUserPws(passwordDatabaseText.getText());
		
		WinkhouseDBAgentUtils.getInstance()
		 					 .getPreferenceStore()
		 					 .setValue(WinkhouseDBAgentUtils.USERDBPWD,encPws);
		
		WinkhouseDBAgentUtils.getInstance().savePreference();
	}

	@Override
	public boolean performCancel() {		
		System.out.println(Activator.getDefault().getStateLocation().toFile().toString());
		
		if (cartellaDatiPathText != null)
			cartellaDatiPathText.setText((WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI).equalsIgnoreCase(""))
        						       ? WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseDBAgentUtils.CARTELLADATI)
        						       : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLADATI)
        						     );
		
		if (nomeDatabaseText != null)
			nomeDatabaseText.setText((WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI).equalsIgnoreCase(""))
			      					  ? WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI)
			                          : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI)
			                        );
		
		return true;
	}

	@Override
	protected void performDefaults() {
		nomeDatabaseText.setText(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseDBAgentUtils.NOME_IP_MACCHINA_DATI));
		cartellaDatiPathText.setText(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseDBAgentUtils.CARTELLADATI));
		passwordDatabaseText.setText(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseDBAgentUtils.USERDBPWD));
	}

	@Override
	public boolean performOk() {
		try{
			performApply();
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
