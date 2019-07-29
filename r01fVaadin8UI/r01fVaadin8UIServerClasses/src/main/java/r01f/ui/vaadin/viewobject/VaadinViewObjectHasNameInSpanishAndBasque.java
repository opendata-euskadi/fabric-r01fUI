package r01f.ui.vaadin.viewobject;

import r01f.locale.Language;

public interface VaadinViewObjectHasNameInSpanishAndBasque {
	public String getNameEs();
	public void setNameEs( String value );
	public String getNameEu();
	public void setNameEu( String value );
	
	public String getNameIn(final Language lang);
}
