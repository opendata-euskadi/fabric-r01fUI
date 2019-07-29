package r01f.ui.vaadin.viewobject;

import lombok.RequiredArgsConstructor;
import r01f.locale.Language;

@RequiredArgsConstructor
public class VaadinViewObjectHasDescriptionInSpanishAndBasqueDelegate 
  implements VaadinViewObjectHasDescriptionInSpanishAndBasque {

	private static final long serialVersionUID = 5062426729579488918L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinViewObjectHasDescriptionInSpanishAndBasque _hasDesc;
/////////////////////////////////////////////////////////////////////////////////////////
//  DELEGATED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getDescriptionEs() {
		return _hasDesc.getDescriptionEs();
	}
	@Override
	public void setDescriptionEs( final String value ) {
		_hasDesc.setDescriptionEs(value);
	}
	@Override
	public String getDescriptionEu() {
		return _hasDesc.getDescriptionEu();
	}
	@Override
	public void setDescriptionEu( final String value ) {
		_hasDesc.setDescriptionEu(value);
	}
	@Override
	public String getDescriptionIn(final Language lang) {
		if (lang.is(Language.SPANISH)) {
			return this.getDescriptionEs();
		} else if (lang.is(Language.BASQUE)) {
			return this.getDescriptionEu();
		} else {
			throw new IllegalArgumentException("Not a valid language: " + lang);
		}
	}
}
