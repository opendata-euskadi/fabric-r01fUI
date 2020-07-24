package r01f.ui.vaadin.tinymceeditor;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.TextField;

import r01f.ui.vaadin.tinymceeditor.shared.TinymceState;

/**
 * Server side component for the VTinyMCETextField widget.
 */
//@JavaScript({"vaadin://../../../comun/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
@JavaScript({"vaadin://scripts/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
public class TinyMCETextField extends TextField{	  
	private static final long serialVersionUID = -384040888446323186L;

	/**
	 * TinyMCE configuration values. 
	 */
	private String config;
	
	public TinyMCETextField() {
        super();
        setWidth("100%");
        setHeight("280px");
    }

    public void setConfig(String jsConfig) {
        getState().conf = jsConfig;
    }

    @Override
    protected TinymceState getState() {
        return (TinymceState) super.getState();
    }
}
