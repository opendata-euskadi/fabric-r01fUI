package r01ui.base.components.contact;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

import r01f.locale.Language;
import r01f.types.contact.ContactMeanType;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Strings;
import r01f.util.types.locale.Languages;
import r01ui.base.components.contact.email.VaadinContactEMailManage;
import r01ui.base.components.contact.nora.VaadinNORAContactComponent;
import r01ui.base.components.contact.nora.VaadinNORAContactFormPresenter;
import r01ui.base.components.contact.phone.VaadinContactPhoneManage;
import r01ui.base.components.contact.socialnetwork.VaadinContactSocialNetworkManage;
import r01ui.base.components.contact.website.VaadinContactWebSiteManage;
import r01ui.base.components.form.VaadinFormEditsViewObject;
import r01ui.base.components.tinyeditor.TinyMCETextFieldComponent;

/**
 * A configurable [contact info] component like:
 * <pre>
 *    +++++++++++++++++++++++++++++++++++++++++++++++
 *	  + +-----------------------------------------+ +
 *	  + | EMails Component						  | +
 *	  + +-----------------------------------------+ +
 *	  + +-----------------------------------------+ +
 *	  + | Phones Component						  | +
 *	  + +-----------------------------------------+ +
 *	  + +-----------------------------------------+ +
 *	  + | Social Networks Component			      | +
 *	  + +-----------------------------------------+ +
 *	  + +-----------------------------------------+ +
 *	  + | Web Sites Component					  | +
 *	  + +-----------------------------------------+ +
 *	  + +-----------------------------------------+ +
 *	  + | Prefered language combo				  | +
 *	  + +-----------------------------------------+ +
 *	  +++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinContactInfoManageComponent
	 extends VerticalLayout
  implements VaadinFormEditsViewObject<VaadinViewContactInfo>,
  			 VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -543903229607808643L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Collection<ContactMeanType> _allowedMediumTypes;
	protected final VaadinNORAContactComponent _noraComponent;
	//private final TinyMCETextFieldComponent _txtGeoPosition;
	protected final VaadinContactEMailManage _emailsComponent;
	protected final VaadinContactPhoneManage _phonesComponent;
	protected final VaadinContactSocialNetworkManage _socialNetworksComponent;
	protected final VaadinContactWebSiteManage _webSitesComponent;
	private final UII18NService _i18n;

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactInfo.PREFERED_LANGUAGE_CHANNELS_FIELD,
					 bindStringConverter=false,
					 required=true)
	private final ComboBox<Language> _cmbPreferedLanguage = new ComboBox<>();

/////////////////////////////////////////////////////////////////////////////////////////
//	BINDED OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
//	/**
//	 * This field ONLY is set when calling #bindViewTo
//	 * and is ONLY readed when calling #getViewObject
//	 */
//	private VaadinViewContactInfo _viewObject;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactInfoManageComponent(final UII18NService i18n) {
		this(i18n,
			 Lists.newArrayList(ContactMeanType.values()));
	}
	public VaadinContactInfoManageComponent(final UII18NService i18n,
										    final Collection<ContactMeanType> types) {
		this(i18n, types, null);
	}
	public VaadinContactInfoManageComponent(final UII18NService i18n,
										    final Collection<ContactMeanType> types,
										    final VaadinNORAContactFormPresenter noraPresenter ) {
		_i18n = i18n;
		_allowedMediumTypes = types;

		
		
		_emailsComponent = types.contains(ContactMeanType.EMAIL) ? new VaadinContactEMailManage(i18n) : null;
		_phonesComponent = types.contains(ContactMeanType.PHONE) ? new VaadinContactPhoneManage(i18n) : null;
		_socialNetworksComponent = types.contains(ContactMeanType.SOCIAL_NETWORK) ? new VaadinContactSocialNetworkManage(i18n) : null;
		_webSitesComponent = types.contains(ContactMeanType.WEB_SITE) ? new VaadinContactWebSiteManage(i18n) : null;

		// preferred language
		_cmbPreferedLanguage.setWidth(100,Unit.PERCENTAGE);
		_cmbPreferedLanguage.setItems(Language.values());
		_cmbPreferedLanguage.setReadOnly(false);
		_cmbPreferedLanguage.setTextInputAllowed(false);
		_cmbPreferedLanguage.setCaption(i18n.getMessage("contact.preferredLanguage"));
		_cmbPreferedLanguage.setItemCaptionGenerator(lang -> Languages.nameUsing(i18n)
															  		  .of(lang));
		_cmbPreferedLanguage.addStyleName(VaadinValoTheme.COMBO_MEDIUM_SIZE);

		////////// Layout
		// geo position
		_noraComponent = new VaadinNORAContactComponent(i18n, noraPresenter);
		_noraComponent.setCaption(i18n.getMessage("geo.address"));
		
		// contact
		VerticalLayout vlContact = new VerticalLayout();
		vlContact.setCaption(i18n.getMessage("contact"));
		vlContact.setMargin(false);
		vlContact.addStyleName(VaadinValoTheme.NO_PADDING_TOP);
		vlContact.addStyleName(VaadinValoTheme.NO_PADDING_LEFT);
		if (_emailsComponent != null) 			vlContact.addComponent(_emailsComponent);
		if (_phonesComponent != null) 			vlContact.addComponent(_phonesComponent);
		if (_socialNetworksComponent != null) 	vlContact.addComponent(_socialNetworksComponent);
		if (_webSitesComponent != null) 		vlContact.addComponent(_webSitesComponent);
		
		// others
		VerticalLayout vlOthers = new VerticalLayout(_cmbPreferedLanguage);
		vlOthers.setCaption(i18n.getMessage("contact.others").toUpperCase());
		vlOthers.setMargin(true);
		vlOthers.addStyleName(VaadinValoTheme.LAYOUT_WHITE_BORDERED);

		// main layout
		//this.addComponent(_txtGeoPosition);
		if(noraPresenter != null) {
			this.addComponent(_noraComponent);
		}
		this.addComponent(vlContact);
		this.addComponent(vlOthers);
		this.addStyleName(VaadinValoTheme.NO_PADDING);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Binding
/////////////////////////////////////////////////////////////////////////////////////////
	////////// [viewObject] > [UI control] --------------
	@Override
	public void editViewObject(final VaadinViewContactInfo viewObj) {
		// Set the [ui control] values from [view object]'s properties
		if (viewObj == null) throw new IllegalArgumentException("Cannot bind a null object!");

		// bind the individual components to the [view object] underlying collection
		_noraComponent.setValue(viewObj.getViewGeoPosition());
		_emailsComponent.setItems(viewObj.getViewContactMails());
		_phonesComponent.setItems(viewObj.getViewContactPhones());
		_socialNetworksComponent.setItems(viewObj.getViewContactSocialNetworks());
		_webSitesComponent.setItems(viewObj.getViewContactWebSites());
		if (viewObj.getWrappedModelObject().getPreferedLanguage() != null) {
			_cmbPreferedLanguage.setSelectedItem(viewObj.getPreferedLanguage());
		}
	}
	////////// [UI control] > [viewObject] --------------
	@Override
	public void writeAsDraftEditedViewObjectTo(final VaadinViewContactInfo viewObj) {
		// ensure the binded [view object] is updated
		viewObj.setViewGeoPosition(_noraComponent.getValue());
		viewObj.setViewContactMails(_emailsComponent.getItems());
		viewObj.setViewContactPhones(_phonesComponent.getItems());
		viewObj.setViewContactSocialNetworks(_socialNetworksComponent.getItems());
		viewObj.setViewContactWebSites(_webSitesComponent.getItems());
		viewObj.setPreferedLanguage(_cmbPreferedLanguage.getValue());
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final VaadinViewContactInfo viewObj) {
		this.writeAsDraftEditedViewObjectTo(viewObj);
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_noraComponent.setCaption(i18n.getMessage("geo.address"));
		if (_emailsComponent != null) _emailsComponent.updateI18NMessages(i18n);
		if (_phonesComponent != null) _phonesComponent.updateI18NMessages(i18n);
		if (_socialNetworksComponent != null) _socialNetworksComponent.updateI18NMessages(i18n);
		if (_webSitesComponent != null) _webSitesComponent.updateI18NMessages(i18n);
	}
}
