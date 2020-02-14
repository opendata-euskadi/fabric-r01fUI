package r01ui.base.components.contact.website;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01ui.base.components.contact.VaadinContactMeanDetailEditBase;

public class VaadinContactWebSiteDetailEdit
	 extends VaadinContactMeanDetailEditBase<VaadinViewDirectoryContactWebSite> {

	private static final long serialVersionUID = -3069365158386075376L;
/////////////////////////////////////////////////////////////////////////////////////////
//  UI
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewDirectoryContactWebSite.URL_FIELD,required=true)
	@VaadinViewComponentLabels(captionI18NKey="contact.web.url",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtUrl = new TextField();

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactWebSiteDetailEdit(final UII18NService i18n) {
		super(VaadinViewDirectoryContactWebSite.class,
			  i18n);

		// url
		_txtUrl.setWidth(100,Unit.PERCENTAGE);
		_txtUrl.setReadOnly(false);
		_txtUrl.addStyleName(VaadinValoTheme.INPUT_HUGE_SIZE);
		
		// layout: DO NOT FORGET! 
		super.addComponents(new HorizontalLayout(_cmbUsage),
							new HorizontalLayout(_txtUrl),						    new HorizontalLayout(_chkDefault,_chkPrivate));

		////////// Init the form components (DO NOT FORGET!!)
		_initFormComponents();
	}
}
