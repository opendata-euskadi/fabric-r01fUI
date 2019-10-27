package r01ui.base.components.text;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Creates an horizontal labeled text like
 * <pre>
 * 		[Label] [text]
 * </pre>@author adminw7local
 */
public class VaadinHLabeledText 
	 extends VerticalLayout
  implements VaadinLabeledText {

	private static final long serialVersionUID = 5756703218221304345L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELD                                                            
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinLabeledTextDelegate _delegated;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHLabeledText(final String label,final String text) {
		_delegated = new VaadinLabeledTextDelegate(this);
		this.addComponents(// the label
						   new Label(VaadinLabeledTextDelegate.createLabelValue(label), 
						  			 ContentMode.HTML),
						   // the text
						   new Label(text));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS                                                                        
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Label getLabel() {
		return _delegated.getLabel();
	}
	@Override
	public Label getText() {
		return _delegated.getText();
	}
	@Override
	public void setLabel(final String label) {
		_delegated.setLabel(label);
	}
	@Override
	public void setText(final String text) {
		_delegated.setText(text);
	}
}
