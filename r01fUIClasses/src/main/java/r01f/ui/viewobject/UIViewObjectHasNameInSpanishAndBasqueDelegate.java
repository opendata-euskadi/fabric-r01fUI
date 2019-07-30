package r01f.ui.viewobject;

import lombok.RequiredArgsConstructor;
import r01f.locale.Language;

@RequiredArgsConstructor
public class UIViewObjectHasNameInSpanishAndBasqueDelegate 
  implements UIViewObjectHasNameInSpanishAndBasque {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	private final UIViewObjectHasNameInSpanishAndBasque _hasDesc;
/////////////////////////////////////////////////////////////////////////////////////////
//  DELEGATED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getNameEs() {
		return _hasDesc.getNameEs();
	}
	@Override
	public void setNameEs( String value ) {
		_hasDesc.setNameEs(value);
	}
	@Override
	public String getNameEu() {
		return _hasDesc.getNameEu();
	}
	@Override
	public void setNameEu( String value ) {
		_hasDesc.setNameEu(value);
	}
	@Override
	public String getNameIn(final Language lang) {
		if (lang.is(Language.SPANISH)) {
			return this.getNameEs();
		} else if (lang.is(Language.BASQUE)) {
			return this.getNameEu();
		} else {
			throw new IllegalArgumentException("Not a valid language: " + lang);
		}
	}
}
