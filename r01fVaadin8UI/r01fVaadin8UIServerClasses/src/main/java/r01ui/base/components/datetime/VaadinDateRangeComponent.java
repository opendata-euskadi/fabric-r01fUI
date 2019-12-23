package r01ui.base.components.datetime;

import java.time.LocalDate;

import com.google.common.collect.Range;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;

import r01f.util.types.Ranges;

/**
 * A vaadin date range selector like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++
 * 		+ [ lower date \/] - [ upper date \/] +
 * 		+++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinDateRangeComponent 
	 extends CustomField<Range<LocalDate>> {

	private static final long serialVersionUID = -4908712937810703202L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private final DateField _dateLowerBound = new DateField();
	private final DateField _dateUperBound = new DateField();
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		////////// Behavior
		// set the upper bound to be at least the lower bound
		_dateLowerBound.addValueChangeListener(e -> {
													LocalDate currLow = e.getValue();
													LocalDate currUp = _dateUperBound.getValue();
													if (currUp != null && currLow != null 
													 && currUp.isBefore(currLow)) {
														_dateUperBound.setValue(null);
													}
													_dateUperBound.setRangeStart(currLow);
													_dateUperBound.setRangeEnd(null);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													LocalDate currLow = _dateLowerBound.getValue();
													LocalDate currUp = e.getValue();
													if (currLow != null && currUp != null
													 && currLow.isAfter(currUp)) {
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
	public Range<LocalDate> getValue() {
		LocalDate low = _dateLowerBound.getValue();
		LocalDate up = _dateUperBound.getValue();
		return Ranges.guavaRangeFrom(low,up);
	}
	@Override
	protected void doSetValue(final Range<LocalDate> value) {
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
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<Range<LocalDate>> listener) {
		Registration reg = super.addValueChangeListener(listener);
		
		// Raise an event when either the [lower bound] or [upper bound] change
		_dateLowerBound.addValueChangeListener(e -> {
													Range<LocalDate> oldVal = Ranges.guavaRangeFrom(e.getOldValue(),_dateUperBound.getValue());
													ValueChangeEvent<Range<LocalDate>> evt = new ValueChangeEvent<>(this,		// component
																													oldVal,		// old value
																													true);		// user originated
													listener.valueChange(evt);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													Range<LocalDate> oldVal = Ranges.guavaRangeFrom(_dateLowerBound.getValue(),e.getOldValue());
													ValueChangeEvent<Range<LocalDate>> evt = new ValueChangeEvent<>(this,		// component
																													oldVal,		// old value
																													true);		// user originated
													listener.valueChange(evt);			
											  });
		return reg;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public DateResolution getResolution() {
		return _dateLowerBound.getResolution();	// both lower or upper has the same resolution
	}
	public void setResolution(final DateResolution res) {
		_dateLowerBound.setResolution(DateResolution.MONTH);
		_dateUperBound.setResolution(DateResolution.MONTH);
	}
}
