package r01f.ui.vaadin.viewobject;

import r01f.locale.Language;

public interface VaadinViewObjectHasLanguageDependentData<L extends VaadinViewObjectLanguageDependent>
		 extends VaadinViewObject {
	/**
	 * Returns the language dependent view object
	 * @param lang
	 * @return
	 */
	public L getViewObjectFor(final Language lang);
}
