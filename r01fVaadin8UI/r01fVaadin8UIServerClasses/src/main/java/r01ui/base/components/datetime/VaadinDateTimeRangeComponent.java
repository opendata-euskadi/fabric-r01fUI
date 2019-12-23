package r01ui.base.components.datetime;

import java.time.LocalDateTime;

import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.DateTimeField;

/**
 * A vaadin date range selector like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++
 * 		+ [ lower hour \/] - [ upper hour \/] +
 * 		+++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinDateTimeRangeComponent 
	 extends VaadinDateTimeRangeComponentBase<LocalDateTime,DateTimeResolution,
	 										  DateTimeField> {

	private static final long serialVersionUID = -4908712937810703202L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDateTimeRangeComponent() {
		super(DateTimeField::new);	// component factory
		
		// BEWARE!! add these styles to the app stylesheet
		//	    .datetimepicker-time-only .v-inline-datefield-calendarpanel-header,
		//	    .datetimepicker-time-only .v-inline-datefield-calendarpanel-body {
		//	        display: none;
		//	    }
		//	    .datetimepicker-time-only .v-datefield-calendarpanel-header,
		//	    .datetimepicker-time-only .v-datefield-calendarpanel-body {
		//	        display: none;
		//	    }
		_dateUperBound.addStyleName("datetimepicker-time-only");
		_dateLowerBound.addStyleName("datetimepicker-time-only");
		
		_dateUperBound.setDateFormat("HH:mm");
		_dateLowerBound.setDateFormat("HH:mm");
	}
}
