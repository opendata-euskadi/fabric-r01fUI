package r01f.ui.vaadin.security.user;

import lombok.experimental.Accessors;
import r01f.model.security.login.Login;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors(prefix="_")
abstract class VaadinViewLogin<L extends Login>
	   extends UIViewObjectWrappedBase<L> {

	private static final long serialVersionUID = -940025946465996852L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewLogin(final L login) {
		super(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OID_FIELD = "oid";
	public LoginOID getOid() {
		return _wrappedModelObject.getOid();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER OID
/////////////////////////////////////////////////////////////////////////////////////////	
	public static final String USER_OID_FIELD = "userOid";
	public UserOID getUserOid() {
		return _wrappedModelObject.getUserOid();
	}
	public void setUserOid(final UserOID userOid) {
		_wrappedModelObject.setUserOid(userOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN ID
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String LOGIN_ID_FIELD = "loginId";
	public LoginID getLoginId() {
		return _wrappedModelObject.getLoginId();
	}
	public void setLoginId(final LoginID loginId) {
		_wrappedModelObject.setLoginId(loginId);
	}
}
