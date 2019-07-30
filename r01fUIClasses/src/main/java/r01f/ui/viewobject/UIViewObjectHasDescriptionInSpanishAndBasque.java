package r01f.ui.viewobject;

import r01f.locale.Language;

public interface UIViewObjectHasDescriptionInSpanishAndBasque 
		 extends UIViewObject {
	public String getDescriptionEs();
	public void setDescriptionEs( String value );
	public String getDescriptionEu();
	public void setDescriptionEu( String value );
	
	public String getDescriptionIn(final Language lang);
}
