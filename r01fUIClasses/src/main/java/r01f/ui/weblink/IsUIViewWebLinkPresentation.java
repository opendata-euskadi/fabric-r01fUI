package r01f.ui.weblink;

import r01f.ui.viewobject.UIViewObject;

public interface IsUIViewWebLinkPresentation 
		 extends UIViewObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	TEXT / TITLE / DESCRIPTION
/////////////////////////////////////////////////////////////////////////////////////////
	public String getText();
	public void setText(final String text);
	
	public String getTitle();
	public void setTitle(final String text);
	
	public String getDescription();
	public void setDescription(final String description);
/////////////////////////////////////////////////////////////////////////////////////////
//	WINDOW OPENING
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean isOpeningInNewWindow();
	public void setOpeningInNewWindow(final boolean open);
}
