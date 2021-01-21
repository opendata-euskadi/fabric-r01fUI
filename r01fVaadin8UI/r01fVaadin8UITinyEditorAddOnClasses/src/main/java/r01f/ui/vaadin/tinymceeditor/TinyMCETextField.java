package r01f.ui.vaadin.tinymceeditor;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.TextField;

import r01f.ui.vaadin.tinymceeditor.shared.TinymceState;

/**
 * Server side component for the VTinyMCETextField widget.
 */
//@JavaScript({"vaadin://../../../comun/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
@JavaScript({"vaadin://scripts/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
public class TinyMCETextField 
	 extends TextField{	  
	private static final long serialVersionUID = -384040888446323186L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * TinyMCE configuration values. 
	 */
	private String _config;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public TinyMCETextField() {
		super();
		this.setWidth("100%");
		this.setHeight(380,Unit.PIXELS);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void setConfig(final String jsConfig) {
		this.getState().conf = jsConfig;
	}
	@Override
	protected TinymceState getState() {
		return (TinymceState)super.getState();
	}
}
