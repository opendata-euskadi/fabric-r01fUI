package r01f.ui.vaadin.security.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Strings;

/**
 * An [user password login] data form that allows the user to configure the [user password login]
 * ie:
 * 		- Create a [user password login] (if the user does NOT have one)
 * 		- Set the [loginId]
 * 		- Change the [password]
 * 
 * It can work in TWO modes:
 * 
 * a) The [user password login] already exists > the [loginId] or [password] can be changed
 * 	  <pre class='brush:java'>
 * 			+-------------------------------+
 * 			| LoginId [xxxxxxxxx]  [change] |
 * 			| [change password]  ----------------> +------------------------------------+
 * 			+-------------------------------+      |    current password [            ] |
 * 												   |          ¿forgot current password? ----> sends an email that begins
 * 												   |                                    |     the password reset proccess
 * 												   |        new password [            ] |
 * 												   | repeat new password [            ] |
 * 												   |                                    |
 * 												   | [cancel]         [change password] |
 * 												   +------------------------------------+
 * 	  </pre> 
 * 
 * b) The [user password login] DOES NOT exist > create a NEW login
 * 			+----------------------------------------------+
 * 			| The user DOES NOT have a user/password login |
 * 			| you can CREATE a NEW one if you wish         |
 * 			|											   |
 * 			|                [ create user/password login]------>	+-------------------------------+		
 *          +----------------------------------------------+        |         LoginId [           ] |
 *                                                                  |        Password [           ] |
 * 			                                                        | Repeat password [           ]	|
 * 			                                                        |                               |
 * 			                                                        | [cancel]             [Create] |
 * 			                                                        +-------------------------------+
 */
