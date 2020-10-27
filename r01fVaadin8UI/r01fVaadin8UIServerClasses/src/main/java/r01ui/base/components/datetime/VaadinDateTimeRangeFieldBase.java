package r01ui.base.components.datetime;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import com.google.common.collect.Range;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractDateField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.HorizontalLayout;

import r01f.patterns.Factory;
import r01f.util.types.Ranges;

/**
 * A vaadin date range selector like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++
 * 		+ [ temporal   \/] - [ temporal   \/] +
 * 		+++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
abstract class VaadinDateTimeRangeFieldBase<T extends Temporal & TemporalAdjuster & Serializable & Comparable<? super T>,
											R extends Enum<R>,
											C extends AbstractDateField<T,R>>
	   extends CustomField<Range<T>> {

	private static final long serialVersionUID = -4908712937810703202L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final C _dateLowerBound;
	protected final C _dateUperBound;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDateTimeRangeFieldBase(final Factory<C> compFactory) {
		_dateLowerBound = compFactory.create();
		_dateUperBound = compFactory.create();
	}
	public VaadinDateTimeRangeFieldBase(final Factory<C> compFactory,
										final String dateFormat) {
		_dateLowerBound = compFactory.create();
		_dateUperBound = compFactory.create();
		_dateLowerBound.setDateFormat(dateFormat);
		_dateUperBound.setDateFormat(dateFormat);
	}
	public VaadinDateTimeRangeFieldBase(final Factory<C> compFactory,
										final R dateResolution) {
		this(compFactory);
		_dateLowerBound.setResolution(dateResolution);
		_dateUperBound.setResolution(dateResolution);
	}
	public VaadinDateTimeRangeFieldBase(final Factory<C> compFactory,
										final R dateResolution,
										final String dateFormat) {
		this(compFactory);
		_dateLowerBound.setResolution(dateResolution);
		_dateUperBound.setResolution(dateResolution);
		_dateLowerBound.setDateFormat(dateFormat);
		_dateUperBound.setDateFormat(dateFormat);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		////////// Behavior
		// set the upper bound to be at least the lower bound
		_dateLowerBound.addValueChangeListener(valChangeEvent -> {
													T currLow = valChangeEvent.getValue();
													T currUp = _dateUperBound.getValue();
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_dateUperBound.setValue(null);
													}
													//do not change the range if it is a DateTimeRangeComponent
													if (!_isDateTimeRangeComponent()) {
														_dateUperBound.setRangeStart(currLow);
														_dateUperBound.setRangeEnd(null);
													}
											   });
		_dateUperBound.addValueChangeListener(valChangeEvent -> {
													T currLow = _dateLowerBound.getValue();
													T currUp = valChangeEvent.getValue();
													if (currLow != null && currUp != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_dateLowerBound.setValue(null);
													}
													//do not change the range if it is a DateTimeRangeComponent
													if (!_isDateTimeRangeComponent()) {
														_dateLowerBound.setRangeStart(null);
														_dateLowerBound.setRangeEnd(currUp);
													}
											  });
		////////// Layout
		HorizontalLayout hly = new HorizontalLayout(_dateLowerBound,
													_dateUperBound);
		return hly;
	}
	@Override
	public Range<T> getValue() {
		T low = _dateLowerBound.getValue();
		T up = _dateUperBound.getValue();
		return low == null && up == null ? null
										 : ((low != null && up != null) && _isNotValidRange(low, up)) ? null							//if values are not valid, do not create the Range
												 											   		: Ranges.guavaRangeFrom(low,up);
	}
	@Override
	protected void doSetValue(final Range<T> value) {
		if (value == null) {
			_dateLowerBound.setValue(null);
			_dateUperBound.setValue(null);
			return;
		};

		if (value.hasLowerBound()) {
			_dateLowerBound.setValue(value.lowerEndpoint());
		} else {
			_dateLowerBound.setValue(null);
		}

		if (value.hasUpperBound()) {
			_dateUperBound.setValue(value.upperEndpoint());
		} else {
			_dateUperBound.setValue(null);
		}
	}

	@Override
	public void clear() {
		_dateLowerBound.setValue(null);
		_dateUperBound.setValue(null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<Range<T>> listener) {
		Registration reg = super.addValueChangeListener(listener);

		// Raise an event when either the [lower bound] or [upper bound] change
		_dateLowerBound.addValueChangeListener(valChangeEvent -> {
													if (_isDateTimeRangeComponent() 
													 && _dateUperBound.getValue() != null && valChangeEvent.getValue() != null
													 && _isNotValidRange(valChangeEvent.getValue(),
															 			 _dateUperBound.getValue())) { // is not a valid range
														// if DateTimeField and up is before low, just set up to null
														return;
													}
													T currLow = _isDateTimeRangeComponent() 
																	&& valChangeEvent.getValue() == null ? null 							// if DateTimeField and value is null, just set the new value to low
																										 : valChangeEvent.getOldValue();	// old value
													T currUp = _dateUperBound.getValue();
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														currLow = null;
													}
													Range<T> val = Ranges.guavaRangeFrom(currLow,
																						 currUp);
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											val,
																											true);		// user originated
													listener.valueChange(evt);
											   });
		_dateUperBound.addValueChangeListener(valChangeEvent -> {
													if (_isDateTimeRangeComponent() 
													 && _dateLowerBound.getValue() != null 
													 && valChangeEvent.getValue() != null
													 && _isNotValidRange(_dateLowerBound.getValue(),
															 			 valChangeEvent.getValue()))  { // is not a valid range
														//if DateTimeField and low is after up, just set low to null
														return;
													}
													T currLow = _dateLowerBound.getValue();
													T currUp = _isDateTimeRangeComponent() 
																	&& valChangeEvent.getValue()==null ? null							// if DateTimeField and value is null, just set the new value to up
																									   : valChangeEvent.getOldValue();	// old value
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														currUp = null;
													}
													Range<T> val = Ranges.guavaRangeFrom(currLow,
																						 currUp);
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											val,
																											true);		// user originated
													listener.valueChange(evt);
											  });
		return reg;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public R getResolution() {
		return _dateLowerBound.getResolution();	// both lower or upper has the same resolution
	}
	public void setResolution(final R res) {
		_dateLowerBound.setResolution(res);
		_dateUperBound.setResolution(res);
	}
	public void setDateFormat(final String dateFormat) {
		_dateLowerBound.setDateFormat(dateFormat);
		_dateUperBound.setDateFormat(dateFormat);	
	}
	public void setDateOutOfRangeMessage(final String dateOutOfRangeMessage) {
		_dateLowerBound.setDateOutOfRangeMessage(dateOutOfRangeMessage);
		_dateUperBound.setDateOutOfRangeMessage(dateOutOfRangeMessage);
	}
	public void setParseErrorMessage(final String parsingErrorMessage) {
		_dateLowerBound.setParseErrorMessage(parsingErrorMessage);
		_dateUperBound.setParseErrorMessage(parsingErrorMessage);		
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	private boolean _isNotValidRange(final T currLow,
								  final T currUp) {
		if (_isDateTimeRangeComponent()) {
			return currLow.get(ChronoField.HOUR_OF_DAY) > (currUp.get(ChronoField.HOUR_OF_DAY));
		} else {
			return currLow.compareTo(currUp) > 0;
		}
	}
	private boolean _isDateTimeRangeComponent() {
		return _dateUperBound instanceof DateTimeField &&
			   _dateLowerBound instanceof DateTimeField ;
	}
}