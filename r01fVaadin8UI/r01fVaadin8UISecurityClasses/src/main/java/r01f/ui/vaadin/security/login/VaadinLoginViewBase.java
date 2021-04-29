package r01f.ui.vaadin.security.login;

import javax.inject.Provider;

import com.google.common.collect.FluentIterable;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.locale.I18NKey;
import r01f.locale.Language;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.security.login.filter.SecurityLoginFilterConfig;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityContextStoreAtThreadLocalStorage;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViewID;
import r01f.util.types.Strings;
import r01f.util.types.locale.Languages;
import r01ui.ui.vaadin.security.theme.VaadinSecurityTheme;

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
public abstract class VaadinLoginViewBase<U extends User,
										  L extends UserPasswordLogin,
										  S extends SecurityContext,
										  P extends VaadinLoginPresenterBase<U,L,
												  							 ? extends VaadinLoginCOREMediatorBase<U,L,
												  									 							   ? extends SecurityAPIBase<U,?,?,?,?>>>>
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
	// Header
	private final Label _lblWelcome;
	private final Label _lblTitle;

	// main login methods
	private final Label _lblCorporateLoginsTip;
	private final VerticalLayout _lyMainLoginMethods;
	private final VaadinLoginMethodButton _btnXLNets;
	private final Button _btnShowOtherLoginMethods;
	
	// Other login methods
	private final Label _lblOtherLoginsTip;
	private final VerticalLayout _lyOtherLoginMethods;
	private final VaadinLoginMethodButton _btnGoogle;
	private final VaadinLoginMethodButton _btnJustizia;
	private final VaadinUserPasswordLoginForm _frmUsrPasswd;
	private final Button _btnShowUsrPasswdForm;
	private final Button _btnHideOtherLoginMethods;
		
