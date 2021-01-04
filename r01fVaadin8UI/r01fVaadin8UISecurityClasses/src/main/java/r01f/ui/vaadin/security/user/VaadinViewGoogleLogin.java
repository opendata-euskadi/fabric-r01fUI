package r01f.ui.vaadin.security.user;

import lombok.experimental.Accessors;
import r01f.model.security.login.google.GoogleLogin;

@Accessors(prefix="_")
public abstract class VaadinViewGoogleLogin<GL extends GoogleLogin>
	 		  extends VaadinViewLogin<GL> {

	private static final long serialVersionUID = -693687373754829618L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewGoogleLogin(final GL login) {
		super(login);
	}
}
