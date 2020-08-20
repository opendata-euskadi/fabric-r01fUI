package r01f.ui.vaadin.tinymceeditor.client.ui;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper around TinyMCE js object
 * 
 * TODO extend to include all appropriate methods.
 * 
 * See http://wiki.moxiecode.com/index.php/TinyMCE:API/tinymce.Editor for
 * documentation and the rest of the api
 */
public class TinyMCEditor 
	 extends JavaScriptObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected TinyMCEditor() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final native boolean isDirty()
	/*-{
		return this.isDirty();
	}-*/;
	public final native String getContent()
	/*-{
		return this.getContent();
	}-*/;
	public final native void setContent(String stringVariable)
	/*-{
		return this.setContent(stringVariable);
	}-*/;
}
