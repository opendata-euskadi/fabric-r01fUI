package r01f.ui.vaadin.tinymceeditor.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractTextFieldWidget;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VTinyMCETextField extends Widget implements AbstractTextFieldWidget {

    /**
     * Set the CSS class name to allow styling.
     */
    public static final String CLASSNAME = "v-tinymcetextfield";

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VTinyMCETextField() {
        // TODO This example code is extending the GWT Widget class so it must
        // set a root element.
        // Change to a proper element or remove this line if extending another
        // widget.
        setElement(Document.get().createDivElement());

        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    @Override
    public void setSelectionRange(int start, int length) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getValue() {
        return TinyMCEService.get(getElement().getId()).getContent();
    }

    @Override
    public void selectAll() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCursorPos() {
        return 0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setMaxLength(int maxlenght) {
        // not supported
    }

    public void setPlaceholder(String placeHolder) {
        // not supported
    }

    public void setText(String text) {
        //TinyMCEService.get(getElement().getId()).setContent(text);
    }

}
