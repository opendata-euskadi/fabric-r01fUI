package r01f.ui.vaadin.security.login;

import javax.inject.Provider;

import com.google.common.collect.FluentIterable;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.locale.I18NKey;
import r01f.locale.Language;
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
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViewID;
import r01f.util.types.Strings;
import r01f.util.types.locale.Languages;


@Slf4j
@Accessors(prefix = "_")
public abstract class VaadinLoginViewBase<U extends User,S extends SecurityContext,
										  P extends VaadinLoginPresenterBase<U,
												  							 ? extends VaadinLoginCOREMediatorBase<U,? extends SecurityAPIBase<U,?,?,?,?,?>>>>
     		  extends Composite
     	   implements View,
     	   			  VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -3842197138419966575L;
/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	protected final transient P _presenter;

	protected final transient SecurityLoginFilterConfig _securityLoginConfig;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	 UI
/////////////////////////////////////////////////////////////////////////////////////////
	private final Label _welcome;
	private final Label _title;
	private final Label _instructions;
	private final Button _googleLoginBtn;
	private final Button _xlnetsLoginBtn;
	private final Button _justiziaLoginBtn;
	private final Button _userPassLink;
		

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
		
		_welcome = new Label();
		_title = new Label();
		_instructions = new Label();
		_googleLoginBtn = new Button();
		_xlnetsLoginBtn = new Button();
		_justiziaLoginBtn = new Button();
		_userPassLink = new Button();
		
		Component loginForm = _buildLoginForm();
		updateI18NMessages(_i18n);
		
		this.setCompositionRoot(loginForm);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_welcome.setValue(i18n.getMessage("security.login.header").toUpperCase());
		_title.setValue(i18n.getMessage(_getAppTitleI18NKey()).toUpperCase());
		_instructions.setValue(Strings.customized("{} {}", 
						      				  VaadinIcons.KEY_O.getHtml(),
						      				  i18n.getMessage("security.login.title")));
		_googleLoginBtn.setCaption(i18n.getMessage("security.login.method.google"));
		_xlnetsLoginBtn.setCaption(i18n.getMessage("security.login.method.xlnets"));
		_justiziaLoginBtn.setCaption(i18n.getMessage("security.login.method.justizia"));
		_userPassLink.setCaption(i18n.getMessage("security.login.method.userPass"));
	}	

