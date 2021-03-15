package r01ui.ui.vaadin.security.theme;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.ui.vaadin.styles.VaadinValoTheme;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinSecurityTheme 
	          extends VaadinValoTheme {

	// Login page
	public static final String LOGIN_ROOT = "r01-login";
	public static final String LOGIN_TITLE = "r01-login-title";
	public static final String LOGIN_PANEL = "r01-login-panel";
	public static final String LOGIN_USR_PWD_FIELDS = "r01-login-form-fields";
	public static final String LOGIN_LANG_SELECTOR = "r01-login-lang-selector";	
}
