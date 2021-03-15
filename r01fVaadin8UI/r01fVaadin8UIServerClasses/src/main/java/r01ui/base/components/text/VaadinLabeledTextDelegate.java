package r01ui.base.components.text;

import com.google.common.collect.Iterables;
import com.vaadin.ui.Label;

import r01f.util.types.Strings;

/**
 * Delegate type for labeled text (horizontal or vertical) like
 * <pre>
 * 		[Label] 
 * 		[text]
 * </pre>
 * or
 * <pre>
 * 		[label][text]
 * </pre>
 */
class VaadinLabeledTextDelegate {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELD                                                            
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinLabeledText _delegated;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLabeledTextDelegate(final VaadinLabeledText delegated) {
		_delegated = delegated;
		
		delegated.setMargin(false);
		_delegated.setSpacing(false);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public Label getLabel() {
		return (Label)Iterables.get(_delegated,0);	// get the first component
	}
	public Label getText() {
		return (Label)Iterables.get(_delegated,1);	// get the second component
	}
	public void setLabel(final String label) {
		this.getLabel()
			.setValue(VaadinLabeledTextDelegate.createLabelValue(label));
	}
	public void setText(final String text) {
		this.getText()
			.setValue(text);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	static String createLabelValue(final String label) {
		return Strings.customized("<strong>{}</strong>",
								  label);
	}
}
