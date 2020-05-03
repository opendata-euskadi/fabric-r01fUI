package r01ui.base.components.datetime;

import java.time.LocalDate;

import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.DateField;

/**
 * A vaadin date range selector like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++
 * 		+ [ lower date \/] - [ upper date \/] +
 * 		+++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinDateRangeComponent 
	 extends VaadinDateTimeRangeComponentBase<LocalDate,DateResolution,
	 										  DateField> {

	private static final long serialVersionUID = -4908712937810703202L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDateRangeComponent() {
		super(DateField::new);
	}
}
