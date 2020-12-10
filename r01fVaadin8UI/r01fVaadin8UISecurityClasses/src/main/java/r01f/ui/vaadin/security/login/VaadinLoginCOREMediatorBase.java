package r01f.ui.vaadin.security.login;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginRequestBuilder;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseOK;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.ui.coremediator.UICOREMediator;

@Slf4j
public abstract class VaadinLoginCOREMediatorBase<U extends User,
												  A extends SecurityAPIBase<U,?,?,?,?,?>>
		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<U> _userType;
	private final A _api;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
//	BEWARE!!	The apis here uses the MASTER's security context since
//				there's NO user's security context until the user is logged in
//	see:
//		- R01MCommonClientBootstrapGuiceModule > this is where the user's security context provider
//													and master's security context provider are binded
//		- R01MClientBootstrapGuiceModule > this is where user's client apis
//											  and master's client apis are binded
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor
	 * BEWARE!! the constructor at the subtype extending this class MUST be declared as
	 * <pre class='brush:java'>
	 * 		public class MyCOREMediator
	 * 		     extends R01UILoginCOREMediatorBase {
	 * 			@Inject
	 * 			public MyCoreMediator(@ClientAPIForSystemUser R01MClientAPIForSecurity api) {
	 * 				super(api)
	 * 			}
	 * 		}
	 * </pre>
	 *
	 * @param api
	 */
	protected VaadinLoginCOREMediatorBase(final Class<U> userType,
										  final A api) {
		_userType = userType;
		_api = api;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Logs the user in and returns the security context user data if login was successful
	 * or null if the user could NOT log in
	 * @param userCode
	 * @param password
	 * @return
	 */
	public UserPasswordLoginResponseOK<U> userPasswordLogin(final LoginID loginId,final Password password) {
		// [1] - use the [user password] api to check the user
		UserPasswordLoginRequest req = UserPasswordLoginRequestBuilder.using(loginId)
																	  .with(password)
																	  .build();
		UserPasswordLoginResponse response = _api.getUserPasswordLoginApi()
											   	 .getForLogin()
											   	 .login(req);
		boolean loginOK = response.hasSucceeded();
		log.warn("User & password login {}: {} ::::::::::::::::::::::::::::::::",
				 loginId,
				 loginOK ? "SUCCESSFULL" : "FAILED");

		// [2] - if NOT loginOK, return null
		if (!loginOK) return null;

		// [3] - Compose the [login session]
		UserPasswordLoginResponseOK<U> loginResponse = response.asResponseOKFor(_userType);
		return loginResponse;
	}
}
