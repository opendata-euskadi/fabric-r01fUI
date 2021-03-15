package r01f.ui.vaadin.security.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import r01f.securitycontext.SecurityIDS.LoginAndPassword;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01ui.ui.vaadin.security.theme.VaadinSecurityTheme;

/**
 * A user-password form like:
 * <pre>
 *		     User [                  ]
 *		 Password [                  ]
 *		 
 *		                    [sign-in]	
 *	
 *				   [remind]
 *		 		  [register]
 *
 *					[back] 
 * </pre>
 */
public class VaadinUserPasswordLoginForm 
	 extends Composite 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 4667007791809300501L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TextField _txtUser;
	private final TextField _txtPassword;
	private final Button _btnUsrPasswdSignIn;
	private final Button _btnHideUsrPasswdForm;
	private final Button _btnUsrPasswdRegister;
	private final Button _btnUsrPasswdRemind;
	
	private VaadinOnUserPasswordSignInRequestedListener _signInReqListener;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserPasswordLoginForm(final UII18NService i18n) {
		////////// UI
		// fields
		_txtUser = new TextField();
		_txtUser.setIcon(VaadinIcons.USER);
		_txtUser.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		_txtPassword = new PasswordField();
		_txtPassword.setIcon(VaadinIcons.LOCK);
		_txtPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		// sign in button
		_btnUsrPasswdSignIn = new Button();
		_btnUsrPasswdSignIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		_btnUsrPasswdSignIn.setClickShortcut(KeyCode.ENTER);

		_btnHideUsrPasswdForm = new Button(VaadinIcons.ARROW_BACKWARD);
		_btnHideUsrPasswdForm.addStyleName(ValoTheme.BUTTON_LINK);

		////////// layout
		_btnUsrPasswdRegister = new Button();
		_btnUsrPasswdRegister.addStyleName(ValoTheme.BUTTON_LINK);
		_btnUsrPasswdRemind = new Button();
		_btnUsrPasswdRemind.addStyleName(ValoTheme.BUTTON_LINK);
		
		// [back] [sign-in]
		HorizontalLayout lyRegRemind = new HorizontalLayout(_btnUsrPasswdRegister, 
														    _btnUsrPasswdRemind);
		lyRegRemind.setMargin(false);

		//     User [                  ]
		// Password [                  ]
		// 
		//                     [sign-in]		
		//		 	 [remind] [register]
		// [back]
		VerticalLayout vly = new VerticalLayout(_txtUser,
											    _txtPassword,
											    _btnUsrPasswdSignIn,
											    lyRegRemind,
											    _btnHideUsrPasswdForm);
		vly.setSpacing(true);
		vly.addStyleName(VaadinSecurityTheme.LOGIN_USR_PWD_FIELDS);
		vly.setComponentAlignment(_txtUser,Alignment.TOP_CENTER);
		vly.setComponentAlignment(_txtPassword,Alignment.TOP_CENTER);
		vly.setComponentAlignment(_btnUsrPasswdSignIn,Alignment.TOP_CENTER);	
		vly.setComponentAlignment(lyRegRemind,Alignment.TOP_CENTER);
		vly.setComponentAlignment(_btnHideUsrPasswdForm,Alignment.TOP_CENTER);
		
		this.setCompositionRoot(vly);
		
		////////// I18NM
		this.updateI18NMessages(i18n);
		
		///////// Behavior
		_setBehavior();
	}
	private void _setBehavior() {
		_btnUsrPasswdSignIn.addClickListener(clickEvent -> {
													LoginID loginId = LoginID.forId(_txtUser.getValue());
													Password passwd = Password.forId(_txtPassword.getValue());
												
													if (_signInReqListener != null) {
														_signInReqListener.onUserPasswordSignInRequested(new LoginAndPassword(loginId,passwd));
													}
											 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS
/////////////////////////////////////////////////////////////////////////////////////////
	public Registration addHideButtonClickListener(final ClickListener clickListener) {
		return _btnHideUsrPasswdForm.addClickListener(clickListener);
	}
	public interface VaadinOnUserPasswordSignInRequestedListener {
		public void onUserPasswordSignInRequested(final LoginAndPassword usrPwd);
	}
	public void setSignInButtonListener(final VaadinOnUserPasswordSignInRequestedListener listener) {
		_signInReqListener = listener;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_txtUser.setCaption(i18n.getMessage("security.login.method.user-password.field.user"));
		_txtPassword.setCaption(i18n.getMessage("security.login.method.user-password.field.pass"));
		_btnUsrPasswdSignIn.setCaption(i18n.getMessage("security.login.button.enter"));
		_btnHideUsrPasswdForm.setCaption(i18n.getMessage("security.login.method.user-password.hide"));
		_btnUsrPasswdRegister.setCaption(i18n.getMessage("security.login.method.user-password.register"));
		_btnUsrPasswdRemind.setCaption(i18n.getMessage("security.login.method.user-password.remind"));
	}
}
