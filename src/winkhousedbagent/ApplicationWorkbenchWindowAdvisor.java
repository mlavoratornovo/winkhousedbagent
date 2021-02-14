package winkhousedbagent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	TrayItem trayItem = null;
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
    	File f = new File(Activator.getDefault()
						  		   .getStateLocation()
						  		   .toFile()
						  		   .toString() + File.separator + "lock.lock");
    	if (f.exists()){
    		PlatformUI.getWorkbench().close();
    	}else{
    		try {
				FileWriter fw = new FileWriter(f);
				fw.write("");
			} catch (IOException e) {
				e.printStackTrace();
			}
	        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
	        configurer.setInitialSize(new Point(400, 300));
	        configurer.setShowCoolBar(false);
	        configurer.setShowStatusLine(false);
	        configurer.setTitle("winkhouseDBAgent");
    	}
    }

	@Override
	public boolean preWindowShellClose() {
		trayItem = new TrayItem(Display.getDefault().getSystemTray(), SWT.NONE);
		trayItem.setText("winkhouseDBAgent");
		trayItem.setImage(Activator.getImageDescriptor("icons/Database16.png").createImage());
		trayItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				Shell workbenchWindowShell = getWindowConfigurer().getWindow().getShell();
				workbenchWindowShell.setVisible(true);
				workbenchWindowShell.setActive();
				workbenchWindowShell.setFocus();
				workbenchWindowShell.setMinimized(false);
				trayItem.dispose();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		getWindowConfigurer().getWindow().getShell().setVisible(false);
		return false;	
	}
}
