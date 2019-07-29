package r01f.ui.vaadin.viewobject;

import r01f.locale.Language;

public interface VaadinViewObjectHasDescriptionInSpanishAndBasque 
		 extends VaadinViewObject {
	public String getDescriptionEs();
	public void setDescriptionEs( String value );
	public String getDescriptionEu();
	public void setDescriptionEu( String value );
	
	public String getDescriptionIn(final Language lang);
}
