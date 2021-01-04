package r01f.ui.vaadin.security.user;

import lombok.experimental.Accessors;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.securitycontext.SecurityIDS.Password;

@Accessors(prefix="_")
public abstract class VaadinViewUserPasswordLogin<L extends UserPasswordLogin>
	 		  extends VaadinViewLogin<L> {

	private static final long serialVersionUID = -1260091998739557900L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewUserPasswordLogin(final L login) {
		super(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PASSWORD_FIELD = "password";
	public Password getPassword() {
		return _wrappedModelObject.getPassword();
	}
	public void setPassword(final Password password) {
		_wrappedModelObject.setPassword(password);
	}
}
