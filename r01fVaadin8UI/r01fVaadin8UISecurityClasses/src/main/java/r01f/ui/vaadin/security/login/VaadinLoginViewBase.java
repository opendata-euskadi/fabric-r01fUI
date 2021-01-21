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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
import r01ui.ui.vaadin.security.theme.R01UISecurityTheme;

/**
 * Ensure to include the [r01uilogin.scss] styles file:
 * [1] - Go to the UI type and check the @Theme annotation
 * 		 It should be something like:
 *		        @Theme("myAppStyles")	
 *		        public class MyAppVaadinUI
 *		        	 extends UI {
 *		        	...
 *		        }
 * [2] - There must exist this folder: [WebContent]/VAADIN/themes/myAppStyles  <-- the @Theme annotation value
 * 		 The folder's contents should be:
 * 			[WebContent]/VAADIN/themes/myAppStyles
 * 				+ styles.scss
 * 				+ addons.scss
 * 				+ favicon.ico
 * 				+ myAppStyles.scss  	<-- the @Theme annotation value
 * 				+ r01uilogin.scss		<-- this file contains the login view styles
 * [3] - The [styles.scss] file must contain:
 *			@import "addons.scss";
 *			@import "myAppStyles.scss";
 *			@import "r01uilogin.scss";	
 *
 *			.myAppStyles {
 *				@include addons;
 *				@include myAppStyles;	<-- the @Theme annotation value
 *				@include r01uilogin;		
 *			} 
 */

