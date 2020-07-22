package r01f.ui.vaadin.tinymceeditor.client.ui;

import r01f.ui.vaadin.tinymceeditor.TinyMCETextField;
import r01f.ui.vaadin.tinymceeditor.client.ui.TinyMCEService.OnChangeListener;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.AbstractTextFieldConnector;
import com.vaadin.shared.ui.Connect;
import r01f.ui.vaadin.tinymceeditor.shared.TinymceState;

@Connect(value = TinyMCETextField.class, loadStyle = Connect.LoadStyle.EAGER)
public class TinyMCEConnector extends AbstractTextFieldConnector implements OnChangeListener {

    private boolean inited;
    private Object oldContent;
    private String paintableId;

    @Override
    protected void init() {
        super.init();

//        getWidget().addChangeHandler(event -> sendValueChange());
//        getWidget().addDomHandler(event -> {
//            getValueChangeHandler().scheduleValueChange();
//        }, InputEvent.getType());
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // Save the client side identifier (paintable id) for the widget
        paintableId = getState().id;
        if(paintableId == null) {
            paintableId = getConnectorId();
        }
        getWidget().getElement().setId(paintableId);

        if (!inited) {
            //set initial value
            this.getWidget().getElement().setInnerHTML(getState().text);
            //load the editor component
            TinyMCEService.loadEditor(paintableId, this, getState().conf);
            //mark the editor initialized
            inited = true;
        } else {

            //the editor is initialized, just update the text content
            // Also check if Vaadin decided to post the already known content to client
            // This might cause invalid state in some rare conditions
            boolean shouldSkipUpdate = oldContent != null && getState().text.equals(oldContent);
            if (!shouldSkipUpdate) {
                TinyMCEService.get(paintableId).setContent(getState().text);
            }
        }
    }

    @Override
    public TinymceState getState() {
        return (TinymceState) super.getState();
    }

    @Override
    public VTinyMCETextField getWidget() {
        return (VTinyMCETextField) super.getWidget();
    }

    @Override
    public void onChange() {
        sendValueChange();
    }

    @Override
    public void onEvent(NativeEvent event) {
        // Could hook lazy/eager mode here
    }

//
//    private void updateVariable() {
//        TinyMCEditor tinyMCEditor = TinyMCEService.get(paintableId);
//        if (tinyMCEditor == null) {
//            return;
//        }
//        String content = tinyMCEditor.getContent();
//        if (content != null && !content.equals(oldContent)) {
//            
//            client.updateVariable(paintableId, "text", content, immediate);
//        }
//        oldContent = content;
//    }

}
