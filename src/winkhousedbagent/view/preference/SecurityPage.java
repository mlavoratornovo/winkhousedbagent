package winkhousedbagent.view.preference;

import java.util.ArrayList;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhousedbagent.dao.WinkSysDAO;
import winkhousedbagent.helper.GDataHelper;
import winkhousedbagent.util.IWinkSysProperties;
import winkhousedbagent.util.WinkhouseDBAgentUtils;
import winkhousedbagent.vo.GDataVO;
import winkhousedbagent.vo.WinkSysVO;

public class SecurityPage extends PreferencePage {

	private Form form = null;
	private Text criptKeyText = null;
	private String dummystr = null; 
	private Button login = null;
	private Button isDefaultRuleRestrict = null; 
	private Button isDefaultRuleRestrictUI = null;
	
	public SecurityPage() {

	}

	public SecurityPage(String title) {
		super(title);
	}

	public SecurityPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 8;
		gridLayout.horizontalSpacing = 0;

		form.getBody().setLayout(gridLayout);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.minimumHeight = 25;
		
		toolkit.paintBordersFor(form.getBody());
		Label posizioneDatabaseLabel = toolkit.createLabel(form.getBody(), "Chiave di criptazione passwords ");
		posizioneDatabaseLabel.setBackground(posizioneDatabaseLabel.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		String cryptKey = null;
				
		cryptKey = (WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.CRIPTKEY)==null)
					? "winkhouse"
					: WinkhouseDBAgentUtils.getInstance().DecryptString(WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.CRIPTKEY));
		
		
		criptKeyText = toolkit.createText(form.getBody(), 
										  cryptKey,
				                          SWT.FLAT|SWT.PASSWORD);
		
		criptKeyText.setLayoutData(gridData);

		Label l_login = toolkit.createLabel(form.getBody(), "Abilita login ");
		l_login.setBackground(l_login.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		login = toolkit.createButton(form.getBody(), "", SWT.CHECK);
		login.setSelection((WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
							? Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
							: false);

		Label l_isDefaultRuleRestrict = toolkit.createLabel(form.getBody(), "Se permessi non presenti vieta accesso ");
		l_isDefaultRuleRestrict.setBackground(l_login.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		isDefaultRuleRestrict = toolkit.createButton(form.getBody(), "", SWT.CHECK);
		isDefaultRuleRestrict.setSelection((WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null)
											? Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICT))
											: false);

		Label l_isDefaultRuleRestrictUI = toolkit.createLabel(form.getBody(), "Se permessi non presenti vieta accesso alle interfacce ");
		l_isDefaultRuleRestrictUI.setBackground(l_login.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		isDefaultRuleRestrictUI = toolkit.createButton(form.getBody(), "", SWT.CHECK);
		isDefaultRuleRestrictUI.setSelection((WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICTUI) != null)
											  ? Boolean.valueOf(WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.ISDEFAULTRULERESTRICTUI))
											  : false);

		return form;
	}

	@Override
	protected void performApply() {
		
		GDataHelper gdh = new GDataHelper();
		ArrayList<GDataVO> algdVOs = gdh.cleanCriptedStrings();
		
		String dummy = WinkhouseDBAgentUtils.getInstance().setSecret(criptKeyText.getText().trim());
		
		
		WinkSysDAO wsDAO = new WinkSysDAO();

		WinkSysVO wsvo = new WinkSysVO();
		wsvo.setPropertyName(IWinkSysProperties.CRIPTKEY);
		wsvo.setPropertyValue(dummy);
		
		if (wsDAO.getPropertyByName(IWinkSysProperties.LOGIN) != null){
			wsDAO.update(wsvo, null, true);
		}else{
			wsDAO.insert(wsvo, null, true);
		}

		wsvo.setPropertyName(IWinkSysProperties.LOGIN);
		wsvo.setPropertyValue(String.valueOf(login.getSelection()));
		
		if (wsDAO.getPropertyByName(IWinkSysProperties.LOGIN) != null){
			wsDAO.update(wsvo, null, true);
		}else{
			wsDAO.insert(wsvo, null, true);
		}
		
		wsvo.setPropertyName(IWinkSysProperties.ISDEFAULTRULERESTRICT);
		wsvo.setPropertyValue(String.valueOf(isDefaultRuleRestrict.getSelection()));
		
		if (wsDAO.getPropertyByName(IWinkSysProperties.ISDEFAULTRULERESTRICT) != null){
			wsDAO.update(wsvo, null, true);
		}else{
			wsDAO.insert(wsvo, null, true);
		}

		wsvo.setPropertyName(IWinkSysProperties.ISDEFAULTRULERESTRICTUI);
		wsvo.setPropertyValue(String.valueOf(isDefaultRuleRestrictUI.getSelection()));
		
		if (wsDAO.getPropertyByName(IWinkSysProperties.ISDEFAULTRULERESTRICTUI) != null){
			wsDAO.update(wsvo, null, true);
		}else{
			wsDAO.insert(wsvo, null, true);
		}
		
		WinkhouseDBAgentUtils.getInstance().setHm_winkSys(null);
		

		WinkhouseDBAgentUtils.getInstance().savePreference();

		gdh.decriptAll(algdVOs);
	}

	@Override
	public boolean performCancel() {
		if (criptKeyText != null)
			criptKeyText.setText((WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.CRIPTKEY) == null || 
					              WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.CRIPTKEY).equalsIgnoreCase(""))
        						  ? "winkhouse"
        						  : WinkhouseDBAgentUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.CRIPTKEY)
        						     );
		return true;
	}

	@Override
	protected void performDefaults() {
		criptKeyText.setText("winkhouse");
		
	}

	@Override
	public boolean performOk() {
		try{
			performApply();
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
