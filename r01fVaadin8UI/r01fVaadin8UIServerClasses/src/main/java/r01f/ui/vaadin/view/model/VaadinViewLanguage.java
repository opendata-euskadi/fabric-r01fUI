package r01f.ui.vaadin.view.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.Strings;
import r01f.util.types.locale.Languages;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class VaadinViewLanguage
  implements UIViewObject {

	private static final long serialVersionUID = 1449034784861550776L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final Language _language;
	@Getter private final Language _showInLanguage;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public String getLanguageCode() {
		return Languages.languageLowerCase(_language);
	}
	public String getName() {
		return Strings.capitalizeFirstLetter(Languages.LANGUAGE_NAMES.get(_language)
																	 .get(_showInLanguage));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object other) {
		if (other == null) return false;
		if (this == other) return true;
		if (!(other instanceof VaadinViewLanguage)) return false;
		VaadinViewLanguage otherVLang = (VaadinViewLanguage)other;
		return _language.is(otherVLang.getLanguage())
			&& _showInLanguage.is(otherVLang.getShowInLanguage());
	}
	@Override
	public int hashCode() {
		return _language.hashCode();
	}
}
