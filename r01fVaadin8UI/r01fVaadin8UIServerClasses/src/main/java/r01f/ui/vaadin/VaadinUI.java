package r01f.ui.vaadin;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.patterns.Provider;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityContextStoreAtThreadLocalStorage;
import r01f.util.types.locale.Languages;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class VaadinUI {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public static Language getCurrentLanguage() {
		return Languages.of(UI.getCurrent().getLocale());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Obtains the current UI class being loaded.
	 * @return the UI class
	 */
	public static Class<? extends UI> uiType() {
		// TODO maybe it's better: Class<? extends UI> uiType = UI.getCurrent().getClass();
		
		String uiClassName = VaadinService.getCurrent().getDeploymentConfiguration().getUIClassName();
		if (uiClassName != null) {
			final ClassLoader classLoader = VaadinService.getCurrent().getClassLoader();
			try {
				Class<? extends UI> uiClass = Class.forName(uiClassName,true,
															classLoader)
													.asSubclass(UI.class);
				return uiClass;
			} catch (final ClassNotFoundException e) {
				throw new RuntimeException("Could not find UI class", e);
			}
		}
		return UI.class;
	}
	/**
	 * Returns the vaadin session stored security context
	 * @param <S>
	 * @param sessionParamId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <S extends SecurityContext> S getSercurityContextFromSession(final String sessionParamId) {
		S securityContext = (S)VaadinUI.createSecurityContextProviderFromVaadinSessionAttr(sessionParamId)
									   .provideValue();
		return securityContext;
	}
	@SuppressWarnings("unchecked")
	public static <S extends SecurityContext> Provider<S> createSecurityContextProviderFromThreadLocal() {
		return () -> (S)SecurityContextStoreAtThreadLocalStorage.get();
	}
	@SuppressWarnings("unchecked")
	public static <S extends SecurityContext> Provider<S> createSecurityContextProviderFromVaadinSessionAttr(final String sessionAttrId) {
		return () -> (S)VaadinService.getCurrentRequest()
									 .getWrappedSession()
									 .getAttribute(sessionAttrId);
	}
}
