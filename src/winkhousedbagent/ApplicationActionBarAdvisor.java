package winkhousedbagent;


import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

import winkhousedbagent.action.AboutDialogAction;
import winkhousedbagent.action.BackUpAction;
import winkhousedbagent.action.PreferenceAction;
import winkhousedbagent.action.RestoreBackUpAction;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private PreferenceAction preferenceAction;
    private IWorkbenchAction exitAction;
    private IContributionItem perspectivesMenu;
    private AboutDialogAction about = null;
    private BackUpAction backup = null;
    private RestoreBackUpAction restorebackup = null;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        
        register(exitAction);
                
        preferenceAction = new PreferenceAction(window,"Impostazioni",Activator.getImageDescriptor("icons/configure.png"));
        register(preferenceAction);
        backup = new BackUpAction("Avvia backup", Activator.getImageDescriptor("icons/DatabaseBCK16.png"));
        register(backup);
        restorebackup = new RestoreBackUpAction("Ripristina backup", Activator.getImageDescriptor("icons/DatabaseRBCK16.png"));
        register(restorebackup);
        
        about = new AboutDialogAction("About");
        register(about);
        
        
   //     perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);

    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&About", IWorkbenchActionConstants.M_HELP);

        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);        
        
        // File
//        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
//        fileMenu.add(messagePopupAction);
        fileMenu.add(preferenceAction);
        fileMenu.add(backup);
        fileMenu.add(restorebackup);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        helpMenu.add(about);
        
     //   MenuManager layoutMenu = new MenuManager("Prospettive", "layout");
	//	layoutMenu.add(perspectivesMenu);
	//	menuBar.add(layoutMenu);
        
        
        // Help
        
        removeActions();
    }
    
    private void removeActions(){
    	ActionSetRegistry asr = WorkbenchPlugin.getDefault().getActionSetRegistry();
    	IActionSetDescriptor[] actions = asr.getActionSets();
    	for (int i = 0; i < actions.length; i++) {
			if (
				(actions[i].getId().equalsIgnoreCase("org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo")) ||
				(actions[i].getId().equalsIgnoreCase("org.eclipse.ui.actionSet.openFiles"))
			   ){
				
				IExtension ext = actions[i].getConfigurationElement()				
				            				  .getDeclaringExtension();
				asr.removeExtension(ext, new Object[] { actions[i]});
			}
		}
    }

    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        
        toolbar.add(exitAction);
        
        //toolbar.add(wizardCancellazioniAction);
        
    }

    
}
