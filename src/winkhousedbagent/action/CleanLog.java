	package winkhousedbagent.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhousedbagent.view.MainView;

public class CleanLog extends Action {

	public CleanLog(String text, ImageDescriptor image) {
		super(text, image);
		setToolTipText("Pulisci il contenuto mostrato nel log");
	}

	@Override
	public void run() {
		
		MainView mv = (MainView)PlatformUI.getWorkbench()
				  						  .getActiveWorkbenchWindow()
				  						  .getActivePage()
				  						  .findView(MainView.ID);
		
		mv.getConsole().setText("");

	}


}
