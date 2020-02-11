package r01ui.base.components.contact.socialnetwork;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.VaadinDetailEditWindowBase;

public class VaadinContactSocialNetworkDetailEditWindow
     extends VaadinDetailEditWindowBase<VaadinViewContactSocialNetwork,
			 						   	VaadinContactSocialNetworkDetailEdit> {

	private static final long serialVersionUID = 2550850462553697426L;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactSocialNetworkDetailEditWindow(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactSocialNetworkDetailEdit(i18n));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("contact.socialNetwork.new");
	}
	@Override
	public I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("contact.socialNetwork.edit");
	}
}
