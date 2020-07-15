package r01ui.base.components.contact.website;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;

public class VaadinContactWebSiteDetailEditWindow
     extends VaadinDetailEditFormWindowBase<VaadinViewDirectoryContactWebSite,
			 						    	VaadinContactWebSiteDetailEdit> {

	private static final long serialVersionUID = 2550850462553697426L;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactWebSiteDetailEditWindow(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactWebSiteDetailEdit(i18n),		// form
			  VaadinViewDirectoryContactWebSite::new);		// view obj factory
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("contact.web.new");
	}
	@Override
	public I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("contact.web.edit");
	}
}
