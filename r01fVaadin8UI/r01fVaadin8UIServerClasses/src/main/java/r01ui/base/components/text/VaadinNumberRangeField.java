package r01ui.base.components.text;

import com.google.common.collect.Range;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import r01f.patterns.FactoryFrom;
import r01f.ui.vaadin.VaadinEvents;
import r01f.util.types.Numbers;
import r01f.util.types.Ranges;
import r01f.util.types.Strings;

/**
 * A vaadin number range selector like:
 * <pre>
 * 		+=================================+
 * 		| [ number   \/] - [ number   \/] |
 * 		+=================================+
 * </pre>
 */
public class VaadinNumberRangeField<T extends Number & Comparable<T>>
	 extends CustomField<Range<T>> {

	private static final long serialVersionUID = -4007237796279851244L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final TextField _txtLowerBound;
	protected final TextField _txtUpperBound;
	
	protected final FactoryFrom<String,T> _numberFromStringFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinNumberRangeField(final FactoryFrom<String,T> numberFromStringFactory) {
		_txtLowerBound = new TextField();
		_txtUpperBound = new TextField();
		
		_numberFromStringFactory = numberFromStringFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		////////// Behavior
		// only allow numbers (do NOT move! these event listeners MUST be the first)
		_txtLowerBound.addValueChangeListener(VaadinEvents.createOnlyNumbersTextFieldValueChangeListener(_txtLowerBound));
		_txtUpperBound.addValueChangeListener(VaadinEvents.createOnlyNumbersTextFieldValueChangeListener(_txtUpperBound));
		
		// set the upper bound to be at least the lower bound
		_txtLowerBound.addValueChangeListener(valChangeEvent -> {
													T currLow = _fromString(valChangeEvent.getValue());
													T currUp = this.getUpperBound();
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_txtUpperBound.setValue(null);
													}
											   });
		_txtUpperBound.addValueChangeListener(valChangeEvent -> {
													T currLow = this.getLowerBound();
													T currUp = _fromString(valChangeEvent.getValue());
													if (currLow != null && currUp != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_txtLowerBound.setValue(null);
													}
											  });
		////////// Layout
		HorizontalLayout hly = new HorizontalLayout(_txtLowerBound,
													_txtUpperBound);
		return hly;
	}
	@Override
	public Range<T> getValue() {
		T low = this.getLowerBound();
		T up = this.getUpperBound();
		return low == null && up == null ? null
										 : ((low != null && up != null) && _isNotValidRange(low, up)) ? null							//if values are not valid, do not create the Range
												 											   		  : Ranges.<T>guavaRangeFrom(low,up);
	}
	@Override
	protected void doSetValue(final Range<T> value) {
		if (value == null) {
			_txtLowerBound.setValue(null);
			_txtUpperBound.setValue(null);
			return;
		};

		if (value.hasLowerBound()) {
			_txtLowerBound.setValue(_toString(value.lowerEndpoint()));
		} else {
			_txtLowerBound.setValue(null);
		}

		if (value.hasUpperBound()) {
			_txtUpperBound.setValue(_toString(value.upperEndpoint()));
		} else {
			_txtUpperBound.setValue(null);
		}
	}
	@Override
	public void clear() {
		_txtLowerBound.setValue(null);
		_txtUpperBound.setValue(null);
	}
	public T getLowerBound() {
		return _fromString(_txtLowerBound.getValue());
	}
	public T getUpperBound() {
		return _fromString(_txtUpperBound.getValue());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
    public Registration addValueChangeListener(final ValueChangeListener<Range<T>> listener) {
		Registration reg = super.addValueChangeListener(listener);

		// Raise an event when either the [lower bound] or [upper bound] change
		_txtLowerBound.addValueChangeListener(valChangeEvent -> {
													T currLow = _fromString(valChangeEvent.getValue());
													T currUp = this.getUpperBound();
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														currLow = null;
													}
													Range<T> val = Ranges.<T>guavaRangeFrom(currLow,currUp);
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											val,
																											true);		// user originated
													listener.valueChange(evt);
											   });
		_txtUpperBound.addValueChangeListener(valChangeEvent -> {
													T currLow = this.getLowerBound();
													T currUp = _fromString(valChangeEvent.getValue());												
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														currUp = null;
													}
													Range<T> val = Ranges.<T>guavaRangeFrom(currLow,currUp);
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											val,
																											true);		// user originated
													listener.valueChange(evt);
											  });
		return reg;
    }
///////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	private T _fromString(final String str) {
		if (Strings.isNullOrEmpty(str) || !Numbers.isNumber(str)) return null;
		return _numberFromStringFactory.from(str);
	}
	private String _toString(final T val) {
		return val != null ? val.toString() : null;
	}
	private boolean _isNotValidRange(final T currLow,final T currUp) {
		return currLow.compareTo(currUp) > 0;
	}
}
