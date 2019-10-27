package r01ui.base.components.text;

import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Layout.AlignmentHandler;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.SpacingHandler;

/**
 * interface for labeled text (horizontal or vertical) like
 * <pre>
 * 		[Label] 
 * 		[text]
 * </pre>
 * or
 * <pre>
 * 		[label][text]
 * </pre>
 */
public interface VaadinLabeledText 
	     extends Layout,
	     		 MarginHandler,SpacingHandler,AlignmentHandler {

/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public Label getLabel();
	public Label getText();
	public void setLabel(final String label);
	public void setText(final String text);
}
