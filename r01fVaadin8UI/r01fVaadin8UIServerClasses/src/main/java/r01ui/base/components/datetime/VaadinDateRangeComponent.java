package r01ui.base.components.datetime;

import java.time.LocalDate;

import com.google.common.collect.Range;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;

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
		////////// Layout
		HorizontalLayout hly = new HorizontalLayout(_dateLowerBound,
													_dateUperBound);
		return hly;
	}
	@Override
	public Range<LocalDate> getValue() {
		LocalDate low = _dateLowerBound.getValue();
		LocalDate up = _dateUperBound.getValue();
		return _composeRange(low,up);
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
													Range<LocalDate> oldVal = _composeRange(e.getOldValue(),_dateUperBound.getValue());
													ValueChangeEvent<Range<LocalDate>> evt = new ValueChangeEvent<>(this,		// component
																													oldVal,		// old value
																													true);		// user originated
													listener.valueChange(evt);
											   });
		_dateUperBound.addValueChangeListener(e -> {
													Range<LocalDate> oldVal = _composeRange(_dateLowerBound.getValue(),e.getOldValue());
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
	private static Range<LocalDate> _composeRange(final LocalDate low,final LocalDate up) {
		Range<LocalDate> outRange = null;
		if (low != null && up != null) {
			outRange = Range.closed(low,up);
		} else if (low != null && up == null) {
			outRange = Range.atLeast(low);
		} else if (low == null && up != null) {
			outRange = Range.atMost(up);
		} else {
			outRange = Range.all();
		}
		return outRange;
	}
}
