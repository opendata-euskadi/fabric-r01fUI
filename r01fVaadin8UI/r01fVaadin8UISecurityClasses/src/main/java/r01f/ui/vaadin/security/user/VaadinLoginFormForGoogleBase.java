package r01f.ui.vaadin.security.user;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import r01f.model.security.login.google.GoogleLogin;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Strings;

/**
 * An [google login] data form that allows the user to configure the [google login]
 * ie: link / unlink the user with a [google login]
 * 
 * If the [user] has NOT previously linked his/her [google login]:
 * <pre class='brush:java'>
 * 		+---------------------------------------+
 * 		| Not linked to a [google login] [link] |
 * 		+---------------------------------------+
 * </pre>
 * 
 * If the [user] has previously linked his/her [google login]:
 * <pre class='brush:java'>
 * 		+------------------------------------------------------+
 * 		| Google login:  [futuretelematics@gmail.com] [unlink] |
 * 		+------------------------------------------------------+
 * </pre>
 * 
 * @param <U>
 * @param <L>
 * @param <VU>
 * @param <VL>
 * @param <P>
 */
public abstract class VaadinLoginFormForGoogleBase<U extends User,GL extends GoogleLogin,
												   VU extends VaadinViewUser<U>,VGL extends VaadinViewGoogleLogin<GL>,
												   P extends VaadinUserEditPresenterBase<U,?,GL,?,
													  								  	 VU,?,VGL,?,
													  								  	 ? extends VaadinUserEditCOREMediatorBase<U,?,GL,?,?>>> 
     		  extends VaadinLoginFormBase<P> {

	private static final long serialVersionUID = -2557548007931202687L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinGoogleLoginNotLinkedComponent _msgNotLinked;
	private final VaadinGoogleLoginLinkedComponent _frmLinked;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinLoginFormForGoogleBase(final UII18NService i18n, 
										final P presenter) {
		super(i18n, 
			  presenter);
		////////// UI controls
		_msgNotLinked = new VaadinGoogleLoginNotLinkedComponent(i18n);
		_frmLinked = new VaadinGoogleLoginLinkedComponent(i18n);
		
		////////// Style
		_msgNotLinked.setVisible(false);
		_frmLinked.setVisible(false);
		
		////////// Layout
		VerticalLayout mainLayout = new VerticalLayout(_msgNotLinked, 
													   _frmLinked);
		
		mainLayout.setMargin(true);
		this.setCompositionRoot(mainLayout);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC INTERFACE
/////////////////////////////////////////////////////////////////////////////////////////
	public void forUser(final UserOID userOid) {
		_userOid = userOid;
		_presenter.onGoogleLoginLoadRequested(userOid,
											  optionalViewLogin -> {
													boolean loginExists = optionalViewLogin.isPresent();
													
													// a) the login exists: show the details
													_frmLinked.setVisible(loginExists);
													if (loginExists) {
														VGL viewLogin = optionalViewLogin.get();
														_frmLinked.setLoginId(viewLogin.getLoginId());
													}
													
													// b) the login does NOT exist: show a message
													_msgNotLinked.setVisible(!loginExists);
											  });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NOT LINKED
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinGoogleLoginNotLinkedComponent
 		  extends Composite
 	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = 196908172392455312L;
		
		////////// UI fields
		private final Label _lblNotLinked = new Label();
		private final Button _btnLink = new Button();
		
		////////// Constructor
		private VaadinGoogleLoginNotLinkedComponent(final UII18NService i18n) {
			// layout
			HorizontalLayout hly = new HorizontalLayout(_lblNotLinked,_btnLink);
			hly.setComponentAlignment(_lblNotLinked, Alignment.MIDDLE_LEFT);
			this.setCompositionRoot(hly);
			_lblNotLinked.setContentMode(ContentMode.HTML);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnLink.addClickListener(clickEvent -> {});
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			String decoratedGoogleLabel = Strings.customized("<span class=\"v-label-bold\"><b>{}</b></span>", i18n.getMessage("security.login.method.google"));
			_lblNotLinked.setValue(i18n.getMessage("security.login.providers.google.not-linked", decoratedGoogleLabel));
			_btnLink.setCaption(i18n.getMessage("link"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LINKED
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinGoogleLoginLinkedComponent
 		  extends Composite
 	   implements VaadinViewI18NMessagesCanBeUpdated {

		private static final long serialVersionUID = -7164085699651511955L;
		
		////////// UI fields
		private final TextField _txtLoginId = new TextField();
		private final Button _btnUnLink = new Button();
		
		////////// Constructor
		private VaadinGoogleLoginLinkedComponent(final UII18NService i18n) {
			// layout
			HorizontalLayout hly = new HorizontalLayout(_txtLoginId,_btnUnLink);
			this.setCompositionRoot(hly);
			
			// style
			_txtLoginId.setReadOnly(true);
			
			// i18n
			this.updateI18NMessages(_i18n);
			
			// behavior
			_btnUnLink.addClickListener(clickEvent -> {});
		}
		////////// LoginId
		void setLoginId(final LoginID loginId) {
			_txtLoginId.setValue(loginId.asString());
		}
		LoginID getLoginId() {
			String loginIdStr = _txtLoginId.getValue();
			return Strings.isNOTNullOrEmpty(loginIdStr) ? LoginID.forId(loginIdStr) : null;
		}
		////////// i18n
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			_txtLoginId.setValue(i18n.getMessage("user-login-id"));
			_btnUnLink.setCaption(i18n.getMessage("unlink"));
		}
	}
}
