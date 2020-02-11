package r01ui.base.components.contact.phone;

import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import r01f.types.contact.ContactPhoneType;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.LangIndependentVaadinViewField;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01ui.base.components.contact.VaadinContactMeanDetailEditBase;
import r01ui.base.components.datetime.VaadinDateTimeRangeComponent;

public class VaadinContactPhoneDetailEdit
	 extends VaadinContactMeanDetailEditBase<VaadinViewContactPhone> {

	private static final long serialVersionUID = -3069365158386075376L;
/////////////////////////////////////////////////////////////////////////////////////////
//  UI
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactPhone.PHONE_NUMBER_FIELD,
					 required=true)
	@VaadinViewComponentLabels(captionI18NKey="contact.phone.number",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtNumber = new TextField();
	
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactPhone.TYPE_FIELD,
					 bindStringConverter=false,
					 required = true)
	@VaadinViewComponentLabels(captionI18NKey="contact.phone.type",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final ComboBox<ContactPhoneType> _cmbType = new ComboBox<ContactPhoneType>();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactPhone.AVAILABLE_RANGE,
					 bindStringConverter=false,
					 required=false)
	@LangIndependentVaadinViewField
	@VaadinViewComponentLabels(captionI18NKey="contact.phone.available",
							   useCaptionI18NKeyAsPlaceHolderKey=true)
	protected final VaadinDateTimeRangeComponent _dateTimeRange = new VaadinDateTimeRangeComponent();
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactPhoneDetailEdit(final UII18NService i18n) {
		super(VaadinViewContactPhone.class,
			  i18n);

		// phone number
		_txtNumber.setWidth(100,Unit.PERCENTAGE);
		_txtNumber.setReadOnly(false);
		_txtNumber.addStyleName(VaadinValoTheme.INPUT_MEDIUM_SIZE);

		// type
		_cmbType.setWidth(100,Unit.PERCENTAGE);
		_cmbType.setItems(ContactPhoneType.values());
		_cmbType.setReadOnly(false);
		_cmbType.addStyleName(VaadinValoTheme.COMBO_MEDIUM_SIZE);

		// availability: from - to
		_dateTimeRange.setResolution(DateTimeResolution.HOUR);
		
		// layout: DO NOT FORGET!
		this.addComponents(new HorizontalLayout(_cmbUsage,_txtNumber,_cmbType),
						   _dateTimeRange,
						   new HorizontalLayout(_chkDefault,_chkPrivate));

		////////// Init the form components (DO NOT FORGET!!)
		_initFormComponents();
	}
}
