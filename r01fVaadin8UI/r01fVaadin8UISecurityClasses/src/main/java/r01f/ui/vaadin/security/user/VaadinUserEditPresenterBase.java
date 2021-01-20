package r01f.ui.vaadin.security.user;

import java.util.Optional;

import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.login.google.GoogleLogin;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;

public abstract class VaadinUserEditPresenterBase<U extends User,L extends UserPasswordLogin,GL extends GoogleLogin,XL extends XLNetsLogin,
												  VU extends VaadinViewUser<U>,VL extends VaadinViewUserPasswordLogin<L>,VGL extends VaadinViewGoogleLogin<GL>,VXL extends VaadinViewXLNetsLogin<XL>,
												  C extends VaadinUserEditCOREMediatorBase<U,L,GL,XL,
												  										   ? extends SecurityAPIBase<U,L,GL,XL,
												  												   					 ?,
												  												   					 ?,?,
												  												   					 ?>>> 
  		   implements UIPresenter {
	private static final long serialVersionUID = 8650730712618077501L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final C _coreMediator;
	private final FactoryFrom<U,VU> _viewUserFactory;
	private final FactoryFrom<L,VL> _viewUserPasswordLoginFactory;
	private final FactoryFrom<GL,VGL> _viewGoogleLoginFactory;
	private final FactoryFrom<XL,VXL> _viewXLNetsLoginFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinUserEditPresenterBase(final C coreMediator,
										  final FactoryFrom<U,VU> viewUserFactory,
										  final FactoryFrom<L,VL> viewUserPasswordLoginFactory,
										  final FactoryFrom<GL,VGL> viewGoogleLoginFactory,
										  final FactoryFrom<XL,VXL> viewXLNetsLoginFactory) {
		_coreMediator = coreMediator;
		_viewUserFactory = viewUserFactory;
		_viewUserPasswordLoginFactory = viewUserPasswordLoginFactory;
		_viewGoogleLoginFactory = viewGoogleLoginFactory;
		_viewXLNetsLoginFactory = viewXLNetsLoginFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER
/////////////////////////////////////////////////////////////////////////////////////////
	public void onUserLoadRequested(final UserOID userOid,
									final UIPresenterSubscriber<VU> subscriber) {
		try {
			U user = _coreMediator.loadUser(userOid);	// throws an exception if the user does NOT exists
			VU viewUser = _viewUserFactory.from(user);
			
			subscriber.onSuccess(viewUser);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onUserUpdateRequested(final VU viewUser,
									  final UIPresenterSubscriber<VU> subscriber) {
		try {
			U user = _coreMediator.updateUser(viewUser.getWrappedModelObject());	
			VU updatedViewUser = _viewUserFactory.from(user);
			subscriber.onSuccess(updatedViewUser);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER-PASSWORD LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public void onUserPasswordLoginLoadRequested(final UserOID userOid,
												 final UIPresenterSubscriber<Optional<VL>> subscriber) {
		try {
			L login = _coreMediator.loadUserPasswordLogin(userOid);	// throws an exception if the user does NOT exists
			VL viewLogin = login != null ? _viewUserPasswordLoginFactory.from(login)
										 : null;
			subscriber.onSuccess(Optional.ofNullable(viewLogin));
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onUserPasswordLoginExistenceCheckRequested(final LoginID loginId,
													 	   final UIPresenterSubscriber<Boolean> subscriber) {
		try {
			boolean exists = _coreMediator.userPasswordLoginExists(loginId);
			subscriber.onSuccess(exists);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onUserPasswordLoginCreateRequested(final VL viewUserPasswordLogin,
												   final UIPresenterSubscriber<VL> subscriber) {
		try {
			L login = _coreMediator.createUserPasswordLogin(viewUserPasswordLogin.getWrappedModelObject());	
			VL viewLogin = _viewUserPasswordLoginFactory.from(login);
			subscriber.onSuccess(viewLogin);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onUserPasswordLoginIdChangeRequested(final UserOID userOid,final LoginID newLoginId,
												     final UIPresenterSubscriber<VL> subscriber) {
		try {
			L login = _coreMediator.changeUserPasswordLoginId(userOid,newLoginId);	// throws an exception if the user does NOT exists
			VL viewLogin = _viewUserPasswordLoginFactory.from(login);
			subscriber.onSuccess(viewLogin);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	public void onUserPasswordLoginPasswordChangeRequested(final LoginID loginId,final Password oldPassword,final Password newPassword,
												   		   final UIPresenterSubscriber<VL> subscriber) {
		try {
			L login = _coreMediator.changeUserPasswordLoginPassword(loginId,oldPassword,newPassword);	// throws an exception if the user does NOT exists
			VL viewLogin = _viewUserPasswordLoginFactory.from(login);
			subscriber.onSuccess(viewLogin);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GOOGLE LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public void onGoogleLoginLoadRequested(final UserOID userOid,
										   final UIPresenterSubscriber<Optional<VGL>> subscriber) {
		try {
			GL login = _coreMediator.loadGoogleLogin(userOid);	// throws an exception if the user does NOT exists
			VGL viewLogin = login != null ? _viewGoogleLoginFactory.from(login)
										  : null;
			subscriber.onSuccess(Optional.ofNullable(viewLogin));
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
}