/////////////////////////////////////////////////////////////////////////////////////////
// ENTRY POINT
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
	private Component _buildLoginForm() {
		VerticalLayout root = new VerticalLayout();
		root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		root.setSizeFull();
		root.setMargin(false);

		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setHeightFull();
		loginPanel.setWidth(60, Unit.PERCENTAGE);
		loginPanel.setMargin(false);
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		Component header = _buildTitle();
		Component mainPanel = _buildMainLayout();
		Component footer = _buildLangSelector(Language.SPANISH,
											  Language.BASQUE);
		loginPanel.addComponents(header, 
								 mainPanel,
								 footer);
		loginPanel.setComponentAlignment(header, Alignment.BOTTOM_CENTER);
		loginPanel.setComponentAlignment(footer, Alignment.TOP_RIGHT);
		loginPanel.setExpandRatio(header, 1);
		loginPanel.setExpandRatio(mainPanel, 4);
		loginPanel.setExpandRatio(footer, 1);
		
		root.addComponent(loginPanel);
		return root;
	}
	
	private Component _buildTitle() {
		_welcome.setSizeUndefined();
		_welcome.addStyleNames(ValoTheme.LABEL_H1,
		 					  ValoTheme.LABEL_BOLD,
		 					  ValoTheme.LABEL_NO_MARGIN,
		 					  "login-title");
		return _welcome;
	}
		
	private Component _buildMainLayout() {
		VerticalLayout buttons = new VerticalLayout();
		buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		buttons.addStyleName("login-panel");
		buttons.setSizeFull();
		buttons.setMargin(true);
		
		_title.addStyleNames(ValoTheme.LABEL_H2,
							 ValoTheme.LABEL_BOLD);
		
		_instructions.setContentMode(ContentMode.HTML);
		_instructions.addStyleNames(ValoTheme.LABEL_H3,
									ValoTheme.LABEL_LIGHT);
		
		
		// Google login
		String gLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											  _securityLoginConfig.getProvider(SecurityProviderID.forId("google")).getLoginUrl(),
											  _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											  _getWebAppUrlPath());		
		_googleLoginBtn.setIcon(new ThemeResource("img/google-32x32.svg"));
		_googleLoginBtn.addStyleNames(ValoTheme.BUTTON_DANGER,
									"google-button");
		_googleLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(gLoginUrl, "_blank"));
		
		// XLNets login
		String xlnetsLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											   _securityLoginConfig.getProvider(SecurityProviderID.forId("xlnets")).getLoginUrl(),
											   _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											   _getWebAppUrlPath());
		_xlnetsLoginBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY, 
									  "xlnets-button");
		_xlnetsLoginBtn.setIcon( new ThemeResource("img/xlnets-32x32.png")); //TODO generic url for themes
		_xlnetsLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(xlnetsLoginUrl, "_blank"));
		
		// Justizia login
		String justiziaLoginUrl = Strings.customized("{}?to={}/{}/",		// future implementation
											   "http://justizia-login-url",
											   _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											   _getWebAppUrlPath());
		_justiziaLoginBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY, 
										"justizia-button");
		_justiziaLoginBtn.setIcon( new ThemeResource("img/justizia-32x32.ico"));
		_xlnetsLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(justiziaLoginUrl, "_blank"));
		
		// User & password
		_userPassLink.addStyleName(ValoTheme.BUTTON_LINK);
		_userPassLink.addClickListener(clickEvent -> {
													Window dialog = new VaadinLoginUserPasswordWindow();
													UI.getCurrent().addWindow(dialog);
		});
		_userPassLink.setSizeFull();
		
		// Add components
		buttons.addComponents(_title, 
							  _instructions,
							  _googleLoginBtn,
							  _xlnetsLoginBtn, 
							  _justiziaLoginBtn, //future implementation
							  _userPassLink);

		return buttons;
	}

	private Component _buildLangSelector(final Language... lang) {
			
		HorizontalLayout langSelector = new HorizontalLayout();
		langSelector.setMargin(false);
		langSelector.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		langSelector.addStyleName("login-lang-selector");
		
		FluentIterable.from(lang).forEach(l -> {
											Button langButton = new Button(Languages.languageLowerCase(l));
											langButton.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
																	ValoTheme.LABEL_BOLD);
											langButton.addClickListener(clickEvent -> {
																							UI.getCurrent().setLocale(Languages.getLocale(l));
																							updateI18NMessages(_i18n);
																					  });
											langSelector.addComponent(langButton);
										  });
		return langSelector;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////		
	private class VaadinLoginUserPasswordWindow
	 	  extends Window
	   implements VaadinViewI18NMessagesCanBeUpdated{

		private static final long serialVersionUID = -3134021582323591011L;
		
		protected VaadinLoginUserPasswordWindow() {
			this.center();
			this.setModal(true);
			this.setWidth(35, Unit.PERCENTAGE);
			this.setResizable(false);
			Component loginForm = _buildFields();
			this.setContent(loginForm);
		
		}
		
		private Component _buildFields() {
			VerticalLayout mainLayout = new VerticalLayout();
			//mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
			mainLayout.setMargin(false);
			mainLayout.addStyleName("fields");
			
			
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
			
			Button registerBtn = new Button(_i18n.getMessage("security.login.register"));
			registerBtn.addStyleName(ValoTheme.BUTTON_LINK);
			//registerBtn.setSizeFull();
		
			Button remindBtn = new Button(_i18n.getMessage("security.login.remind"));
			remindBtn.addStyleName(ValoTheme.BUTTON_LINK);
			//remindBtn.setSizeFull();
			
			
			// Add components
			mainLayout.addComponents(txtUser,
								 	 txtPassword,
								 	 btnSignIn,
								 	 registerBtn,
								 	 remindBtn);
			btnSignIn.focus();
	
			return mainLayout;
		}
		
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
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract I18NKey _getAppTitleI18NKey();
	protected abstract VaadinViewID _getMainViewId();
	protected abstract UrlPath _getWebAppUrlPath();
}
