package r01f.ui.vaadin.security.login;

import java.util.Locale;

import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

import lombok.experimental.Accessors;
import r01f.client.api.security.SecurityAPIBase;
import r01f.guids.CommonOIDs.WebSessionID;
import r01f.locale.Language;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseOK;
import r01f.model.security.user.User;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.ui.presenter.UIPresenter;
import r01f.util.types.locale.Languages;

@Accessors(prefix="_")
public abstract class VaadinLoginPresenterBase<U extends User,
											   C extends VaadinLoginCOREMediatorBase<U,? extends SecurityAPIBase<U,?,?,?,?,?>>>
		   implements UIPresenter {

	private static final long serialVersionUID = -4290934932756711025L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final C _coreMediator;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginPresenterBase(final C authCoreMediator) {
		_coreMediator = authCoreMediator;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSession<U> userPasswordLogin(final LoginID loginId,final Password password) {
		// a) get the user from the [login response]
		UserPasswordLoginResponseOK<U> loginResponse = _coreMediator.userPasswordLogin(loginId,password);
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
}
