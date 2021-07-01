
package r01f.ui.vaadin.i18n;

import java.util.Collection;
import java.util.Locale;

import com.vaadin.ui.UI;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.locale.Language;
import r01f.types.JavaPackage;
import r01f.ui.i18n.UII18NManagerBase;
import r01f.util.types.locale.Languages;


@Slf4j
@Accessors(prefix="_")
public abstract class VaadinUII18NManager
			  extends UII18NManagerBase {

	private static final long serialVersionUID = 1841465816645518204L;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUII18NManager(final Collection<JavaPackage> pckgNames) {
		super(pckgNames);
	}
	public VaadinUII18NManager(final ClassLoader classLoader,
							   final Collection<JavaPackage> pckgNames) {
		super(classLoader,
			  pckgNames);
	}
	public VaadinUII18NManager(final VaadinUII18NManager other) {
		super(other.getI18nBundleNames());	// clone
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Locale getCurrentLocale() {
		Locale outLoc = null;
		if (UI.getCurrent() != null) 
			outLoc = UI.getCurrent().getLocale();
		if (outLoc == null) log.warn("NO current locale!! detault to {}", Languages.getLocale(Language.DEFAULT).toString());
		return outLoc != null ? outLoc : Languages.getLocale(Language.DEFAULT);
	}
}