///////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginViewBase(final Provider<SecurityContext> securityContextProvider,
							   final UII18NService i18n,
							   final P presenter,
							   final SecurityLoginFilterConfig securityLoginConfig) {
		////////// services
		_i18n = i18n;

		_presenter = presenter;
		_securityLoginConfig = securityLoginConfig;
		
		////////// UI
		// title
		_lblTitle = new Label();
		_lblTitle.addStyleNames(ValoTheme.LABEL_H2,
							 	ValoTheme.LABEL_BOLD);
		// Login methods
		// A) XLNets login
		_lblCorporateLoginsTip = new Label();
		_lblCorporateLoginsTip.setContentMode(ContentMode.HTML);
		_lblCorporateLoginsTip.addStyleNames(ValoTheme.LABEL_H3,
											 ValoTheme.LABEL_LIGHT,
											 VaadinValoTheme.LABEL_WORD_WRAP);
		_btnXLNets = _buildXLNetsButton();
		_btnShowOtherLoginMethods = new Button();
		_btnShowOtherLoginMethods.addStyleName(ValoTheme.BUTTON_LINK);
		
		_lyMainLoginMethods = new VerticalLayout(_lblCorporateLoginsTip,
												 _btnXLNets,
												 _btnShowOtherLoginMethods);
		_lyMainLoginMethods.setComponentAlignment(_lblCorporateLoginsTip,Alignment.MIDDLE_CENTER);
		_lyMainLoginMethods.setComponentAlignment(_btnXLNets,Alignment.MIDDLE_CENTER);
		_lyMainLoginMethods.setComponentAlignment(_btnShowOtherLoginMethods,Alignment.MIDDLE_CENTER);
		_lyMainLoginMethods.setMargin(new MarginInfo(false,true,false,true)); // right & left margin
		_lyMainLoginMethods.setSpacing(true);
		
		// B) Other login methods
		_lblOtherLoginsTip = new Label();
		_lblOtherLoginsTip.setContentMode(ContentMode.HTML);
		_lblOtherLoginsTip.addStyleNames(ValoTheme.LABEL_H3,
										 ValoTheme.LABEL_LIGHT,
										 VaadinValoTheme.LABEL_WORD_WRAP);
		_btnGoogle = _buildGoogleButton();
		_btnJustizia = _buildJustiziaButton(); 
		_btnShowUsrPasswdForm = new Button();
		_btnHideOtherLoginMethods = new Button(VaadinIcons.ARROW_BACKWARD);
		
		_btnShowUsrPasswdForm.addStyleName(ValoTheme.BUTTON_LINK);
		_btnHideOtherLoginMethods.addStyleName(ValoTheme.BUTTON_LINK);
		
		_lyOtherLoginMethods = new VerticalLayout(_lblOtherLoginsTip,
												  _btnGoogle,
												  _btnJustizia,
												  _btnShowUsrPasswdForm,
												  _btnHideOtherLoginMethods);
		
		_lyOtherLoginMethods.setMargin(new MarginInfo(false,true,false,true)); // right & left margin
		_lyOtherLoginMethods.setSpacing(true);
		_lyOtherLoginMethods.setComponentAlignment(_lblOtherLoginsTip,Alignment.TOP_CENTER);
		_lyOtherLoginMethods.setComponentAlignment(_btnGoogle,Alignment.TOP_CENTER); 
		_lyOtherLoginMethods.setComponentAlignment(_btnJustizia,Alignment.TOP_CENTER);
		_lyOtherLoginMethods.setComponentAlignment(_btnShowUsrPasswdForm,Alignment.TOP_CENTER);
		_lyOtherLoginMethods.setComponentAlignment(_btnHideOtherLoginMethods,Alignment.TOP_CENTER);
		
		// C) user / password login
		_frmUsrPasswd = new VaadinUserPasswordLoginForm(i18n);
		
		////////// Layout
		// a layout that contains the layers that are being shown / hidden
		VerticalLayout ly = new VerticalLayout(_lblTitle,
						 					   // login method layers
						 					   _lyMainLoginMethods,
						 					   _lyOtherLoginMethods,
						 					   _frmUsrPasswd);
		ly.setMargin(false);
		ly.setSpacing(true);
		Responsive.makeResponsive(ly);
		ly.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		ly.setComponentAlignment(_lblTitle,Alignment.TOP_CENTER);
		ly.setComponentAlignment(_lyMainLoginMethods,Alignment.TOP_CENTER);
		ly.setComponentAlignment(_lyOtherLoginMethods,Alignment.TOP_CENTER);
		ly.setComponentAlignment(_frmUsrPasswd,Alignment.TOP_CENTER);

		
		Panel panel = new Panel();
		panel.addStyleName(VaadinSecurityTheme.LOGIN_PANEL);
		panel.setContent(ly);
		
		// a blue background that contains the pannel and the lang selector
		Component langSel = _buildLangSelector(Language.SPANISH,
											   Language.BASQUE);
		// welcome
		_lblWelcome = new Label();
		_lblWelcome.addStyleNames(ValoTheme.LABEL_H1,
		 					   	  ValoTheme.LABEL_BOLD,
		 					   	  ValoTheme.LABEL_NO_MARGIN,
		 					   	  VaadinSecurityTheme.LOGIN_TITLE);
		VerticalLayout lyRoot = new VerticalLayout(_lblWelcome, 
						 				 		   panel,
						 				 		   langSel);
		lyRoot.addStyleName(VaadinSecurityTheme.LOGIN_ROOT);
		Responsive.makeResponsive(lyRoot);
		lyRoot.setWidthFull();
		lyRoot.setHeightFull();
		lyRoot.setComponentAlignment(_lblWelcome,Alignment.BOTTOM_CENTER);
		lyRoot.setComponentAlignment(panel,Alignment.TOP_CENTER);
		lyRoot.setComponentAlignment(langSel,Alignment.TOP_CENTER);
		lyRoot.setExpandRatio(_lblWelcome,1);
		lyRoot.setExpandRatio(panel,5);
		lyRoot.setExpandRatio(langSel,1);
		
		this.setCompositionRoot(lyRoot);
		
		////////// I18N
		this.updateI18NMessages(_i18n);
		
		////////// Behavior
		_setBehavior();
		
		////////// Initial disposition
		_lyMainLoginMethods.setVisible(true);
		_lyOtherLoginMethods.setVisible(false);
		_frmUsrPasswd.setVisible(false);
	}
	private void _setBehavior() {
		// show other login methods
		_btnShowOtherLoginMethods.addClickListener(clickEvent -> {
														_lyMainLoginMethods.setVisible(false);
														_lyOtherLoginMethods.setVisible(true);
														_frmUsrPasswd.setVisible(false);
												   });
		// hide other login methods
		_btnHideOtherLoginMethods.addClickListener(clickEvent -> {
														_lyMainLoginMethods.setVisible(true);
														_lyOtherLoginMethods.setVisible(false);
														_frmUsrPasswd.setVisible(false);
												   });
		// show user password login
		_btnShowUsrPasswdForm.addClickListener(clickEvent -> {
													_lyMainLoginMethods.setVisible(false);
													_lyOtherLoginMethods.setVisible(false);
													_frmUsrPasswd.setVisible(true);
									  		   });
		// hide user password login
		_frmUsrPasswd.addHideButtonClickListener(clickEvent -> {
													_lyMainLoginMethods.setVisible(false);
													_lyOtherLoginMethods.setVisible(true);
													_frmUsrPasswd.setVisible(false);
											     });
		// user password login
		_frmUsrPasswd.setSignInButtonListener(loginAndPassword -> {
													_userPasswordLogin(loginAndPassword.getLoginId(),
																	   loginAndPassword.getPassword());
											  });
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
			Notification.show(_i18n.getMessage("security.login.method.user-password.invalid.user.or.passwd"),
							  Notification.Type.ERROR_MESSAGE);
		}
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
//	LANG SELECTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	protected Component _buildLangSelector(final Language... lang) {
		HorizontalLayout langSelector = new HorizontalLayout();
		langSelector.setMargin(false);
		langSelector.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		langSelector.addStyleName(VaadinSecurityTheme.LOGIN_LANG_SELECTOR);
		FluentIterable.from(lang)
					  .forEach(l -> {
									Button langButton = new Button(Languages.languageLowerCase(l));
									langButton.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
															 ValoTheme.LABEL_BOLD);
									langButton.addClickListener(clickEvent -> {
																	UI.getCurrent().setLocale(Languages.getLocale(l));
																	updateI18NMessages(_i18n);
																});
									langSelector.addComponent(langButton);
								});
		langSelector.setHeightUndefined();
		return langSelector;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BUTTONS
