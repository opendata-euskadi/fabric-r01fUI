package r01f.ui.vaadin.tinymceeditor.client.ui;

import r01f.ui.vaadin.tinymceeditor.TinyMCETextField;
import r01f.ui.vaadin.tinymceeditor.client.ui.TinyMCEService.OnChangeListener;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.AbstractTextFieldConnector;
import com.vaadin.shared.ui.Connect;
import r01f.ui.vaadin.tinymceeditor.shared.TinymceState;

@Connect(value = TinyMCETextField.class, 
		 loadStyle = Connect.LoadStyle.EAGER)
public class TinyMCEConnector 
	 extends AbstractTextFieldConnector 
  implements OnChangeListener {

	private static final long serialVersionUID = 5951797405554549926L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private boolean _inited;
	private Object _oldContent;
	private String _paintableId;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void init() {
		super.init();

//		getWidget().addChangeHandler(event -> sendValueChange());
//		getWidget().addDomHandler(event -> {
//			getValueChangeHandler().scheduleValueChange();
//		}, InputEvent.getType());
	}
	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		// Save the client side identifier (paintable id) for the widget
		_paintableId = getState().id;
		if (_paintableId == null) {
			_paintableId = getConnectorId();
		}
		this.getWidget().getElement().setId(_paintableId);

		if (!_inited) {
			//set initial value
			this.getWidget().getElement().setInnerHTML(this.getState().text);
			//load the editor component
			TinyMCEService.loadEditor(_paintableId, 
									  this,
									  this.getState().conf);
			//mark the editor initialized
			_inited = true;
		} else {
			// the editor is initialized, just update the text content
			// Also check if Vaadin decided to post the already known content to client
			// This might cause invalid state in some rare conditions
			boolean shouldSkipUpdate = _oldContent != null && this.getState().text.equals(_oldContent);
			if (!shouldSkipUpdate) {
				TinyMCEService.get(_paintableId).setContent(this.getState().text);
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
	public void onEvent(final NativeEvent event) {
		// Could hook lazy/eager mode here
	}
//	private void updateVariable() {
//		TinyMCEditor tinyMCEditor = TinyMCEService.get(paintableId);
//		if (tinyMCEditor == null) {
//			return;
//		}
//		String content = tinyMCEditor.getContent();
//		if (content != null && !content.equals(oldContent)) {
//			
//			client.updateVariable(paintableId, "text", content, immediate);
//		}
//		oldContent = content;
//	}
}
