
package r01f.ui.vaadin.tinymceeditor;
import java.util.Map;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.LegacyComponent;
import com.vaadin.ui.TextField;

import r01f.ui.vaadin.tinymceeditor.shared.TinymceState;

/**
 * Server side component for the VTinyMCETextField widget.
 */
//@JavaScript({"vaadin://../../../comun/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
@JavaScript({"vaadin://scripts/tinymce4/js/tinymce/tinymce.min.js", "client/ui/r01vtinymce.js"})
public class TinyMCETextField extends TextField implements LegacyComponent{	  
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

	

	@Override
	public void paintContent(PaintTarget target) throws PaintException {		
		if ( config != null ) {
			target.addAttribute( "conf", config );
		}
		
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		// TODO Auto-generated method stub
	}
    
}
