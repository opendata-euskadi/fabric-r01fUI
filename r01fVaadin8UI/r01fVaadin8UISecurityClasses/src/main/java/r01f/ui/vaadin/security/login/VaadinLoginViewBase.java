package r01f.ui.vaadin.security.login;

import javax.inject.Provider;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.locale.I18NKey;
import r01f.model.security.user.User;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.security.login.filter.SecurityLoginFilterConfig;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityContextStoreAtThreadLocalStorage;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewID;
import r01f.util.types.Strings;

@Slf4j
@Accessors(prefix = "_")
public abstract class VaadinLoginViewBase<U extends User,S extends SecurityContext,
										  P extends VaadinLoginPresenterBase<U,
												  							 ? extends VaadinLoginCOREMediatorBase<U,? extends SecurityAPIBase<U,?,?,?,?,?>>>>
     		  extends Composite
     	   implements View {

	private static final long serialVersionUID = -3842197138419966575L;
/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	protected final transient P _presenter;

	protected final transient SecurityLoginFilterConfig _securityLoginConfig;

///////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginViewBase(final Provider<SecurityContext> securityContextProvider,
							   final UII18NService i18n,
							   final P presenter,
							   final SecurityLoginFilterConfig securityLoginConfig) {
		_i18n = i18n;

		_presenter = presenter;
		_securityLoginConfig = securityLoginConfig;

		Component loginForm = _buildLoginForm();
		this.setCompositionRoot(loginForm);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	 UI
/////////////////////////////////////////////////////////////////////////////////////////
	private Component _buildLoginForm() {
		VerticalLayout root = new VerticalLayout();
		root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		root.setSizeFull();

		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");
		loginPanel.addComponent(_buildLabels());
		loginPanel.addComponent(_buildFields());
		root.addComponent(loginPanel);

		return root;
	}
	private Component _buildFields() {
		VerticalLayout fields = new VerticalLayout();
		fields.setMargin(new MarginInfo(false,true,true,true));
		fields.addStyleName("fields");

		// User password
		TextField txtUser = new TextField(_i18n.getMessage("security.login.field.user"));
		txtUser.setIcon(VaadinIcons.USER);
		txtUser.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		PasswordField txtPassword = new PasswordField(_i18n.getMessage("security.login.field.pass"));
		txtPassword.setIcon(VaadinIcons.LOCK);
		txtPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		Button btnSignIn = new Button(_i18n.getMessage("security.login.button.enter"));
		btnSignIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSignIn.setClickShortcut(KeyCode.ENTER);
		btnSignIn.addClickListener(clickEvent -> {
										// Call the login
										_userPasswordLogin(LoginID.forId(txtUser.getValue()),
														   Password.forId(txtPassword.getValue()));
								   });
		// Google login
		String gLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											  _securityLoginConfig.getProvider(SecurityProviderID.forId("google")).getLoginUrl(),
											  _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											  _getWebAppUrlPath());
		Link lnkGoogleLogin = new Link("Google Login",
							 		   new ExternalResource(gLoginUrl));

		// XLNets login
		String xlLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											   _securityLoginConfig.getProvider(SecurityProviderID.forId("xlnets")).getLoginUrl(),
											   _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											   _getWebAppUrlPath());
		Link lnkXLNetsLogin = new Link("XLNets Login",
							 		    new ExternalResource(xlLoginUrl));

		// Add components
		fields.addComponents(txtUser,
							 txtPassword,
							 btnSignIn,
							 new HorizontalLayout(lnkGoogleLogin,lnkXLNetsLogin));
		fields.setComponentAlignment(btnSignIn,
									 Alignment.BOTTOM_LEFT);
		btnSignIn.focus();

		return fields;
	}
	private Component _buildLabels() {
		VerticalLayout labels = new VerticalLayout();
		labels.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		labels.setSpacing(false);
		labels.setMargin(false);

		Label logoLabel = new Label();
		logoLabel.setContentMode(ContentMode.HTML);
		logoLabel.setValue(VaadinIcons.KEY_O.getHtml() + " " + _i18n.getMessage("security.login.title"));
		logoLabel.addStyleNames(ValoTheme.LABEL_H1,
		 					  	ValoTheme.LABEL_COLORED);
		labels.addComponent(logoLabel);

		Label welcome = new Label(_i18n.getMessage("security.login.header"));
		welcome.setSizeUndefined();
		welcome.addStyleNames(ValoTheme.LABEL_H3,
		 					  ValoTheme.LABEL_BOLD,
		 					  ValoTheme.LABEL_NO_MARGIN);
		labels.addComponent(welcome);

		Label title = new Label(_i18n.getMessage(_getAppTitleI18NKey()));
		title.setSizeUndefined();
		title.addStyleNames(ValoTheme.LABEL_H4,
						    ValoTheme.LABEL_LIGHT);
		labels.addComponent(title);
		return labels;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public void enter(final ViewChangeEvent viewChangeEvent) {
		S securityContext = SecurityContextStoreAtThreadLocalStorage.get();

		if (securityContext == null) {
			WrappedSession webSession = VaadinService.getCurrentRequest()
	  				 							 	 .getWrappedSession();
			securityContext = (S)webSession.getAttribute(LoginSessionStore.LOGIN_CTX_SESSIONATTR);
		}
		if (securityContext != null)  {
			log.warn("[login] > there already exist an user session (loginId = {})",securityContext.getLoginId());
			UI.getCurrent().getNavigator().navigateTo(_getMainViewId().asString());
		}
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private void _userPasswordLogin(final LoginID login,final Password password) {
		// use the presenter to do the login
		LoginSession<U> loginSession = _presenter.userPasswordLogin(login,
										 			    			password);
		if (loginSession != null) {
			// goto the main page
			Url mainPageUrl = Url.from(Strings.customized("/{}#!{}",
														  _getWebAppUrlPath(),_getMainViewId()));
			log.warn("[login] > user {} has logged-in > redir to MAIN page: {}.....................................",
					 loginSession.getLoginId(),
					 mainPageUrl);
			Page.getCurrent().setLocation(mainPageUrl.asString());
		} else {
			Notification.show(_i18n.getMessage("security.login.invalid.user.or.passwd"),
							  Notification.Type.ERROR_MESSAGE);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract I18NKey _getAppTitleI18NKey();
	protected abstract VaadinViewID _getMainViewId();
	protected abstract UrlPath _getWebAppUrlPath();
}