/////////////////////////////////////////////////////////////////////////////////////////	
	private VaadinLoginMethodButton _buildXLNetsButton() {
		VaadinLoginMethodButton outBtn = new VaadinLoginMethodButton(Path.from("img/xlnets-32x32.png"),
										   							 _securityLoginConfig.getProvider(SecurityProviderID.forId("xlnets")).getLoginUrl(),
										   							 Url.from(_securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase")),
										   							 _getWebAppUrlPath());
		outBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY,"xlnets-button");
		return outBtn;
	}
	private VaadinLoginMethodButton _buildGoogleButton() {
		VaadinLoginMethodButton outBtn = new VaadinLoginMethodButton(Path.from("img/google-32x32.svg"),
										   							 _securityLoginConfig.getProvider(SecurityProviderID.forId("google")).getLoginUrl(),
										   							 Url.from(_securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase")),
										   							 _getWebAppUrlPath());
		outBtn.addStyleNames(ValoTheme.BUTTON_DANGER,"google-button");
		return outBtn;
	}
	private VaadinLoginMethodButton _buildJustiziaButton() {
		VaadinLoginMethodButton outBtn = new VaadinLoginMethodButton(Path.from("img/logo_justizia.png"),
										   							 Url.from("http://justizia-login-url"),
										   							 Url.from(_securityLoginConfig.getUrlVars().getProperty("frontEndUrlBase")),
										   							 _getWebAppUrlPath());
		outBtn.addStyleNames(ValoTheme.BUTTON_PRIMARY,"justizia-button");
		outBtn.setVisible(false);
		return outBtn;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract I18NKey _getAppTitleI18NKey();
	protected abstract VaadinViewID _getMainViewId();
	protected abstract UrlPath _getWebAppUrlPath();
	
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_lblWelcome.setValue(i18n.getMessage("security.login.header").toUpperCase());
		_lblTitle.setValue(i18n.getMessage(_getAppTitleI18NKey()).toUpperCase());
		
		// corporate
		_lblCorporateLoginsTip.setValue(Strings.customized("{} {}", 
						      				  	  	   	   VaadinIcons.KEY_O.getHtml(),
						      				  	  	   	   i18n.getMessage("security.login.corporate.tip")));
		_btnXLNets.setCaption(i18n.getMessage("security.login.method.xlnets"));
		_btnShowOtherLoginMethods.setCaption(i18n.getMessage("security.login.method.other.show"));
		
		// other
		_lblOtherLoginsTip.setValue(Strings.customized("{} {}", 
						      				  	  		  VaadinIcons.WARNING.getHtml(),
						      				  	  		  i18n.getMessage("security.login.other.tip")));
		_btnGoogle.setCaption(i18n.getMessage("security.login.method.google"));
		_btnJustizia.setCaption(i18n.getMessage("security.login.method.justizia"));
		_btnHideOtherLoginMethods.setCaption(i18n.getMessage("security.login.method.other.hide"));
		
		// user password
		_btnShowUsrPasswdForm.setCaption(i18n.getMessage("security.login.method.user-password.show"));
		_frmUsrPasswd.updateI18NMessages(i18n);
	}
}
