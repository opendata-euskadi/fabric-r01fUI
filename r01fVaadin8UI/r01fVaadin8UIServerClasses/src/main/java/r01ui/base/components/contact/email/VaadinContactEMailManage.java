package r01ui.base.components.contact.email;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.contact.VaadinContactMeanManageBase;

public class VaadinContactEMailManage
	 extends VaadinContactMeanManageBase<VaadinViewContactEmail,
	 									 VaadinContactEmailDetailEditWindow> {

	private static final long serialVersionUID = 793707311914095044L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactEMailManage(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactEmailDetailEditWindow(i18n),
			  VaadinViewContactEmail::new,			// factory
			  I18NKey.named("contact.email.list.title"));		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void _configureMoreGridColumns(final UII18NService i18n) {
		////////// Columns
		// email
		_grid.addColumn(VaadinViewContactEmail::getMail)
			 .setCaption(i18n.getMessage("contact.email.email"))
			 .setExpandRatio(3)
			 .setResizable(false)
			 .setId("email");
	}
}
