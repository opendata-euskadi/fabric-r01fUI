package r01f.ui.vaadin.codemirror;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.HasValueChangeMode;

@JavaScript({
		// main library
		"vaadin://codemirror/codemirror.js?v=0.9",
		// Vaadin connector
		"vaadin://codemirror/codemirror-connector.js?v=0.9",
		// language support
		"vaadin://codemirror/mode/clike/clike.js?v=0.9",
		// language support
		"vaadin://codemirror/mode/javascript/javascript.js?v=0.9",
		// language support
		"vaadin://codemirror/mode/xml/xml.js?v=0.9",
		// language support
		"vaadin://codemirror/mode/markdown/markdown.js?v=0.9",
		// language support
		"vaadin://codemirror/mode/css/css.js?v=0.9",
		// code folding
		"vaadin://codemirror/fold/foldcode.js?v=0.9",
		// code folding
		"vaadin://codemirror/fold/comment-fold.js?v=0.9",
		// code folding
		"vaadin://codemirror/fold/brace-fold.js?v=0.9",
		// code folding
		"vaadin://codemirror/fold/foldgutter.js?v=0.9", })
@StyleSheet({
		// main styles
		"vaadin://codemirror/codemirror.css?v=0.9",
		// code folding
		"vaadin://codemirror/fold/foldgutter.css?v=0.9", })
public class CodeMirrorField extends AbstractJavaScriptComponent implements HasValue<String>, HasValueChangeMode {
	private final List<ValueChangeListener<String>> valueChangeListeners = new ArrayList<>();
	private String value;

	public CodeMirrorField() {
		getState().mode = "text/x-java";
		getState().indentUnit = 2;
		getState().lineNumbers = true;
		getState().lineWrapping = true;
		getState().foldGutter = true;
		getState().gutters = new ArrayList(Arrays.asList("CodeMirror-linenumbers", "CodeMirror-foldgutter"));
		getState().readOnly = ReadOnly.FALSE;

		addFunction("onValueChange", arguments -> {
			String value = arguments.asString();
			setValue(value, true);
		});

		setValueChangeMode(ValueChangeMode.EAGER);
	}

	public void setMode(final String mode) {
		getState().mode = mode;
	}

	public void setIndentUnit(final int indentUnit) {
		getState().indentUnit = indentUnit;
	}

	@Override
	public void setValueChangeMode(final ValueChangeMode valueChangeMode) {
		getState().valueChangeMode = valueChangeMode;
	}

	@Override
	public ValueChangeMode getValueChangeMode() {
		return getState().valueChangeMode;
	}

	@Override
	public void setValueChangeTimeout(final int valueChangeTimeout) {
		getState().valueChangeTimeout = valueChangeTimeout;

	}

	@Override
	public int getValueChangeTimeout() {
		return getState().valueChangeTimeout;
	}

	@Override
	public void setValue(final String value) {
		setValue(value, false);

	}

	protected void setValue(final String value, final boolean userOriginated) {
		String oldValue = this.value;
		this.value = value;
		if (!Objects.equals(oldValue, value)) {
			getState().value = value;
			for (ValueChangeListener<String> l : valueChangeListeners) {
				l.valueChange(new ValueChangeEvent<String>(this, oldValue, userOriginated));
			}
		}
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public Registration addValueChangeListener(final ValueChangeListener<String> listener) {
		valueChangeListeners.add(listener);
		return () -> valueChangeListeners.remove(listener);
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return super.isReadOnly();
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		this.setReadOnly(readOnly ? ReadOnly.TRUE : ReadOnly.FALSE);
	}
	public void setReadOnly(final ReadOnly readOnly) {
		getState().readOnly = readOnly;
	}	

	@Override
	public boolean isRequiredIndicatorVisible() {
		// TODO Auto-generated method stub
		return super.isRequiredIndicatorVisible();
	}

	@Override
	public void setRequiredIndicatorVisible(final boolean visible) {
		// TODO Auto-generated method stub
		super.setRequiredIndicatorVisible(visible);
	}

	@Override
	protected CodeMirrorState getState() {
		return (CodeMirrorState) super.getState();
	}

	@Override
	protected CodeMirrorState getState(final boolean markAsDirty) {
		return (CodeMirrorState) super.getState(markAsDirty);
	}
	
	public boolean isLineNumbers() {
		return getState(false).lineNumbers;
	}
	public void setLineNumbers(final boolean lineNumbers) {
		getState().lineNumbers = lineNumbers;
	}
	public boolean isLineWrapping() {
		return getState(false).lineWrapping;
	}
	public void setLineWrapping(final boolean lineWrapping) {
		getState().lineWrapping = lineWrapping;
	}
	public boolean isFoldGutter() {
		return getState(false).foldGutter;
	}
	public void setFoldGutter(final boolean foldGutter) {
		getState().foldGutter = foldGutter;
	}

    public void addGutter(final String gutter) {
        getState().gutters.add(gutter);
    }

    public void removeGutter(final String gutter) {
    	getState().gutters.remove(gutter);
    }

    public List<String> getGutters() {
        return new ArrayList<>(getState().gutters);
    }
}
