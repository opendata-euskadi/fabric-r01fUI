package r01f.ui.viewobject;

import r01f.locale.Language;

@Deprecated // use UIViewObjectByLanguage
public interface UIViewObjectHasLanguageDependentData<L extends UIViewObjectLanguageDependent>
		 extends UIViewObject {
	/**
	 * Returns the language dependent view object
	 * @param lang
	 * @return
	 */
	public L getViewObjectFor(final Language lang);
}