public abstract class VaadinoginFormForUserPasswordLBase<U extends User,L extends UserPasswordLogin,
												  	     VU extends VaadinViewUser<U>,VL extends VaadinViewUserPasswordLogin<L>,
												  	     P extends VaadinUserEditPresenterBase<U,L,?,?,
													  										   VU,VL,?,?,
													  										   ? extends VaadinUserEditCOREMediatorBase<U,L,?,?,?>>> 
     		  extends VaadinLoginFormBase<P> {

	private static final long serialVersionUID = -2996792475557413032L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinUserPasswordLoginDetailComponent _detailLogin;
	private final VaadinUserLoginIdChangeForm _frmLoginIdChange;
	private final VaadinUserPassworChangeForm _frmUserPasswordChange;
	
	private final VaadinUserPassworLoginDoesNotExistComponent _msgUserPasswordLoginDoesNotExist;
	private final VaadinUserPasswordLoginCreateForm _frmUserPasswordLoginCreate;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinoginFormForUserPasswordLBase(final UII18NService i18n,
										 	  final P presenter) {
		////////// services
		super(i18n,
			  presenter);
		
		////////// UI
		_detailLogin = new VaadinUserPasswordLoginDetailComponent(i18n);
		_frmLoginIdChange = new VaadinUserLoginIdChangeForm(i18n);
		_frmUserPasswordChange = new VaadinUserPassworChangeForm(i18n);
		
		_msgUserPasswordLoginDoesNotExist = new VaadinUserPassworLoginDoesNotExistComponent(i18n);
		_frmUserPasswordLoginCreate = new VaadinUserPasswordLoginCreateForm(i18n);
		
		// all components are NOT visible by default
		_detailLogin.setVisible(false);
		_frmLoginIdChange.setVisible(false);
		_frmUserPasswordChange.setVisible(false);
		
		_msgUserPasswordLoginDoesNotExist.setVisible(false);
		_frmUserPasswordLoginCreate.setVisible(false);
		
		////////// Layout
		CssLayout ly = new CssLayout(_detailLogin,
									 _frmLoginIdChange,
									 _frmUserPasswordChange,
									 _msgUserPasswordLoginDoesNotExist,
									 _frmUserPasswordLoginCreate);
		this.setCompositionRoot(ly);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC INTERFACE
/////////////////////////////////////////////////////////////////////////////////////////
	public void forUser(final UserOID userOid) {
		_userOid = userOid;
		_presenter.onUserPasswordLoginLoadRequested(userOid,
													optionalViewLogin -> {
														boolean loginExists = optionalViewLogin.isPresent();
														
														// a) the login exists: show the details
														_detailLogin.setVisible(loginExists);
														_frmLoginIdChange.setVisible(false);
														_frmUserPasswordChange.setVisible(false);
														if (loginExists) {
															VL viewLogin = optionalViewLogin.get();
															_detailLogin.setLoginId(viewLogin.getLoginId());
														}
														
														// b) the login does NOT exist: show a message
														_msgUserPasswordLoginDoesNotExist.setVisible(!loginExists);
														_frmUserPasswordLoginCreate.setVisible(false);
													});
	}
	protected abstract VL _createViewUserPasswordLoginFrom(final UserOID userOid,
														   final LoginID loginId,final Password pwd);
/////////////////////////////////////////////////////////////////////////////////////////
//	USER / PASSWORD LOGIN DETAIL
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinUserPasswordLoginDetailComponent 
	  	  extends Composite 
	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = 1308587048876375375L;
		
		////////// UI fields
		private final TextField _txtLoginId = new TextField();
		private final Button _btnChangeLoginId = new Button();
		private final Button _btnChangePasswd = new Button();
		
		////////// Constructor
		private VaadinUserPasswordLoginDetailComponent(final UII18NService i18n) {
			HorizontalLayout hlyLoginId = new HorizontalLayout(_txtLoginId,_btnChangeLoginId);
			VerticalLayout vly = new VerticalLayout(hlyLoginId,
													_btnChangePasswd);
			this.setCompositionRoot(vly);
			
			// style
			_txtLoginId.setReadOnly(true);	// cannot change
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior 
			_btnChangeLoginId.addClickListener(clickEvent -> _frmLoginIdChange.show());			// show the [login id] change form
			_btnChangePasswd.addClickListener(clickEvent -> _frmUserPasswordChange.show());		// show the [password] change form
		}
		////////// Value
		LoginID getLoginId() {
			return LoginID.forId(_txtLoginId.getValue());
		}
		void setLoginId(final LoginID loginId) {
			_txtLoginId.setValue(loginId != null ? loginId.asString() : "");
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_txtLoginId.setCaption(i18n.getMessage("user-login-id"));
			_btnChangeLoginId.setCaption(i18n.getMessage("security.user-login-id.change"));
			_btnChangePasswd.setCaption(i18n.getMessage("security.password.change"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGINID CHANGE
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinUserLoginIdChangeForm 
		  extends Composite 
	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = -6672325226690496098L;
		
		////////// UI fields
		private final TextField _txtLoginId = new TextField();
		private final Label _lblLoginIdAlreadyExists = new Label();
		
		private final Button _btnCancel = new Button();
		private final Button _btnChange = new Button();
		
		////////// Constructor
		private VaadinUserLoginIdChangeForm(final UII18NService i18n) {
			// layout
			HorizontalLayout hlyLoginId = new HorizontalLayout(_txtLoginId,_lblLoginIdAlreadyExists);
			HorizontalLayout hlyBtns = new HorizontalLayout(_btnCancel,_btnChange);
			hlyBtns.setComponentAlignment(_btnCancel,Alignment.BOTTOM_LEFT);
			hlyBtns.setComponentAlignment(_btnChange,Alignment.BOTTOM_RIGHT);
			VerticalLayout vly = new VerticalLayout(hlyLoginId,
													hlyBtns);
			this.setCompositionRoot(vly);
			
			// style
			_lblLoginIdAlreadyExists.setIcon(VaadinIcons.WARNING);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnCancel.addClickListener(clickEvent -> this.hide());
			_btnChange.addClickListener(clickEvent -> {
											LoginID loginId = LoginID.forId(_txtLoginId.getValue());	// TODO do NOT allow empty loginId
											_presenter.onUserPasswordLoginExistenceCheckRequested(loginId,
																								  alreadyExists -> {
																									  	_lblLoginIdAlreadyExists.setVisible(alreadyExists);
																										if (alreadyExists) return;
																										
																										// the loginId DOES NOT exists: update
																										_presenter.onUserPasswordLoginIdChangeRequested(_userOid,loginId,
																																					    viewLogin -> {
																																							  // the login has changed
																																							  _detailLogin.setLoginId(viewLogin.getLoginId());
																																							  this.hide();
																																							  
																																							  Notification.show(i18n.getMessage("securith.login.changed"),
																																									  			Type.TRAY_NOTIFICATION);
																																					    });
																								  });
										});
		}
		////////// show / hide
		void show() {
			_txtLoginId.setValue("");
			_lblLoginIdAlreadyExists.setVisible(false);
			
			this.setVisible(true);
			_detailLogin.setVisible(false);
		}
		void hide() {
			this.setVisible(false);
			_detailLogin.setVisible(true);
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_txtLoginId.setCaption(i18n.getMessage("user-login-id"));
			_lblLoginIdAlreadyExists.setCaption(i18n.getMessage("security.user-login-id.already.exists"));
			_btnCancel.setCaption(i18n.getMessage("cancel"));
			_btnChange.setCaption(i18n.getMessage("change"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PASSWORD CHANGE
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinUserPassworChangeForm 
		  extends Composite 
	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = -2222171167404982164L;
		
		////////// UI fields
		private final PasswordField _txtOldPassword = new PasswordField();
		private final Button _btnResetOldPassword = new Button();
		private final PasswordField _txtNewPassword = new PasswordField();
		private final PasswordField _txtConfirmNewPassword = new PasswordField();
		
		private final Button _btnCancel = new Button();
		private final Button _btnChange = new Button();
		
		////////// Constructor
		private VaadinUserPassworChangeForm(final UII18NService i18n) {
			// layout
			HorizontalLayout hlyBtns = new HorizontalLayout(_btnCancel,_btnChange);
			hlyBtns.setComponentAlignment(_btnCancel,Alignment.BOTTOM_LEFT);
			hlyBtns.setComponentAlignment(_btnChange,Alignment.BOTTOM_RIGHT);
			VerticalLayout vly = new VerticalLayout(_txtOldPassword,
													_btnResetOldPassword,
													_txtNewPassword,
													_txtConfirmNewPassword,
													hlyBtns);
			this.setCompositionRoot(vly);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnCancel.addClickListener(clickEvent -> this.hide());
			_btnChange.addClickListener(clickEvent -> {
											// check that the password matches
											boolean pwdMatch = _verifyPasswords(_txtNewPassword.getValue(),
																				_txtConfirmNewPassword.getValue(),
																				true);	// the passwd is mandatory
											if (!pwdMatch) {
												Notification.show(_i18n.getMessage("password-not-match"),
																				   Notification.Type.ERROR_MESSAGE);
												return;
											}
											
											// change the password
											LoginID loginId = _detailLogin.getLoginId();
											Password oldPwd = Password.forId(_txtOldPassword.getValue());
											Password newPwd = Password.forId(_txtNewPassword.getValue());
											_presenter.onUserPasswordLoginPasswordChangeRequested(loginId,oldPwd,newPwd,
																						    	  viewLogin -> { 
																										this.hide();
																										Notification.show(i18n.getMessage("security.password.changed"),
																														  Type.TRAY_NOTIFICATION);
																						    	  });
										});
		}
		////////// show / hide
		void show() {
			_txtOldPassword.setValue("");
			_txtNewPassword.setValue("");
			_txtConfirmNewPassword.setValue("");
			
			this.setVisible(true);
			_detailLogin.setVisible(false);
		}
		void hide() {
			this.setVisible(false);
			_detailLogin.setVisible(true);
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_txtOldPassword.setCaption(i18n.getMessage("security.password.current"));
			_btnResetOldPassword.setCaption(i18n.getMessage("security.password.forgot"));
			_txtNewPassword.setCaption(i18n.getMessage("security.password.new"));
			_txtConfirmNewPassword.setCaption(i18n.getMessage("security.password.new.confirm"));
			_btnCancel.setCaption(i18n.getMessage("cancel"));
			_btnChange.setCaption(i18n.getMessage("change"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THE USER DOES NOT HAVE USER/PASSWORD LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinUserPassworLoginDoesNotExistComponent 
		  extends Composite 
	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = 344366352391520366L;
		
		////////// UI fields
		private final Label _lblUserNotHaveUserPasswordLogin = new Label("security.user-does-not-have-userpassword-login"); 
		private final Label _lblUserCanCreateUserPasswordLogin = new Label("security.user-can-create-userpassword-login");
		private final Button _btnCreateUserPasswordLogin = new Button();
		
		////////// Constructor
		private VaadinUserPassworLoginDoesNotExistComponent(final UII18NService i18n) {
			// layout
			VerticalLayout vly = new VerticalLayout(_lblUserNotHaveUserPasswordLogin,
													_lblUserCanCreateUserPasswordLogin,
													_btnCreateUserPasswordLogin);
			vly.setComponentAlignment(_lblUserNotHaveUserPasswordLogin,Alignment.TOP_LEFT);
			vly.setComponentAlignment(_lblUserCanCreateUserPasswordLogin,Alignment.TOP_LEFT);
			vly.setComponentAlignment(_btnCreateUserPasswordLogin,Alignment.TOP_RIGHT);
			this.setCompositionRoot(vly);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnCreateUserPasswordLogin.addClickListener(clickEvent -> _frmUserPasswordLoginCreate.show());
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_lblUserNotHaveUserPasswordLogin.setCaption(i18n.getMessage("security.user-does-not-have-userpassword-login"));
			_lblUserCanCreateUserPasswordLogin.setCaption(i18n.getMessage("security.user-can-create-userpassword-login"));
			_btnCreateUserPasswordLogin.setCaption(i18n.getMessage("create"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER/PASSWORD LOGIN CREATE
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinUserPasswordLoginCreateForm 
		  extends Composite 
	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = 1898655906514157437L;
		
		////////// UI fields
		private final TextField _txtLoginId = new TextField();
		private final Label _lblLoginIdAlreadyExists = new Label();
		private final PasswordField _txtPassword = new PasswordField();
		private final PasswordField _txtConfirmPassword = new PasswordField();
		
		private final Button _btnCancel = new Button();
		private final Button _btnCreate = new Button();
		
		////////// Constructor
		private VaadinUserPasswordLoginCreateForm(final UII18NService i18n) {
			// layout
			HorizontalLayout hlyLoginId = new HorizontalLayout(_txtLoginId,_lblLoginIdAlreadyExists);
			HorizontalLayout hlyBtns = new HorizontalLayout(_btnCancel,_btnCreate);
			hlyBtns.setComponentAlignment(_btnCancel,Alignment.BOTTOM_LEFT);
			hlyBtns.setComponentAlignment(_btnCreate,Alignment.BOTTOM_RIGHT);
			
			VerticalLayout vly = new VerticalLayout(hlyLoginId,
													_txtPassword,
													_txtConfirmPassword,
													hlyBtns);
			this.setCompositionRoot(vly);
			
			// style
			_lblLoginIdAlreadyExists.setIcon(VaadinIcons.WARNING);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnCancel.addClickListener(clickEvent -> this.hide());
			_btnCreate.addClickListener(clickEvent -> {
											// [1] - check that the password matches
											boolean pwdMatch = _verifyPasswords(_txtPassword.getValue(),
																				_txtConfirmPassword.getValue(),
																				true);	// the passwd is mandatory
											if (!pwdMatch) {
												Notification.show(_i18n.getMessage("password-not-match"),
																				   Notification.Type.ERROR_MESSAGE);
												return;
											}
				
											// [2] - create the login
											LoginID loginId = LoginID.forId(_txtLoginId.getValue());
											Password password = Password.forId(_txtPassword.getValue());	
											// a) check that the [loginId] DOES NOT already exist
											_presenter.onUserPasswordLoginExistenceCheckRequested(loginId,
																								  alreadyExist -> {
																									  	_lblLoginIdAlreadyExists.setVisible(alreadyExist);
																										if (alreadyExist) return;
																										
																										// the login does NOT exist: create it 
																										VL viewLogin = _createViewUserPasswordLoginFrom(_userOid,
																																						loginId,password);
																										_presenter.onUserPasswordLoginCreateRequested(viewLogin,
																																					  createdViewLogin -> {
																																						  this.hide();
																																						  Notification.show(i18n.getMessage("securith.login.created"),
																																								  			Type.TRAY_NOTIFICATION);
																																					  });
																								  });
										});
		}
		////////// show / hide
		void show() {
			_txtLoginId.setValue("");
			_lblLoginIdAlreadyExists.setVisible(false);		// NOT visible by default
			_txtPassword.setValue("");
			_txtConfirmPassword.setValue("");
			
			_msgUserPasswordLoginDoesNotExist.setVisible(false);
			this.setVisible(true);
		}
		void hide() {
			this.setVisible(false);
			_msgUserPasswordLoginDoesNotExist.setVisible(true);
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_txtLoginId.setCaption(i18n.getMessage("user-login-id"));
			_lblLoginIdAlreadyExists.setCaption(i18n.getMessage("security.user-login-id.already.exists"));
			_txtPassword.setCaption(i18n.getMessage("password"));
			_txtConfirmPassword.setCaption(i18n.getMessage("password-confirm"));
			_btnCancel.setCaption(i18n.getMessage("cancel"));
			_btnCreate.setCaption(i18n.getMessage("create"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static boolean _verifyPasswords(final String password,
									 		final String confirmPassword,
									 		final boolean mandatoryPassword) {
		if (!mandatoryPassword) return Strings.isNullOrEmpty(password) 
									&& Strings.isNullOrEmpty(confirmPassword);
		if (Strings.isNullOrEmpty(password)) return false;
		if (Strings.isNullOrEmpty(confirmPassword)) return false;
		
		boolean pwdMatch = password.equals(confirmPassword);
		return pwdMatch;
	}
}
