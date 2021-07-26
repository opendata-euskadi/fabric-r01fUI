package r01ui.base.components.button;

import com.vaadin.data.HasValue;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViews;

/**
 * A {@link HasValue} button wrapper like a toggle. It can be use as field and can be bind with {@link VaadinViews}
 * <pre>
 * 		+=======================+
 * 		+ Captino [on [x]  off] +
 * 		+=======================+
 * </pre>
 */
public class VaadinToggleButton 
	 extends CustomField<Boolean> {

	private static final long serialVersionUID = 2192600562645846577L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	private final ThemeResource _toggleOn = new ThemeResource("img/toggle-on.png");
	private final ThemeResource _toggleOff = new ThemeResource("img/toggle-off.png");
	private final Label _lblCaption = new Label();
	private final Button _btnToggle = new Button();
/////////////////////////////////////////////////////////////////////////////////////////
//	STATE (avoid as much as possible)
/////////////////////////////////////////////////////////////////////////////////////////	
	private boolean _value = false;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinToggleButton() {
		_lblCaption.addStyleName("v-caption");
		_btnToggle.addStyleName(VaadinValoTheme.BUTTON_ICON_ONLY);
		_btnToggle.addStyleName(VaadinValoTheme.BUTTON_BORDERLESS);
		_btnToggle.setIcon(_toggleOff);	
	}
	@Override
	protected Component initContent() {
		HorizontalLayout hly = new HorizontalLayout(_lblCaption,_btnToggle);
		hly.setComponentAlignment(_lblCaption,Alignment.BOTTOM_LEFT);
		hly.setMargin(false);
		return hly;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS
/////////////////////////////////////////////////////////////////////////////////////////	
	public void addClickListener(final ClickListener listener) {
		_btnToggle.addClickListener(clickEvent -> {
									this.setValue(!_value);
									if (listener != null)
										listener.buttonClick(clickEvent);
								 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTION
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setCaption(final String caption) {
		_lblCaption.setValue(caption);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALUE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Boolean getValue() {
		return _value;
	}
	@Override
	protected void doSetValue(final Boolean value) {
		this.toggle(value);
	}	
	public void toggle(final boolean value) {
		_value = value;
		if (_value) {
			_btnToggle.setIcon(_toggleOn);
		} else {
			_btnToggle.setIcon(_toggleOff);
		}
	}
	public void toggle() {
		this.toggle(!_value);
	}
}