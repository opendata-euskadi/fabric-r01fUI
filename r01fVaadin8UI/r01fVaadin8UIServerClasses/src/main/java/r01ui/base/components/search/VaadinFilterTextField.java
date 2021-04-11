package r01ui.base.components.search;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import r01f.ui.i18n.UII18NService;
import r01f.util.types.Strings;

/**
 * A text filter component like:
 * <pre>
 * 		[..........][filter][clear]
 * </pre>
 */
public class VaadinFilterTextField 
     extends CustomField<String> {

	private static final long serialVersionUID = 8276710095246102703L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	private final TextField _txtLabelFilter;	// Filter text box
	private final Button _btnFilter;			// Search button
	private final Button _btnClearFilter;		// Clear filter button
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinFilterTextField(final UII18NService i18n) {
		////////// UI
		_txtLabelFilter = new TextField();
		
		_btnFilter = new Button(i18n.getMessage("filter"),VaadinIcons.FILTER);
		_btnClearFilter = new Button(i18n.getMessage("clear"),VaadinIcons.ERASER);
		
		// style
		this.setDefaultStyling();
		
		_enableButtonsDependingOnValue("");
		
		////////// Behavior
		_setBehavior();
	}
	@Override
	protected Component initContent() {
		HorizontalLayout ly = new HorizontalLayout(_txtLabelFilter,_btnFilter,_btnClearFilter);
		return ly;
	}
	private void _setBehavior() {
		// when the text changes enable / disable the [filter] button
		_txtLabelFilter.addValueChangeListener(valChangeEvent -> {
													String val = valChangeEvent.getValue();
													
													// if no text simulate a click to the filter button
													// beware! DO NOT MOVE since _btnFilter.click() DOES NOT work if the button is disabled 
													if (Strings.isNullOrEmpty(val)) _btnFilter.click();
													
													// disable the [filter] button if val is empty
													_enableButtonsDependingOnValue(val);												
											   });
		
		// when clicking the [clear] button 
		_btnClearFilter.addClickListener(clickEvent -> {
												_txtLabelFilter.setValue("");
										 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALUE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getValue() {
		return _txtLabelFilter.getValue();
	}
	@Override
	protected void doSetValue(final String value) {
		_txtLabelFilter.setValue(value);
		_enableButtonsDependingOnValue(value);
	}
	private void _enableButtonsDependingOnValue(final String value) {
		// enable / disable depending on the value
		_btnFilter.setEnabled(Strings.isNOTNullOrEmpty(value));
		_btnClearFilter.setEnabled(Strings.isNOTNullOrEmpty(value));		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS
/////////////////////////////////////////////////////////////////////////////////////////
	public Registration addFilterClickListener(final Button.ClickListener clickEventListener) {
		return _btnFilter.addClickListener(clickEventListener);
	}
	public Registration addClearFilterClickListener(final Button.ClickListener clickEventListener) {
		return _btnClearFilter.addClickListener(clickEventListener);
	}
	@Override
	public Registration addValueChangeListener(final ValueChangeListener<String> listener) {
		return _txtLabelFilter.addValueChangeListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void setFilterButtonCaption(final String text) {
		_btnFilter.setCaption(text);
	}
	public void setClearFilterCaption(final String text) {
		_btnClearFilter.setCaption(text);
	}
	public void setFilterButtonIcon(final Resource icon) {
		_btnFilter.setIcon(icon);
	}
	public void setClearFilterButtonIcon(final Resource icon) {
		_btnClearFilter.setIcon(icon);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	STYLE
/////////////////////////////////////////////////////////////////////////////////////////	
	public void setDefaultStyling() {
		// text field
		_txtLabelFilter.setIcon(VaadinIcons.SEARCH);
		_txtLabelFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		_txtLabelFilter.setWidthFull();
		
		// buttons
		_btnFilter.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,
								 ValoTheme.BUTTON_ICON_ONLY,
								 ValoTheme.BUTTON_TINY);
		_btnClearFilter.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,
								 	  ValoTheme.BUTTON_ICON_ONLY,
								 	  ValoTheme.BUTTON_TINY);
	}
	public void addFilterTextFieldStyleNames(final String... styles) {
		_txtLabelFilter.addStyleNames(styles);
	}
	public void addFilterButtonStyleNames(final String... styles) {
		_btnFilter.addStyleNames(styles);
	}
	public void addClearFilterButtonStyleNames(final String...styles) {
		_btnClearFilter.addStyleNames(styles);
	}
}
