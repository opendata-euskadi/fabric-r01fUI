package r01ui.base.components.contact;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import r01f.locale.Language;
import r01f.types.contact.ContactMeanType;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.VaadinViewField;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewHasVaadinViewObjectBinder;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.locale.Languages;
import r01ui.base.components.contact.email.VaadinContactEMailManage;
import r01ui.base.components.contact.phone.VaadinContactPhoneManage;
import r01ui.base.components.contact.socialnetwork.VaadinContactSocialNetworkManage;
import r01ui.base.components.contact.website.VaadinContactWebSiteManage;

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
  implements Component,
			 VaadinViewI18NMessagesCanBeUpdated,
			 VaadinViewHasVaadinViewObjectBinder<VaadinViewContactInfo> {

	private static final long serialVersionUID = -543903229607808643L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Collection<ContactMeanType> _allowedMediumTypes;

	private final VaadinContactEMailManage _emailsComponent;
	private final VaadinContactPhoneManage _phonesComponent;
	private final VaadinContactSocialNetworkManage _socialNetworksComponent;
	private final VaadinContactWebSiteManage _webSitesComponent;

	@VaadinViewField(bindToViewObjectFieldNamed=VaadinViewContactInfo.PREFERED_LANGUAGE_CHANNELS_FIELD,
					 bindStringConverter=false,
					 required=true)
	private final ComboBox<Language> _cmbPreferedLanguage = new ComboBox<Language>();

/////////////////////////////////////////////////////////////////////////////////////////
//	BINDED OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This field ONLY is set when calling #bindViewTo
	 * and is ONLY readed when calling #getViewObject
	 */
	private VaadinViewContactInfo _bindedViewObj;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactInfoManageComponent(final UII18NService i18n) {
		this(i18n,
			 Lists.newArrayList(ContactMeanType.values()));
	}
	public VaadinContactInfoManageComponent(final UII18NService i18n,
										    final Collection<ContactMeanType> types) {
		_allowedMediumTypes = types;

		// create the components
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

		Label labelOthers = new Label(i18n.getMessage("contact.others"));
		labelOthers.addStyleName(VaadinValoTheme.LABEL_AND_ADD_BUTTON);
		VerticalLayout vlOthers = new VerticalLayout(labelOthers,
													 _cmbPreferedLanguage);
		vlOthers.addStyleName(VaadinValoTheme.NO_PADDING_TOP);
		vlOthers.addStyleName(VaadinValoTheme.NO_PADDING_LEFT);

		// add to the layout
		if (_emailsComponent != null) 			this.addComponent(_emailsComponent);
		if (_phonesComponent != null) 			this.addComponent(_phonesComponent);
		if (_socialNetworksComponent != null) 	this.addComponent(_socialNetworksComponent);
		if (_webSitesComponent != null) 		this.addComponent(_webSitesComponent);
		this.addComponent(vlOthers);
		this.addStyleName(VaadinValoTheme.NO_PADDING);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[VIEW OBJECT] > [UI-CONTROLS]
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void bindViewTo(final VaadinViewContactInfo viewObj) {
		// Set the [ui control] values from [view object properties]
		// (unlike binder.readBean [ui controls] are binded to [view object properties]
		//  so when an [ui control] changes, the [view object property] is also changed)
		if (viewObj == null) throw new IllegalArgumentException("Cannot bind a null object!");

		// store the view object
		_bindedViewObj = viewObj;

		// Ensure the [view object] contains a collection for each underlying contact object
		if (_bindedViewObj.getWrappedModelObject().getContactMails() == null) _bindedViewObj.setViewContactMails(Lists.newArrayList());
		if (_bindedViewObj.getWrappedModelObject().getContactPhones() == null) _bindedViewObj.setViewContactPhones(Lists.newArrayList());
		if (_bindedViewObj.getWrappedModelObject().getContactSocialNetworks() == null) _bindedViewObj.setViewContactSocialNetworks(Lists.newArrayList());
		if (_bindedViewObj.getWrappedModelObject().getContactWebSites() == null) _bindedViewObj.setViewContactWebSites(Lists.newArrayList());

		// bind the individual components to the [view object] underlying collection
		_emailsComponent.setItems(_bindedViewObj.getViewContactMails());
		_phonesComponent.setItems(_bindedViewObj.getViewContactPhones());
		_socialNetworksComponent.setItems(_bindedViewObj.getViewContactSocialNetworks());
		_webSitesComponent.setItems(_bindedViewObj.getViewContactWebSites());
		if (_bindedViewObj.getWrappedModelObject().getPreferedLanguage() != null) {
			_cmbPreferedLanguage.setSelectedItem(_bindedViewObj.getPreferedLanguage());
		}
	}
	@Override
	public void readBean(final VaadinViewContactInfo viewObj) {
		// Set the [ui control] values from [view object properties]
		// (the [ui controls] are NOT binded to [view object properties]
		//  so when an [ui control] changes, the change is NOT reflected
		//  at the [view object property]
		if (viewObj == null) throw new IllegalArgumentException("Cannot read a null object!");

		// BEWARE!! the view object is NOT stored
		_bindedViewObj = null;

		// bind the individual components to the [view object] underlying collection
		_emailsComponent.setItems(viewObj.hasMailAddresses() ? viewObj.getViewContactMails()
													 		 : Lists.newArrayList());
		_phonesComponent.setItems(viewObj.hasPhones() ? viewObj.getViewContactPhones()
													  : Lists.newArrayList());
		_socialNetworksComponent.setItems(viewObj.hasSocialNetworks() ? viewObj.getViewContactSocialNetworks()
																	  : Lists.newArrayList());
		_webSitesComponent.setItems(viewObj.hasWebSites() ? viewObj.getViewContactWebSites()
														  : Lists.newArrayList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[UI-CONTROLS] > [VIEW OBJECT]
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public VaadinViewContactInfo getViewObject() {
		// update the wrapped view object with the individual components values
		if (_bindedViewObj == null) throw new IllegalStateException("There's NO binded [view object]: the [view object] MUST have been binded using #bindViewTo() (do NOT use #readBean())");

		// ensure the binded [view object] is updated
		_bindedViewObj.setViewContactMails(_emailsComponent.getItems());
		_bindedViewObj.setViewContactPhones(_phonesComponent.getItems());
		_bindedViewObj.setViewContactSocialNetworks(_socialNetworksComponent.getItems());
		_bindedViewObj.setViewContactWebSites(_webSitesComponent.getItems());
		_bindedViewObj.setPreferedLanguage(_cmbPreferedLanguage.getValue());
		return _bindedViewObj;
	}
	@Override
	public boolean writeBeanIfValid(final VaadinViewContactInfo viewObj) {
		// writes the [ui contro] field values to the corresponding [view objet properties]
		viewObj.setViewContactMails(_emailsComponent.getItems());
		viewObj.setViewContactPhones(_phonesComponent.getItems());
		viewObj.setViewContactSocialNetworks(_socialNetworksComponent.getItems());
		viewObj.setViewContactWebSites(_webSitesComponent.getItems());
		viewObj.setPreferedLanguage(_cmbPreferedLanguage.getValue());
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isValid() {
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		if (_emailsComponent != null) _emailsComponent.updateI18NMessages(i18n);
		if (_phonesComponent != null) _phonesComponent.updateI18NMessages(i18n);
		if (_socialNetworksComponent != null) _socialNetworksComponent.updateI18NMessages(i18n);
		if (_webSitesComponent != null) _webSitesComponent.updateI18NMessages(i18n);
	}
}
