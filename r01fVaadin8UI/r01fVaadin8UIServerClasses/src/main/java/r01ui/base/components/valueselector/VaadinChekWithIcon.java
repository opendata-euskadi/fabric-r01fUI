package r01ui.base.components.valueselector;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;

import r01f.ui.vaadin.styles.VaadinValoTheme;

public class VaadinChekWithIcon
	 extends CheckBox {

	private static final long serialVersionUID = -813137874480690299L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinChekWithIcon(final VaadinIcons icon,
						      final String description) {
		super();
		this.setCaptionAsHtml(true);
		this.setCaption(icon.getHtml());
		this.setDescription(description);
		this.addStyleName(VaadinValoTheme.CHECK_WITH_ICON);
		this.setResponsive(true);
	}
	public VaadinChekWithIcon(final VaadinIcons icon,
						     final String description,
						     final Boolean initialState) {
		super();
		this.setCaptionAsHtml(true);
		this.setCaption(icon.getHtml());
		this.setDescription(description);
		this.addStyleName(VaadinValoTheme.CHECK_WITH_ICON);
		this.setValue(initialState);
		this.setResponsive(true);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public float getWidth() {
		return -1;	// force unknown width
	}
}
