package r01f.ui.viewobject;

import r01f.locale.Language;

public interface UIViewObjectByLanguage<L extends UIViewObjectInLanguage>
		 extends UIViewObject {
	/**
	 * Returns the language dependent view object
	 * @param lang
	 * @return
	 */
	public L getViewObjectFor(final Language lang);

}
