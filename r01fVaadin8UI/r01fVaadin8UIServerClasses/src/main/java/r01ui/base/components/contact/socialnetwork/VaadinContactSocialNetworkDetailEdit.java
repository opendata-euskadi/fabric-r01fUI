package r01ui.base.components.contact.socialnetwork;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import r01f.types.contact.ContactSocialNetworkType;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01ui.base.components.contact.VaadinContactMeanDetailEditBase;

public class VaadinContactSocialNetworkDetailEdit
	 extends VaadinContactMeanDetailEditBase<VaadinViewContactSocialNetwork> {

	private static final long serialVersionUID = -3069365158386075376L;
/////////////////////////////////////////////////////////////////////////////////////////
//  UI
/////////////////////////////////////////////////////////////////////////////////////////
	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactSocialNetwork.TYPE_FIELD,
					 bindStringConverter=false,
					 required=true)
	@VaadinViewComponentLabels(captionI18NKey="contact.socialNetwork.type",useCaptionI18NKeyAsPlaceHolderKey=true)
	protected final ComboBox<ContactSocialNetworkType> _cmbType = new ComboBox<ContactSocialNetworkType>();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactSocialNetwork.USER_FIELD,required=true)
	@VaadinViewComponentLabels(captionI18NKey="contact.socialNetwork.user",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtUser = new TextField();

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactSocialNetwork.PROFILE_URL_FIELD,required=false)
	@VaadinViewComponentLabels(captionI18NKey="contact.socialNetwork.profile.url",useCaptionI18NKeyAsPlaceHolderKey=true)
	private final TextField _txtProfileUrl = new TextField();
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactSocialNetworkDetailEdit(final UII18NService i18n) {
		super(VaadinViewContactSocialNetwork.class,
			  i18n);
		// type
		_cmbType.setItems(ContactSocialNetworkType.values());
		_cmbType.setItemCaptionGenerator(type -> type.nameUsing(i18n));
		_cmbType.addStyleName(VaadinValoTheme.COMBO_MEDIUM_SIZE);

		// user
		_txtUser.setWidth(100,Unit.PERCENTAGE);
		_txtUser.setReadOnly(false);
		_txtUser.addStyleName(VaadinValoTheme.INPUT_MEDIUM_SIZE);

		// profileUrl
		_txtProfileUrl.setWidth(100,Unit.PERCENTAGE);
		_txtProfileUrl.setReadOnly(false);
		_txtProfileUrl.addStyleName(VaadinValoTheme.INPUT_HUGE_SIZE);

		// layout: DO NOT FORGET!
		super.addComponents(new HorizontalLayout(_cmbUsage,_cmbType,_txtUser),
							new HorizontalLayout(_txtProfileUrl),
						    new HorizontalLayout(_chkDefault,_chkPrivate));

		////////// Init the form components (DO NOT FORGET!!)
		_initFormComponents();
	}
}
