package winkhousedbagent.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhousedbagent.view.AboutDialog;


public class AboutDialogAction extends Action {
	
	public static String ID = "freemobilia.aboutaction";
	
	public AboutDialogAction() {
		setId(ID);
	}

	public AboutDialogAction(String text) {
		super(text);
		setId(ID);
	}

	public AboutDialogAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public AboutDialogAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	@Override
	public void run() {
		
		AboutDialog ad = new AboutDialog();
	}


}
