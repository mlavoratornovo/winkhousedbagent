package winkhousedbagent.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhousedbagent.helper.BackUpHelper;

public class RestoreBackUpAction extends Action {

	public static String ID = "freemobilia.restorebackupaction";
	
	public RestoreBackUpAction() {
		setId(ID);
	}

	public RestoreBackUpAction(String text) {
		super(text);
		setId(ID);
	}

	public RestoreBackUpAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public RestoreBackUpAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	
	@Override
	public void run() {
		BackUpHelper uDBHelper = new BackUpHelper();
		uDBHelper.restoreDBBackUp();
	}

	
}
