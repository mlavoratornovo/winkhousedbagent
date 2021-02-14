package winkhousedbagent.view.preference;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import winkhousedbagent.helper.BackUpHelper;
import winkhousedbagent.helper.UpdateDBHelper;
import winkhousedbagent.util.WinkhouseDBAgentUtils;

public class BackUpPage extends PreferencePage {

	public Form form = null;
	public Text cartellaDatiPathText = null;
	public Button abilitaBackup = null;
	public Combo comboPeriodo = null;
	
	public static String DEFAULT_BACKUP_FOLDER = "\\winkhouse_backup";
	
	public BackUpPage() {
	}

	public BackUpPage(String title) {
		super(title);
	}

	public BackUpPage(String title, ImageDescriptor image) {
		super(title, image);
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
		
		Label imagePathLabel = toolkit.createLabel(form.getBody(), "Cartella backup dati");
		imagePathLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Composite cimm = toolkit.createComposite(form.getBody());
		GridLayout gridLayoutImm = new GridLayout(2, false);
		gridLayoutImm.marginWidth = 0;
		gridLayoutImm.marginHeight = 0;
		gridLayoutImm.verticalSpacing = 0;
		gridLayoutImm.horizontalSpacing = 0;		
		
		GridData gridDataImm = new GridData(SWT.FILL, SWT.FILL, true, false);
		cimm.setLayout(gridLayoutImm);		
		cimm.setLayoutData(gridDataImm);
		cartellaDatiPathText = toolkit.createText(cimm, 
				                           		  (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLABACKUPDATI).equalsIgnoreCase(""))
				                           		   ? Activator.getDefault().getStateLocation().toFile().toString()+DEFAULT_BACKUP_FOLDER
				                           		   : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.CARTELLABACKUPDATI),
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

		        dd.setMessage("Seleziona la cartella dove verranno salvati di backup database");

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
		
//		Label abilitaBackUpLabel = toolkit.createLabel(form.getBody(), "Abilita Backup");
//		abilitaBackUpLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
//		
//		abilitaBackup = toolkit.createButton(form.getBody(),"",SWT.CHECK);
//		
//		abilitaBackup.setSelection((WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI).equalsIgnoreCase(""))
//                					? false
//                					: Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI)));
//		
//		
//		Label periodoLabel = toolkit.createLabel(form.getBody(), "Periodo Backup");
//		periodoLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
//		
//		comboPeriodo = new Combo(form.getBody(), SWT.SINGLE);
//		comboPeriodo.setItems(new String[]{"1 giorno","1 settimana","1 mese"});
//		
//		String periodo = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.PERIODOBACKUPDATI).equalsIgnoreCase(""))
//						  ? ""
//						  : WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.PERIODOBACKUPDATI);
//		
//		if (periodo.equalsIgnoreCase("1 giorno")){
//			comboPeriodo.select(0);
//		}
//		if (periodo.equalsIgnoreCase("1 settimana")){
//			comboPeriodo.select(1);
//		}
//		if (periodo.equalsIgnoreCase("1 mese")){
//			comboPeriodo.select(2);
//		}
		
		gridDataImm.minimumHeight = 16;				
		gridDataImm.heightHint = 16;			
		
		toolkit.paintBordersFor(cimm);
		toolkit.paintBordersFor(form.getBody());

		return form;
		
	}

	@Override
	protected void performApply() {
		
//		boolean abilitaBackUp_old = (WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI).equalsIgnoreCase(""))
//				 				 	 ? false
//				 				 	 : Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getPreferenceStore().getString(WinkhouseDBAgentUtils.ABILITABACKUPDATI));
//				
//		WinkhouseDBAgentUtils.getInstance()
//							 .getPreferenceStore()
//							 .setValue(WinkhouseDBAgentUtils.ABILITABACKUPDATI,abilitaBackup.getSelection());

		WinkhouseDBAgentUtils.getInstance()
		 					 .getPreferenceStore()
		 					 .setValue(WinkhouseDBAgentUtils.CARTELLABACKUPDATI,cartellaDatiPathText.getText());

//		WinkhouseDBAgentUtils.getInstance()
//		 					 .getPreferenceStore()
//		 					 .setValue(WinkhouseDBAgentUtils.PERIODOBACKUPDATI,comboPeriodo.getItems()[comboPeriodo.getSelectionIndex()]);

		WinkhouseDBAgentUtils.getInstance().savePreference();
		
//		if (abilitaBackUp_old == false && abilitaBackup.getSelection() == true){
//			
//			BackUpHelper bUpHelper = new BackUpHelper();
//			
//			bUpHelper.startScheduledJob(0, 0);
//			
//		}
//
//		if (abilitaBackUp_old == true && abilitaBackup.getSelection() == false){
//			
//			if (WinkhouseDBAgentUtils.getInstance().getBackupThread() != null){
//				WinkhouseDBAgentUtils.getInstance().getBackupThread().cancel(false);
//			}
//			
//		}

	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
	

}
