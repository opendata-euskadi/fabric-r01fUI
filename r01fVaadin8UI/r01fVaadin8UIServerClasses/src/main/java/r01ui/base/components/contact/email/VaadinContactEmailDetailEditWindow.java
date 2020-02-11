package r01ui.base.components.contact.email;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.VaadinDetailEditWindowBase;

public class VaadinContactEmailDetailEditWindow
     extends VaadinDetailEditWindowBase<VaadinViewContactEmail,
			 						    VaadinContactEmailDetailEdit> {

	private static final long serialVersionUID = 5765469965554072345L;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactEmailDetailEditWindow(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactEmailDetailEdit(i18n));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("contact.email.new");
	}
	@Override
	public I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("contact.email.edit");
	}
}