@Slf4j
@Accessors(prefix = "_")
public abstract class VaadinLoginViewBase<U extends User,S extends SecurityContext,
										  P extends VaadinLoginPresenterBase<U,
												  							 ? extends VaadinLoginCOREMediatorBase<U,? extends SecurityAPIBase<U,?,?,?,?,?,?,?>>>>
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

	private final VerticalLayout _buttonsLy;
	private final Button _googleLoginBtn;
	private final Button _xlnetsLoginBtn;
	private final Button _justiziaLoginBtn;
	private final Button _userPassLink;
	
	private Component _usrPassForm;
	private TextField _txtUser;
	private TextField _txtPassword;
	private Button _btnSignIn;
	private Button _btnBack;
	private Button _registerBtn;
	private Button _remindBtn;
		

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
		_buttonsLy = new VerticalLayout();
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
		_txtUser.setCaption(i18n.getMessage("security.login.field.user"));
		_txtPassword.setCaption(i18n.getMessage("security.login.field.pass"));
		_btnSignIn.setCaption(i18n.getMessage("security.login.button.enter"));
		_btnBack.setCaption(i18n.getMessage("security.login.button.back"));
		_registerBtn.setCaption(i18n.getMessage("security.login.register"));
		_remindBtn.setCaption(i18n.getMessage("security.login.remind"));
		
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
	protected Component _buildLoginForm() {
		VerticalLayout root = new VerticalLayout();
		root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		root.setSizeFull();
		root.setMargin(false);

		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setHeightFull();
		loginPanel.setWidth(35, Unit.PERCENTAGE);
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
		loginPanel.setExpandRatio(mainPanel, 6);
		loginPanel.setExpandRatio(footer, 1);
		
		root.addComponent(loginPanel);
		return root;
	}
	
	protected Component _buildTitle() {
		_welcome.setSizeUndefined();
		_welcome.addStyleNames(ValoTheme.LABEL_H1,
		 					  ValoTheme.LABEL_BOLD,
		 					  ValoTheme.LABEL_NO_MARGIN,
		 					  R01UISecurityTheme.LOGIN_TITLE);
		return _welcome;
	}
		
	protected Component _buildMainLayout() {
		VerticalLayout mainLy = new VerticalLayout();
		mainLy.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		mainLy.addStyleName(R01UISecurityTheme.LOGIN_PANEL);
		mainLy.setSizeFull();
		mainLy.setMargin(true);
		
		_title.addStyleNames(ValoTheme.LABEL_H2,
							 ValoTheme.LABEL_BOLD);
		
		_instructions.setContentMode(ContentMode.HTML);
		_instructions.addStyleNames(ValoTheme.LABEL_H3,
									ValoTheme.LABEL_LIGHT);
		
		
		// XLNets login
		Image xlnetsIcon = new Image();
		xlnetsIcon.setIcon(new ThemeResource("img/xlnets-32x32.png"));
		String xlnetsLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											   _securityLoginConfig.getProvider(SecurityProviderID.forId("xlnets")).getLoginUrl(),
											   _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											   _getWebAppUrlPath());
		_xlnetsLoginBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY, 
									  "xlnets-button");
		_xlnetsLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(xlnetsLoginUrl, "_blank"));
		HorizontalLayout xlnetsLy = new HorizontalLayout(xlnetsIcon, _xlnetsLoginBtn);
		
		// Google login
		Image googleIcon = new Image();
		googleIcon.setIcon(new ThemeResource("img/google-32x32.svg"));
		String gLoginUrl = Strings.customized("{}?to={}/{}/",		// http://site/app/google/users/login?to=http://localhost:8080/r01PLATEAWebServiceCatalogUIWar/
											  _securityLoginConfig.getProvider(SecurityProviderID.forId("google")).getLoginUrl(),
											  _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											  _getWebAppUrlPath());		
		_googleLoginBtn.addStyleNames(ValoTheme.BUTTON_DANGER,
									"google-button");
		_googleLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(gLoginUrl, "_blank"));
		HorizontalLayout gLy = new HorizontalLayout(googleIcon, _googleLoginBtn);
		
		// Justizia login
		Image jzIcon = new Image();
		jzIcon.setIcon(new ThemeResource("img/logo_justizia.png"));
		String justiziaLoginUrl = Strings.customized("{}?to={}/{}/",		// future implementation
											   "http://justizia-login-url",
											   _securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase"),
											   _getWebAppUrlPath());
		_justiziaLoginBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY, 
										"justizia-button");
		_justiziaLoginBtn.addListener(clickEvent ->  Page.getCurrent().open(justiziaLoginUrl, "_blank"));
		HorizontalLayout jzLy = new HorizontalLayout(jzIcon, _justiziaLoginBtn);
		jzLy.setVisible(false); //future implementation
		
		_buttonsLy.addComponents(xlnetsLy, 
							   	jzLy, 
							    gLy);
		_buttonsLy.setMargin(new MarginInfo(false,true,false,true)); //right & left margin
		_buttonsLy.setComponentAlignment(xlnetsLy, Alignment.TOP_CENTER);
		_buttonsLy.setComponentAlignment(jzLy, Alignment.MIDDLE_CENTER); 
		_buttonsLy.setComponentAlignment(gLy, Alignment.BOTTOM_CENTER);
		_buttonsLy.setSpacing(true);
		_usrPassForm = _buildUserPwdFields();
		
		// User & password
		_userPassLink.addStyleName(ValoTheme.BUTTON_LINK);
		_userPassLink.addClickListener(clickEvent -> {
														_buttonsLy.setVisible(false);
														_userPassLink.setVisible(false);
														_usrPassForm.setVisible(true);
													 });
		_userPassLink.setSizeFull();
		
		
		// Add components
		mainLy.addComponents(_title, 
							  _instructions,
							  _buttonsLy,
							  _usrPassForm,
							  _userPassLink);
		mainLy.setExpandRatio(_title, 1);
		mainLy.setExpandRatio(_instructions, 1);
		mainLy.setExpandRatio(_buttonsLy, 5);
		mainLy.setExpandRatio(_usrPassForm, 5);
		mainLy.setExpandRatio(_userPassLink, 1);

		return mainLy;
	}

	protected Component _buildLangSelector(final Language... lang) {

		HorizontalLayout langSelector = new HorizontalLayout();
		langSelector.setMargin(false);
		langSelector.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		langSelector.addStyleName(R01UISecurityTheme.LOGIN_LANG_SELECTOR);

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

	private Component _buildUserPwdFields() {
		VerticalLayout usrPassFields = new VerticalLayout();
		usrPassFields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		usrPassFields.setMargin(false);
		usrPassFields.setSpacing(true);
		usrPassFields.addStyleName(R01UISecurityTheme.LOGIN_FIELDS);
		usrPassFields.setVisible(false);

		// User password
		_txtUser = new TextField();
		_txtUser.setIcon(VaadinIcons.USER);
		_txtUser.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		_txtPassword = new PasswordField();
		_txtPassword.setIcon(VaadinIcons.LOCK);
		_txtPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);


		_btnSignIn = new Button();
		_btnSignIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		_btnSignIn.setClickShortcut(KeyCode.ENTER);
		_btnSignIn.addClickListener(clickEvent -> {
													// Call the login
													_userPasswordLogin(LoginID.forId(_txtUser.getValue()),
																	   Password.forId(_txtPassword.getValue()));
												 });
		_btnBack = new Button();
		_btnBack.addClickListener(clickEvent -> {
													_buttonsLy.setVisible(true);
													_userPassLink.setVisible(true);
													_usrPassForm.setVisible(false);
											   });
		HorizontalLayout buttonsLy = new HorizontalLayout(_btnBack, 
														  _btnSignIn);
		buttonsLy.setMargin(false);

		_registerBtn = new Button();
		_registerBtn.addStyleName(ValoTheme.BUTTON_LINK);
		_remindBtn = new Button();
		_remindBtn.addStyleName(ValoTheme.BUTTON_LINK);

		// Add components
		usrPassFields.addComponents(_txtUser,
									_txtPassword,
									buttonsLy,
									_registerBtn,
									_remindBtn);
		_btnSignIn.focus();

		return usrPassFields;
	}

	private void _userPasswordLogin(final LoginID login,
			final Password password) {
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
