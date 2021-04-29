package r01f.ui.vaadin.security.login;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginRequestBuilder;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseOK;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.user.User;
import r01f.model.security.user.UserStatus;
import r01f.securitycontext.SecurityIDS.LoginAndPassword;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.EMail;
import r01f.ui.coremediator.UICOREMediator;

@Slf4j
@Accessors(prefix="_")
public abstract class VaadinLoginCOREMediatorBase<U extends User,
												  L extends UserPasswordLogin,
												  A extends SecurityAPIBase<U,?,?,?,?>>
		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<U> _userType;
	@Getter protected final A _api;
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
	 * @param api
	 */
	protected VaadinLoginCOREMediatorBase(final Class<U> userType,
										  final A api) {
		_userType = userType;
		_api = api;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER / PASSWORD LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean checkUserHasPasswordLogin(final UserOID oid) {
		L usrPwd = null;
		try {
			usrPwd = _api.<L>getUserPasswordLoginApi()
						 .getForCrud()
						 .loadByOrNull(oid);
			if (usrPwd != null) return true;
		} catch (final PersistenceException persex ) {
			persex.printStackTrace();
		}
		return false;
	}
	/**
	 * Checks if a [user/password login] exists for the given [loginId]
	 * @param loginId
	 * @return
	 */
	public boolean existsUserPasswordLogin(final LoginID loginId) {
		log.info("Check [user/password login] existence for loginId=",loginId);
		return _api.getUserPasswordLoginApi()
				   .getForCrud()	
				   .loadByOrNull(loginId) != null;
	}
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
		log.info("User & password login {}: {} ::::::::::::::::::::::::::::::::",
				 loginId,
				 loginOK ? "SUCCESSFULL" : "FAILED");

		// [2] - if NOT loginOK, return null
		if (!loginOK) return null;

		// [3] - Compose the [login session]
		UserPasswordLoginResponseOK<U> loginResponse = response.asResponseOKFor(_userType);
		return loginResponse;
	}
	public UserPasswordLoginRecoveryResponse userPasswordLoginPasswordResetRequested(final LoginID loginId,final EMail email) {
		UserPasswordLoginRecoveryResponse recoveryResponse = _api.getUserPasswordLoginApi()
																 .getForLogin()
																 .recoverPassword(loginId,email);
		return recoveryResponse;
	}
	public LoginOID createOrUpdateUserPasswordLogin(final UserOID userOid,
													final LoginID loginId,final Password password) {
		LoginOID outLoginOid = null;
		
		// [1] - Load the login by userOid
		L usrPwdLogin = _api.<L>getUserPasswordLoginApi()
	   						.getForCrud()
	   						.loadByOrNull(userOid);
		// [2] - If the login exists update the password
		if (usrPwdLogin != null) {
			usrPwdLogin = _api.<L>getUserPasswordLoginApi()
						   	  .getForCrud()
						   	  .updatePassword(loginId,password);
			outLoginOid = usrPwdLogin.getOid();
		}
		// [3] - If the login DOES NOT exist, create a new one (a confirmation email will be sent)
		else {
			// a) - load the [user]
			U user = _api.getUsersApi()
						 .getForCrud()
						 .load(userOid);
			
			// b) create the [user/password login]
			UserPasswordLoginCreateRequest<U> loginCreateReq = new UserPasswordLoginCreateRequest<>(user,new LoginAndPassword(loginId,password));
			UserPasswordLoginCreateResponse usrPwsLoginCreate = _api.getUserPasswordLoginApi()
															   		.getForLogin()
															   		.createLogin(loginCreateReq);
			outLoginOid = usrPwsLoginCreate.hasSucceeded() ? usrPwsLoginCreate.asResponseOK()
																			  .getLoginOid()
														   : null;
		}
		return outLoginOid;
	}
	public L updateUserPasswordLogin(final UserOID userOid,final Password password) {
		L outUsrPwd = null;
		
		// [1] - Load the login
		L usrPwdLogin = _api.<L>getUserPasswordLoginApi()
	   						.getForCrud()
	   						.loadByOrNull(userOid);
		// [2] - If the login exists update the password
		if (usrPwdLogin != null) {
			outUsrPwd = _api.<L>getUserPasswordLoginApi()
						   	.getForCrud()
						   	.updatePassword(usrPwdLogin.getLoginId(),password);
		}
		return outUsrPwd;
	}
	/**
	 * Creates a NEW [user/password login]
	 * @param user
	 * @param loginId
	 * @param password
	 * @param initialUserStatusWhenUserAlreadyExists
	 * @param initialUserStatusWhenUserIsNew
	 * @return
	 */
	public UserPasswordLoginCreateResponse createUserPasswordLogin(final U user,
												 				   final LoginID loginId,final Password password,
												 				   final UserStatus initialUserStatusWhenUserAlreadyExists,final UserStatus initialUserStatusWhenUserIsNew) {
		log.info("Register [user] with loginId={}",
				 loginId);
		UserPasswordLoginCreateRequest<U> req = new UserPasswordLoginCreateRequest<>(user,new LoginAndPassword(loginId,password),
																	   				 initialUserStatusWhenUserAlreadyExists,initialUserStatusWhenUserIsNew);
		UserPasswordLoginCreateResponse res = _api.getUserPasswordLoginApi()
												  .getForLogin()
												  .createLogin(req);
		return res;
	}
	public L deleteAndCreateUserPasswordLogin(final UserOID userOid,
											  final LoginID loginId,final Password password) {
		// [0] - Load the [user login]
		L usrPwdLogin = _api.<L>getUserPasswordLoginApi()
		     			    .getForCrud()
		     				.loadByOrNull(userOid);
		// [1] - It the login exists, remove it!! 
		if (usrPwdLogin != null) {
			_api.getUserPasswordLoginApi()
			    .getForCrud()
			    .delete(usrPwdLogin.getOid());
		}
		// [2] - Create a new login
		usrPwdLogin = _createUserPasswordLogin(userOid,
											   loginId,password);
		usrPwdLogin = _api.<L>getUserPasswordLoginApi()
						  .getForCrud()
						  .create(usrPwdLogin);
		return usrPwdLogin;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract L _createUserPasswordLogin(final UserOID userOid,
												  final LoginID loginId,final Password password);	

}
