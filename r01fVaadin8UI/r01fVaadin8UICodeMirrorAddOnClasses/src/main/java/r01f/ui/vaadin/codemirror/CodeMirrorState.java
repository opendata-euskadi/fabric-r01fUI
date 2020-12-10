package r01f.ui.vaadin.codemirror;

import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.shared.ui.ValueChangeMode;

public class CodeMirrorState extends JavaScriptComponentState {

	public ValueChangeMode valueChangeMode;
	public int valueChangeTimeout;
	public String value;
	public String mode;
	public int indentUnit;
	public boolean lineNumbers;
	public boolean lineWrapping;
	public boolean foldGutter;
	public List<String> gutters;
	public ReadOnly readOnly;

}

