package r01ui.base.components.valueselector;

import com.vaadin.ui.CheckBox;

import r01f.ui.vaadin.styles.VaadinValoTheme;

public class VaadinChekWithHtml
	 extends CheckBox {

	private static final long serialVersionUID = 5187667772147179595L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinChekWithHtml(final String description,
							  final String captionHtml) {
		super();
		this.setCaptionAsHtml(true);
		this.setCaption(captionHtml);
		this.setDescription(description);
		this.addStyleName(VaadinValoTheme.CHECK_WITH_ICON);
		this.setResponsive(true);
	}
	public VaadinChekWithHtml(final String description,
							  final String captionHtml,
							  final Boolean initialState) {
		super();
		this.setCaptionAsHtml(true);
		this.setCaption(captionHtml);
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
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////

}
