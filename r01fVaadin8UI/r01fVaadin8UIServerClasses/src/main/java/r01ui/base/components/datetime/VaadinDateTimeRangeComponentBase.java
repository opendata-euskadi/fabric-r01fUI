package r01ui.base.components.datetime;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import com.google.common.collect.Range;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractDateField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
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
													 && currUp.compareTo(currLow) < 0) {		// is before
														_dateUperBound.setValue(null);
													}
													_dateUperBound.setRangeStart(currLow);
													_dateUperBound.setRangeEnd(null);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													T currLow = _dateLowerBound.getValue();
													T currUp = e.getValue();
													if (currLow != null && currUp != null
													 && currLow.compareTo(currUp) > 0) {		// is after
														_dateLowerBound.setValue(null);
													}
													_dateLowerBound.setRangeStart(null);
													_dateLowerBound.setRangeEnd(currUp);
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
		return Ranges.guavaRangeFrom(low,up);
	}
	@Override
	protected void doSetValue(final Range<T> value) {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder 
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<Range<T>> listener) {
		Registration reg = super.addValueChangeListener(listener);
		
		// Raise an event when either the [lower bound] or [upper bound] change
		_dateLowerBound.addValueChangeListener(e -> {
													Range<T> oldVal = Ranges.guavaRangeFrom(e.getOldValue(),_dateUperBound.getValue());
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											oldVal,		// old value
																											true);		// user originated
													listener.valueChange(evt);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													Range<T> oldVal = Ranges.guavaRangeFrom(_dateLowerBound.getValue(),e.getOldValue());
													ValueChangeEvent<Range<T>> evt = new ValueChangeEvent<>(this,		// component
																											oldVal,		// old value
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
}
