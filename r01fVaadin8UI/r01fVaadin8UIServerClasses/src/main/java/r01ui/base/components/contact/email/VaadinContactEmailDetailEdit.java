package r01ui.base.components.contact.email;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01ui.base.components.contact.VaadinContactMeanDetailEditBase;

public class VaadinContactEmailDetailEdit
	 extends VaadinContactMeanDetailEditBase<VaadinViewContactEmail> {

	private static final long serialVersionUID = -829148065680140801L;

/////////////////////////////////////////////////////////////////////////////////////////
//  UI
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactEmail.EMAIL_FIELD,required=true)
	@VaadinViewComponentLabels(captionI18NKey="contact.email.email",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtEmail = new TextField();

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactEmailDetailEdit(final UII18NService i18n) {
		super(VaadinViewContactEmail.class,
			  i18n);
		// email
		_txtEmail.setWidth(100,Unit.PERCENTAGE);
		_txtEmail.setReadOnly(false);
		
		// layout: DO NOT FORGET! 
		HorizontalLayout hl = new HorizontalLayout(_cmbUsage,_txtEmail);
		hl.setSizeFull();
		hl.setExpandRatio(_txtEmail, 2);
		hl.setExpandRatio(_cmbUsage, 1);
		super.addComponents(hl,
						    new HorizontalLayout(_chkDefault,_chkPrivate));

		////////// Init the form components (DO NOT FORGET!!)
		_initFormComponents();
	}
}
