package r01f.ui.viewobject;

import r01f.locale.Language;

public interface UIViewObjectHasNameInSpanishAndBasque {
	public String getNameEs();
	public void setNameEs( String value );
	public String getNameEu();
	public void setNameEu( String value );
	
	public String getNameIn(final Language lang);
}
