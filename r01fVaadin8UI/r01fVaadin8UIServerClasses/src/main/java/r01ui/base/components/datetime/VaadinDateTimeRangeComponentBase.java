package r01ui.base.components.datetime;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Collection;

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
abstract class VaadinDateTimeRangeComponentBase<T extends Temporal & TemporalAdjuster & Serializable & Comparable<? super T>,
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
	public VaadinDateTimeRangeComponentBase(final Factory<C> compFactory) {
		_dateLowerBound = compFactory.create();
		_dateUperBound = compFactory.create();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		////////// Behavior
		// set the upper bound to be at least the lower bound
		_dateLowerBound.addValueChangeListener(e -> {
													T currLow = e.getValue();
													T currUp = _dateUperBound.getValue();
													if (currUp != null && currLow != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_dateUperBound.setValue(null);
													}
													//do not change the range if it is a DateTimeRangeComponent
													if(!_isDateTimeRangeComponent()) {
														_dateUperBound.setRangeStart(currLow);
														_dateUperBound.setRangeEnd(null);
													}
											   });
		_dateUperBound.addValueChangeListener(e -> {
													T currLow = _dateLowerBound.getValue();
													T currUp = e.getValue();
													if (currLow != null && currUp != null
													 && _isNotValidRange(currLow,currUp)) {		// is not a valid range
														_dateLowerBound.setValue(null);
													}
													//do not change the range if it is a DateTimeRangeComponent
													if(!_isDateTimeRangeComponent()) {
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
										 : ((low != null && up != null) && _isNotValidRange(low, up)) ? null							//if values are not valid, do not create de Range
												 											   		: Ranges.guavaRangeFrom(low,up);
	}
	@Override
	protected void doSetValue(final Range<T> value) {
		if (value == null) return;

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
		
		Collection<?> registeredEventListeners = this.getListeners(ValueChangeEvent.class);
	}
	/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<Range<T>> listener) {
		Registration reg = super.addValueChangeListener(listener);

		// Raise an event when either the [lower bound] or [upper bound] change
		_dateLowerBound.addValueChangeListener(e -> {
													if(_isDateTimeRangeComponent() && _dateUperBound.getValue()!=null && e.getValue()!=null
																&& _isNotValidRange(e.getValue(),_dateUperBound.getValue())) { // is not a valid range
														//if DateTimeField and up is before low, just set up to null
														return;
													}
													Range<T> val = Ranges.guavaRangeFrom(_isDateTimeRangeComponent() && e.getValue()==null ? null 				//if DateTimeField and value is null, just set the new value to low
																																		   : e.getOldValue(),	// old value
																					     _dateUperBound.getValue());
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											val,
																											true);		// user originated
													listener.valueChange(evt);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													if(_isDateTimeRangeComponent() && _dateLowerBound.getValue()!=null && e.getValue()!=null
																&& _isNotValidRange(_dateLowerBound.getValue(),e.getValue()))  { // is not a valid range
														//if DateTimeField and low is after up, just set low to null
														return;
													}
													Range<T> val = Ranges.guavaRangeFrom(_dateLowerBound.getValue(),
																					     _isDateTimeRangeComponent() && e.getValue()==null ? null				//if DateTimeField and value is null, just set the new value to up
																					    												   : e.getOldValue());	// old value
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
///////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	private boolean _isNotValidRange(T currLow,
								  T currUp) {
		if(_isDateTimeRangeComponent()) {
			return currLow.get(ChronoField.HOUR_OF_DAY) > (currUp.get(ChronoField.HOUR_OF_DAY));
		}else {
			return currLow.compareTo(currUp) > 0;
		}
	}
	private boolean _isDateTimeRangeComponent() {
		return _dateUperBound instanceof DateTimeField &&
			   _dateLowerBound instanceof DateTimeField ;
	}
}