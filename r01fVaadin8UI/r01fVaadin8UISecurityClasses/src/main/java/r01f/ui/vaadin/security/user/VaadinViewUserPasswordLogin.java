package r01f.ui.vaadin.security.user;

import lombok.experimental.Accessors;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors(prefix="_")
public abstract class VaadinViewUserPasswordLogin<L extends UserPasswordLogin>
	 		  extends UIViewObjectWrappedBase<L> {

	private static final long serialVersionUID = -1260091998739557900L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewUserPasswordLogin(final L login) {
		super(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID & USER CODE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OID_FIELD = "oid";
	public LoginOID getOid() {
		return _wrappedModelObject.getOid();
	}
	
	public static final String USER_OID_FIELD = "userOid";
	public UserOID getUserOid() {
		return _wrappedModelObject.getUserOid();
	}
	public void setUserOid(final UserOID userOid) {
		_wrappedModelObject.setUserOid(userOid);
	}

	public static final String LOGIN_ID_FIELD = "loginId";
	public LoginID getLoginId() {
		return _wrappedModelObject.getLoginId();
	}
	public void setLoginId(final LoginID loginId) {
		_wrappedModelObject.setLoginId(loginId);
	}
	
	public static final String PASSWORD_FIELD = "password";
	public Password getPassword() {
		return _wrappedModelObject.getPassword();
	}
	public void setPassword(final Password password) {
		_wrappedModelObject.setPassword(password);
	}
}
