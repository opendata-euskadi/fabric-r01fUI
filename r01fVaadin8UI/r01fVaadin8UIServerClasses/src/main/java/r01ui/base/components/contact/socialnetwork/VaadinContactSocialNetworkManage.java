package r01ui.base.components.contact.socialnetwork;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.contact.VaadinContactMeanManageBase;

public class VaadinContactSocialNetworkManage
	 extends VaadinContactMeanManageBase<VaadinViewContactSocialNetwork,
	 									 VaadinContactSocialNetworkDetailEditWindow> {

	private static final long serialVersionUID = 793707311914095044L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactSocialNetworkManage(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactSocialNetworkDetailEditWindow(i18n),
			  VaadinViewContactSocialNetwork::new,			// factory
			  I18NKey.named("contact.socialNetwork.list.title"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void _configureMoreGridColumns(final UII18NService i18n) {
		////////// Columns
		// type
		_grid.addColumn(VaadinViewContactSocialNetwork::getType)
		   	 .setCaption(i18n.getMessage("contact.socialNetwork.type"))
		   	 .setExpandRatio(1)
		   	 .setResizable(false)
		   	 .setId("type");
		// user
		_grid.addColumn(VaadinViewContactSocialNetwork::getUser)
			 .setCaption(i18n.getMessage("contact.socialNetwork.user"))
			 .setExpandRatio(1)
			 .setResizable(false)
			 .setId("user");
		// profile
		_grid.addColumn(VaadinViewContactSocialNetwork::getProfileUrl)
			 .setCaption(i18n.getMessage("contact.socialNetwork.profile.url"))
			 .setExpandRatio(1)
			 .setResizable(false)
			 .setId("profileUrl");
	}
}
