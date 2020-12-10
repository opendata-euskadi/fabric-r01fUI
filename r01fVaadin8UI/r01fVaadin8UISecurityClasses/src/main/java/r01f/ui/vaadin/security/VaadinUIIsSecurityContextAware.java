package r01f.ui.vaadin.security;

import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityContextStoreAtThreadLocalStorage;

/**
 * An interface for every view that is aware of the security context
 * maybe showing / hiding any ui component depending on the security context
 */
public interface VaadinUIIsSecurityContextAware {
	/**
	 * Gives any view / component implementing this interface an opportunity
	 * to configure the ui depending upon de security context
	 */
	public void configureUIUponSecurityContext();
	/**
	 * Returns the security context
	 * @param <S>
	 * @return
	 */
	public default <S extends SecurityContext> S getSecurityContext() {
		return SecurityContextStoreAtThreadLocalStorage.get();
	}
}
