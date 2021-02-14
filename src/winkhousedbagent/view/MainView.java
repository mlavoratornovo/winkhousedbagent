package winkhousedbagent.view;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import winkhousedbagent.Activator;
import winkhousedbagent.action.ADLogRegister;
import winkhousedbagent.action.CleanLog;
import winkhousedbagent.action.SaveLog;
import winkhousedbagent.db.ConnectionManager;
import winkhousedbagent.helper.HSQLDBHelper;
import winkhousedbagent.helper.UpdateDBHelper;


public class MainView extends ViewPart {

	public final static String ID = "winkhousedbagenti.MainView";
	
	private FormToolkit ft = null;
	private ScrolledForm f = null;
	private Text console = null;
	
	
	public MainView() {}
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setExpandVertical(true);
		f.setImage(Activator.getImageDescriptor("/icons/Database16.png").createImage());
		f.setText("Console database");		
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		mgr.add(new ADLogRegister(ADLogRegister.CONNECT_TOOLTIP,Action.AS_CHECK_BOX));
		mgr.add(new CleanLog("Pulisci il log", Activator.getImageDescriptor("/icons/history_clear.png")));
		mgr.add(new SaveLog("Salva il log", Activator.getImageDescriptor("/icons/document-save.png")));
		f.updateToolBar();
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);
		section.setExpanded(true);
		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}

		});
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gd);
		section.setText("Log Database");
		section.setDescription("mostra le istruzioni SQL eseguite nel database");						
		
		console = ft.createText(section,"",SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		console.setLayoutData(gd);
		console.setBackground(new Color(null,0,0,0));
		console.setForeground(new Color(null,76,217,27));
		console.setEditable(false);
		console.setCursor(PlatformUI.getWorkbench().getDisplay().getSystemCursor(SWT.CURSOR_HAND));

		section.setClient(console);
		
		HSQLDBHelper.getInstance().setConsole(console);
		if (HSQLDBHelper.getInstance().startHSQLDB() == true){
			try{
				ConnectionManager.getInstance().setConsole(console);
				Connection con = ConnectionManager.getInstance().getConnection();
				
				UpdateDBHelper uDBH = new UpdateDBHelper();
				uDBH.setConsole(console);
//				if (uDBH.goToVersion_1_1(con) == true){
//					try {
//						con.commit();
//					} catch (SQLException e1) {
//						e1.printStackTrace();
//					}
//				}else{
//					try {
//						con.rollback();
//					} catch (SQLException e1) {
//						e1.printStackTrace();
//					}
//				}
			}catch(Exception e){
				console.append("IMPOSSIBILE ESEGUIRE CONNESSIONE ALLA BASE DATI\n");
				console.append("Controllare la password impostata\n");
				
				for (int i = 0; i < e.getStackTrace().length; i++) {
					console.append(((StackTraceElement)e.getStackTrace()[i]).toString() + "\n");
				}				
				
			}
		}else{
			console.append("IMPOSSIBILE AVVIARE IL SERVER DATI");			
		}
		
	}
	
	@Override
	public void dispose() {
    	File f = new File(Activator.getDefault()
				   .getStateLocation()
				   .toFile()
				   .toString() + File.separator + "lock.lock");

	 	if (f.exists()){
	 		f.delete();
	 	}
		
	 	HSQLDBHelper.getInstance().chiudiDatabase();
		super.dispose();
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public Text getConsole() {
		return console;
	}

}
