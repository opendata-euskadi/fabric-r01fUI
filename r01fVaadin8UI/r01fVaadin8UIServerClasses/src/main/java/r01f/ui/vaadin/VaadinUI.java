package r01f.ui.vaadin;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class VaadinUI {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Obtains the current UI class being loaded.
	 * @return the UI class
	 */
	public static Class<? extends UI> uiType() {
		// TODO maybe it's better:
//		Class<? extends UI> uiType = UI.getCurrent().getClass();
		
		final String uiClassName = VaadinService.getCurrent().getDeploymentConfiguration().getUIClassName();
		if (uiClassName != null) {
			final ClassLoader classLoader = VaadinService.getCurrent().getClassLoader();
			try {
				final Class<? extends UI> uiClass = Class.forName(uiClassName,true,
																  classLoader)
														 .asSubclass(UI.class);
				return uiClass;
			} catch (final ClassNotFoundException e) {
				throw new RuntimeException("Could not find UI class", e);
			}
		}
		return UI.class;
	}
}
