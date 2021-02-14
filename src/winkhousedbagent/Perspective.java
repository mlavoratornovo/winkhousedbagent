package winkhousedbagent;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhousedbagent.view.MainView;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);				
		layout.addStandaloneView(MainView.ID, false, IPageLayout.LEFT, 0.25f, layout.getEditorArea());
	}
}
