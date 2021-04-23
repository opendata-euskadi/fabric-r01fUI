package r01ui.base.components.grid;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Operation button with styles for use in {@link Grid}, {@link VaadinCRUDGridBase} (eg: edit, copy, delete...). 
 * It can be used only to show a icon in a grid column.  
 * <pre>
 * 		+-----+-----+-----+------------------------+
 * 		| col | col | col | ...  [button] [button] |
 * 		| col | col | col | ...  [button] [button] |
 * 		| col | col | col | ...  [button] [button] |
 * 		+-----+-----+-----+------------------------+
 * </pre>
 */
public class VaadinGridButton 
	 extends Button {	
	
	private static final long serialVersionUID = 2871532213852151898L;		
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinGridButton(final String description, 
							final Resource icon,
							final ClickListener listener) {
		super(icon, 
			  listener);
		this.setDescription(description);
		this.setStyleName(ValoTheme.BUTTON_BORDERLESS);		
		this.setSizeFull();
	}
	
	public VaadinGridButton(final String description,
							final Resource icon,final  ClickListener listener,
							final String... styles) {
		this(description, 
			 icon, 
			 listener);
		this.addStyleNames(styles);
	}	
	
	public VaadinGridButton(final String description,
							final  Resource icon) {
		super(icon);
		this.setDescription(description);
		this.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);	
	}
}