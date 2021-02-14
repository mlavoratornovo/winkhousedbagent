package winkhousedbagent.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhousedbagent.Activator;
import winkhousedbagent.helper.HSQLDBHelper;
import winkhousedbagent.view.MainView;

public class ADLogRegister extends Action {

	private ImageDescriptor noconnect = Activator.getImageDescriptor("icons/connect_no.png");
	private ImageDescriptor connect = Activator.getImageDescriptor("icons/connect_established.png");
	
	public final static String CONNECT_TOOLTIP = "Connettiti al log del database";
	public final static String NO_CONNECT_TOOLTIP = "Disconnetti dal log del database";
	
	public ADLogRegister(String text, int style) {
		super(text, style);
		setImageDescriptor(noconnect);
		setToolTipText(text);		
	}

	@Override
	public void run() {
		MainView mv = (MainView)PlatformUI.getWorkbench()
				  						  .getActiveWorkbenchWindow()
				  						  .getActivePage()
				  						  .findView(MainView.ID);
		
		if (isChecked()){
			
			setImageDescriptor(connect);
			setToolTipText(NO_CONNECT_TOOLTIP);
			HSQLDBHelper.getInstance().setDoLog(true);
			
		}else{
			
			setImageDescriptor(noconnect);
			setToolTipText(CONNECT_TOOLTIP);
			HSQLDBHelper.getInstance().setDoLog(false);			
			
		}
		
	}
	
	


}
