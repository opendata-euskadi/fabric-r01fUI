package r01ui.base.components.valueselector;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.util.types.StringSplitter;
import r01f.util.types.Strings;

@Accessors(prefix="_")
public class VaadinValueSelectorComponent<T> 
	 extends CustomField<T> {

	private static final long serialVersionUID = 5420720079052991200L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String BTN_SELECTED_STYLE = "danger";
	/**
	 * The null value representation
	 */
	@Getter private final T _nullValue;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	private final HorizontalLayout _hlyValueSelectors;
	private final ComboBox<T> _cmbValue;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinValueSelectorComponent(final T nullValue) {
		_nullValue = nullValue;
		
		////////// create components
		_hlyValueSelectors = new HorizontalLayout();
		_cmbValue = new ComboBox<T>();
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue) {
		this(nullValue);
		this.setCaption(caption);
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final Collection<T> values) {
		this(caption,
			 nullValue);
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final T... values) {
		this(caption,
			 nullValue);
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final T... values) {
		this(caption,
			 nullValue);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final Collection<T> values) {
		this(caption,
			 nullValue);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	public VaadinValueSelectorComponent(final T nullValue,
										final Collection<T> values) {
		this(nullValue);
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final T nullValue,
										final T... values) {
		this(nullValue);
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final T... values) {
		this(nullValue);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	public VaadinValueSelectorComponent(final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final Collection<T> values) {
		this(nullValue);
		this.setValues(itemCaptionGenerator,
					   values);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected Component initContent() {
		////////// layout
		VerticalLayout vly = new VerticalLayout();
		vly.addComponents(_hlyValueSelectors,
						  _cmbValue);
		return vly;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALUE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public T getValue() {
		return _cmbValue.getValue();
	}
	@Override
	protected void doSetValue(final T value) {
		// just set the combo value: this will fire the buttons value change
		if (value != null 
		 && value.toString().equals(_nullValue.toString())) {		// WTF!
			_cmbValue.setValue(null);
		} else {
			_cmbValue.setValue(value);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder 
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<T> listener) {
		Registration reg = super.addValueChangeListener(listener);
		
		// raise an event when the combo value changes
		_cmbValue.addValueChangeListener(e -> {
											T oldVal = e.getOldValue();
											ValueChangeEvent<T> evt = new ValueChangeEvent<>(this,		// component
																							 oldVal,		// old value
																							 true);		// user originated
											listener.valueChange(evt);			
										 });
		return reg;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COMBO                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public void showCombo() {
		_cmbValue.setVisible(true);
	}
	public void hideCombo() {
		_cmbValue.setVisible(false);
	}
	public boolean isComboVisible() {
		return _cmbValue.isVisible();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTIONS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public void setItemCaptionGenerator(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_cmbValue.setItemCaptionGenerator(itemCaptionGenerator);
		_buttonIterator().forEachRemaining(btn -> btn.setDescription(itemCaptionGenerator.apply((T)btn.getData())));	// remember tha button vaadin's DATA object contains the VALUE
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public void setValues(final T... values) {
		this.setValues(Lists.<T>newArrayList(values));
	}
	public void setValues(final Collection<T> values) {
		this.setValues(null,// no item caption generator > use the value
					   values);
	}
	@SuppressWarnings("unchecked")
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final T... values) {
		this.setValues(Lists.<T>newArrayList(values));
	}
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final Collection<T> values) {
		ItemCaptionGenerator<T> theItemCaptionGen = itemCaptionGenerator != null
														? itemCaptionGenerator
														: val -> val.toString();
		_replaceValueButtons(values,
							 theItemCaptionGen);
	}
	@SuppressWarnings("unchecked")
	private void _replaceValueButtons(final Collection<T> values,
									  final ItemCaptionGenerator<T> itemCaptionGen) {
		// [1] - Remove all components
		_hlyValueSelectors.removeAllComponents();
		
		// [2] - Create buttons
		values.stream()
			  .forEach(val -> {
							Button btnVal = new Button(val.toString());
							btnVal.setData(val);	// BEWARE!! store the val as data
							btnVal.addClickListener(// when clicking a [button] select the corresponding [combo item]
													e -> {
														// [1] - If there exists an already selected button, unselect it
														Button btnPrev = _findSelectedValueButton();
														if (btnPrev != null) btnPrev.removeStyleName(BTN_SELECTED_STYLE);
														
														// [2] - Select the button
														Button btn = e.getButton();		// should be btnVal
														if (btn == null) throw new IllegalStateException();
														btn.addStyleName(BTN_SELECTED_STYLE);
														
														// [3] - Select the corresponding [combo item]
														_cmbValue.setValue((T)btn.getData());
												    });
							btnVal.setDescription(itemCaptionGen.apply(val));
				  			_hlyValueSelectors.addComponent(btnVal);
			  		   });
		// [3] - Populate the combo
		_cmbValue.setItems(values);
		_cmbValue.setItemCaptionGenerator(itemCaptionGen);
		_cmbValue.addValueChangeListener(// When selecting a [combo item], select the corresponding [button]
										 (final ValueChangeEvent<T> e) -> {
											// [1] - If there exists an already selected button, unselect it
											Button btnPrev = _findSelectedValueButton();
											if (btnPrev != null) btnPrev.removeStyleName(BTN_SELECTED_STYLE);
											
											// [2] - Select the new button
											T selectedValue = e.getValue();
											if (selectedValue != null												
											 && !_nullValue.toString().equals(selectedValue.toString())) {	// WTF!! 
												// a combo value is selected
												Button btn = _findValueButton(e.getValue());
												if (btn == null) throw new IllegalStateException();
												btn.addStyleName(BTN_SELECTED_STYLE);
											} else {
												// the combo has NOT a selected value
												_buttonIterator().forEachRemaining(btn -> removeStyleName(BTN_SELECTED_STYLE));
											}
										 });
	}
	private Button _findValueButton(final T val) {
		return _findValueButton(btn -> btn.getData().equals(val));
	}
	private Button _findSelectedValueButton() {
		return _findValueButton(btn -> {
									boolean outHasSelectedStyle = false;
									String style = btn.getStyleName();
									if (Strings.isNOTNullOrEmpty(style)) {
										outHasSelectedStyle = StringSplitter.using(Splitter.on(" "))
																		  	.at(style)
																		  	.indexOf(BTN_SELECTED_STYLE) >= 0;
									}
									return outHasSelectedStyle;
								});
	}
	private Button _findValueButton(final Predicate<Button> pred) {
		Button outButton = null;
		Iterator<Button> btnIt = _buttonIterator();
		do {
			Button btn = btnIt.hasNext() ? btnIt.next() : null;
			if (btn != null 
			 && pred.test(btn)) {
				outButton = btn;
				break;
			}
		} while(btnIt.hasNext() && outButton == null);
		return outButton;
	}
	private Iterator<Button> _buttonIterator() {
		Iterator<Component> btnIt = _hlyValueSelectors.iterator();
		return Iterators.transform(btnIt,
								   comp -> (Button)comp);
	}
}
