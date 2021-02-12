package r01f.ui.vaadin.security.login;

import java.util.Locale;

import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

import lombok.experimental.Accessors;
import r01f.client.api.security.SecurityAPIBase;
import r01f.guids.CommonOIDs.WebSessionID;
import r01f.locale.Language;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseOK;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.user.User;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.EMail;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.util.types.locale.Languages;

@Accessors(prefix="_")
public abstract class VaadinLoginPresenterBase<U extends User,
											   L extends UserPasswordLogin,
											   C extends VaadinLoginCOREMediatorBase<U,L,
											   										 ? extends SecurityAPIBase<U,L,?,?,?,?,?,?>>>
		   implements UIPresenter {

	private static final long serialVersionUID = -4290934932756711025L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final C _coreMediator;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginPresenterBase(final C authCoreMediator) {
		_coreMediator = authCoreMediator;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN EXISTENCE
/////////////////////////////////////////////////////////////////////////////////////////
	public void onUserPasswordLoginExistenceCheckRequested(final LoginID loginId,
														   final UIPresenterSubscriber<Boolean> subscriber) {
		try {
			boolean exists = _coreMediator.existsUserPasswordLogin(loginId);
			subscriber.onSuccess(exists);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER / PASSWORD LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSession<U> userPasswordLogin(final LoginID loginId,final Password password) {
		// a) get the user from the [login response]
		UserPasswordLoginResponseOK<U> loginResponse = _coreMediator.userPasswordLogin(loginId,password);
		if (loginResponse == null) return null;		// error while login
		
		// b) compose the login session
		WrappedSession vaadinWrappedSession = UI.getCurrent().getSession()	// vaadin session
															 .getSession();	// unwrap the web session;
		LoginSession<U> outLoginSession = new LoginSession<>(WebSessionID.forId(vaadinWrappedSession.getId()),
															 SecurityProviderID.USER_PASSWORD,loginId,
															 loginResponse.getUser());
		// c) attach the [login session] to the [thread local] & [web session] storage where the [servlet filter]
		//	  expects it
		LoginSessionStore.storeAtThreadLocalStorage(outLoginSession);
		vaadinWrappedSession.setAttribute(LoginSessionStore.LOGIN_CTX_SESSIONATTR,
										  outLoginSession);
		// d) set the locale
		Locale locale = UI.getCurrent().getSession().getLocale();
		if (locale == null) {
			locale = Languages.getLocale(Language.SPANISH);
		} else {
			if (!locale.equals(Languages.getLocale(Language.SPANISH)) && !locale.equals(Languages.getLocale(Language.BASQUE)) ) {
				locale = Languages.getLocale(Language.SPANISH);
			}
		}
		UI.getCurrent().getSession()
					   .setLocale(locale);
		// e) return
		return outLoginSession;
	}
	public void onUserPasswordLoginPasswordResetRequested(final LoginID loginId,final EMail email,
							  							  final UIPresenterSubscriber<Boolean> subscriber) {
		UserPasswordLoginRecoveryResponse recoveryResponse = _coreMediator.userPasswordLoginPasswordResetRequested(loginId,
																												   email);
		
		subscriber.onSuccess(recoveryResponse.hasSucceeded());
	}
	/**
	 * Updates a [user]'s [login] (if the login DOES NOT exists, it creates a NEW one)
	 * @param userOid
	 * @param loginId
	 * @param userPassword
	 * @param subscriber
	 */
	public void createOrUpdateUserPasswordLogin(final UserOID userOid,
												final LoginID loginId,final Password userPassword,
												final UIPresenterSubscriber<LoginOID> subscriber) {
		try {
			// a "normal" user: the [loginId] is the [EMail]
			LoginOID loginOid = _coreMediator.createOrUpdateUserPasswordLogin(userOid,
													  		   				  loginId,userPassword);
			subscriber.onSuccess(loginOid);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	/**
  	 * if the user comes from a batch creation the [loginId] is the [NIF]
	 * and the user MUST change it for the [email] the FIRST time he/she logs-in
	 * ... so the [NIF]-based login is DELETED and a new one is created where
	 *	   the [loginId] is the [EMail]
	 * @param userOid
	 * @param newLoginId
	 * @param password
	 */
	public void deleteAndCreateUserPasswordLogin(final UserOID userOid,
												 final LoginID newLoginId,final Password password,
												 final UIPresenterSubscriber<L> subscriber) {
		try {
			L login = _coreMediator.deleteAndCreateUserPasswordLogin(userOid,
															   		 newLoginId,password);
			subscriber.onSuccess(login);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	/**
	 * Checks if a [user] has an [user/password login]
	 * @param oid
	 * @param subscriber
	 */
	public void checkUserHasUserPasswordLogin(final UserOID oid,
										  	  final UIPresenterSubscriber<Boolean> subscriber) {
		try {
			boolean hasUserPasswdLogin = _coreMediator.checkUserHasPasswordLogin(oid);
			subscriber.onSuccess(hasUserPasswdLogin);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
}
