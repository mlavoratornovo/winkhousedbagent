package winkhousedbagent.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;

import winkhousedbagent.Activator;
import winkhousedbagent.view.preference.BackUpPage;
import winkhousedbagent.view.preference.DataBasePage;
import winkhousedbagent.view.preference.SecurityPage;


public class PreferenceAction extends Action {

	private static IWorkbenchWindow window = null;
	private final static String ID = "preferenceaction";
	
	public PreferenceAction(IWorkbenchWindow window, String text, ImageDescriptor image) {
		super(text, image);
		this.window = window;
		setId(ID);
		setText(text);
		setImageDescriptor(image);
	}

	@Override
	public void run() {
		PreferenceManager pm = new PreferenceManager();
		DataBasePage dbp = new DataBasePage();
		dbp.setTitle("Database");
//		BackUpPage bp = new BackUpPage("Backup");
		SecurityPage sp = new SecurityPage("Sicurezza"); 
		
		IPreferenceNode nodedb = new PreferenceNode("0",dbp);
//		IPreferenceNode nodebp = new PreferenceNode("1",bp);
		IPreferenceNode nodesp = new PreferenceNode("2",sp);

		pm.addToRoot(nodedb);
//		pm.addToRoot(nodebp);
		pm.addToRoot(nodesp);
		
		PreferenceDialog dialog = new PreferenceDialog(window.getShell(), pm);
		dialog.open();

	}

}
