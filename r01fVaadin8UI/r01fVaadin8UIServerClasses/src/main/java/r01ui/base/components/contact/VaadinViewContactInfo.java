package r01ui.base.components.contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.types.contact.ContactInfo;
import r01f.types.contact.ContactMail;
import r01f.types.contact.ContactPhone;
import r01f.types.contact.ContactSocialNetwork;
import r01f.types.contact.ContactWeb;
import r01f.ui.viewobject.UIViewObjectWrappedBase;
import r01f.util.types.collections.CollectionUtils;
import r01ui.base.components.contact.email.VaadinViewContactEmail;
import r01ui.base.components.contact.phone.VaadinViewContactPhone;
import r01ui.base.components.contact.socialnetwork.VaadinViewContactSocialNetwork;
import r01ui.base.components.contact.website.VaadinViewDirectoryContactWebSite;

@Accessors(prefix = "_")
public class VaadinViewContactInfo
     extends UIViewObjectWrappedBase<ContactInfo> {

	private static final long serialVersionUID = -2722608269795512772L;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewContactInfo() {
		this(new ContactInfo());
	}
	public VaadinViewContactInfo(final ContactInfo contactInfo) {
		super(contactInfo);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONTACT INFO - emailChannels
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<VaadinViewContactEmail> getViewContactMails() {
		return this.hasMailAddresses()  ? _wrappedModelObject.getContactMails()
														     .stream()
														     .map(VaadinViewContactEmail::new)
														     .collect(Collectors.toList())
										: new ArrayList<VaadinViewContactEmail>();
	}
	public void setViewContactMails(final Collection<VaadinViewContactEmail> viewObjs) {
		Collection<ContactMail> mails = CollectionUtils.hasData(viewObjs)
											? viewObjs.stream()
													  .map(viewObj -> viewObj.getWrappedModelObject())
													  .collect(Collectors.toList())
											: null;
		_wrappedModelObject.setContactMails(mails);
	}
	public boolean hasMailAddresses() {
		return _wrappedModelObject != null ? _wrappedModelObject.hasMails() : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONTACT INFO - phoneChannels
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<VaadinViewContactPhone> getViewContactPhones() {
		return this.hasPhones() ? _wrappedModelObject.getContactPhones()
									 				  .stream()
													  .map(VaadinViewContactPhone::new)
													  .collect(Collectors.toList())
								: new ArrayList<VaadinViewContactPhone>();
	}
	public void setViewContactPhones(final Collection<VaadinViewContactPhone> viewObjs) {
		Collection<ContactPhone> phones = CollectionUtils.hasData(viewObjs)
											? viewObjs.stream()
													  .map(viewObj -> viewObj.getWrappedModelObject())
													  .collect(Collectors.toList())
											: null;
		_wrappedModelObject.setContactPhones(phones);
	}
	public boolean hasPhones() {
		return _wrappedModelObject != null ? _wrappedModelObject.hasPhones() : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONTACT INFO - social networks
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<VaadinViewContactSocialNetwork> getViewContactSocialNetworks() {
		return this.hasSocialNetworks() ? _wrappedModelObject.getContactSocialNetworks()
															 .stream()
															 .map(VaadinViewContactSocialNetwork::new)
															 .collect(Collectors.toList())
										: new ArrayList<VaadinViewContactSocialNetwork>();
	}
	public void setViewContactSocialNetworks(final Collection<VaadinViewContactSocialNetwork> viewObjs) {
		Collection<ContactSocialNetwork> nets = CollectionUtils.hasData(viewObjs)
													? viewObjs.stream()
															  .map(viewObj -> viewObj.getWrappedModelObject())
															  .collect(Collectors.toList())
													: null;
		_wrappedModelObject.setContactSocialNetworks(nets);
	}
	public boolean hasSocialNetworks() {
		return _wrappedModelObject != null ? _wrappedModelObject.hasSocialNetworks() : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONTACT INFO - web sites
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<VaadinViewDirectoryContactWebSite> getViewContactWebSites() {
		return this.hasWebSites() ? _wrappedModelObject.getContactWebSites()
														.stream()
														.map(VaadinViewDirectoryContactWebSite::new)
														.collect(Collectors.toList())
								  : new ArrayList<VaadinViewDirectoryContactWebSite>();
	}
	public void setViewContactWebSites(final Collection<VaadinViewDirectoryContactWebSite> viewObjs) {
		Collection<ContactWeb> nets = CollectionUtils.hasData(viewObjs)
											? viewObjs.stream()
													  .map(viewObj -> viewObj.getWrappedModelObject())
													  .collect(Collectors.toList())
											: null;
		_wrappedModelObject.setContactWebSites(nets);
	}
	public boolean hasWebSites() {
		return _wrappedModelObject != null ? _wrappedModelObject.hasWebSites() : false;
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	PREFERED LANGUAGE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PREFERED_LANGUAGE_CHANNELS_FIELD = "preferredLanguage";
	public Language getPreferedLanguage() {
		return _wrappedModelObject.getPreferedLanguage() != null ? _wrappedModelObject.getPreferedLanguage() 
																 : null;
	}
	public void setPreferedLanguage(final Language language) {
		_wrappedModelObject.setPreferedLanguage(language);
	}
}
