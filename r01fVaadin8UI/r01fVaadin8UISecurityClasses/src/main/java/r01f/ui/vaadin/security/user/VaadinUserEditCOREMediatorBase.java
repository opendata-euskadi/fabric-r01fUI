package r01f.ui.vaadin.security.user;

import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.coremediator.UICOREMediator;

public abstract class VaadinUserEditCOREMediatorBase<U extends User,L extends UserPasswordLogin,
												     A extends SecurityAPIBase<U,L,
												   							   ?,	// user login entry
												   							   ?,?,	// auth target resource
												   							   ?>>	// user auth on resource
  		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final A _securityApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinUserEditCOREMediatorBase(final A securityApi) {
		_securityApi = securityApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER
/////////////////////////////////////////////////////////////////////////////////////////
	public U loadUser(final UserOID userOid) {
		return _securityApi.getUsersApi()
						   .getForCrud()
						   .load(userOid);	// throws an exception if the user does NOT exists
	}
	public U updateUser(final U user) {
		return _securityApi.getUsersApi()
						   .getForCrud()
						   .update(user);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER PASSWORD LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public L loadUserPasswordLogin(final UserOID userOid) {
		return _securityApi.getUserPasswordLoginApi()
						   .getForCrud()
						   .loadBy(userOid);	// throws an exception if the user does NOT have a user-password login
	}
	public boolean userPasswordLoginExists(final LoginID loginId) {
		return _securityApi.getUserPasswordLoginApi()
						   .getForCrud()
						   .loadByOrNull(loginId) != null;
	}
	public L createUserPasswordLogin(final L userPasswordLogin) {
		return _securityApi.getUserPasswordLoginApi()
						   .getForCrud()
						   .create(userPasswordLogin);
	}
	public L changeUserPasswordLoginId(final UserOID userOid,final LoginID newLoginId) {
		return _securityApi.getUserPasswordLoginApi()
						   .getForCrud()
						   .changeLoginId(userOid,newLoginId);
	}
	public L changeUserPasswordLoginPassword(final LoginID loginId,final Password oldPwd,final Password newPwd) {
		return _securityApi.getUserPasswordLoginApi()
						   .getForCrud()
						   .updatePassword(loginId,oldPwd,newPwd);
	}
}
