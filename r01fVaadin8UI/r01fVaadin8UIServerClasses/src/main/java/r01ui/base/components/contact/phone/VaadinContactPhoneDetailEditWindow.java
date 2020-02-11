package r01ui.base.components.contact.phone;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.VaadinDetailEditWindowBase;

public class VaadinContactPhoneDetailEditWindow
     extends VaadinDetailEditWindowBase<VaadinViewContactPhone,
			 					 	   	VaadinContactPhoneDetailEdit> {
	private static final long serialVersionUID = -8502135692216603837L;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactPhoneDetailEditWindow(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactPhoneDetailEdit(i18n));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("contact.phone.new");
	}
	@Override
	public I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("contact.phone.edit");
	}
}
