package r01ui.base.components.contact.socialnetwork;

import lombok.experimental.Accessors;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.ContactSocialNetwork;
import r01f.types.contact.ContactSocialNetworkType;
import r01f.types.url.Url;
import r01f.util.types.Strings;
import r01ui.base.components.contact.VaadinContactMeanObjectBase;

@Accessors( prefix="_" )
public class VaadinViewContactSocialNetwork
     extends VaadinContactMeanObjectBase<ContactSocialNetwork> {

	private static final long serialVersionUID = 981595816336929199L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String USER_FIELD = "user";
	public LoginID getUser() {
		return _wrappedModelObject.getUser();
	}
	public String getUserAsString() {
		return this.getUser() != null ? this.getUser().asString() : null;
	}
	public void setUser(final LoginID userCode) {
		_wrappedModelObject.setUser(userCode);
	}
	public void setUserFromString(final String userCode) {
		if (Strings.isNullOrEmpty(userCode)) {
			_wrappedModelObject.setUser(null);
		} else {
			_wrappedModelObject.setUser(LoginID.forId(userCode));
		}
	}

	public static final String TYPE_FIELD = "type";
	public ContactSocialNetworkType getType() {
		return _wrappedModelObject.getType();
	}
	public void setType(final ContactSocialNetworkType type) {
		_wrappedModelObject.setType(type);
	}

	public static final String PROFILE_URL_FIELD = "profileUrl";
	public Url getProfileUrl() {
		return _wrappedModelObject.getProfileUrl();
	}
	public String getProfileUrlAsString() {
		return this.getProfileUrl() != null ? this.getProfileUrl().asString() : null;
	}
	public void setProfileUrl(final Url url) {
		_wrappedModelObject.setProfileUrl(url);
	}
	public void setProfileUrlFromString(final String url) {
		if (Strings.isNullOrEmpty(url)) {
			_wrappedModelObject.setProfileUrl(null);
		} else {
			_wrappedModelObject.setProfileUrl(Url.from(url));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewContactSocialNetwork() {
		this(new ContactSocialNetwork());
	}
	public VaadinViewContactSocialNetwork(final ContactSocialNetwork socialNet) {
		super(socialNet);
	}
}
