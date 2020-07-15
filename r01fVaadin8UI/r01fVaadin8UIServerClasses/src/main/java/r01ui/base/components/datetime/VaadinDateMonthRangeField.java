package r01ui.base.components.datetime;

import java.time.LocalDate;

import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.DateField;

public class VaadinDateMonthRangeField  
	 extends VaadinDateTimeRangeFieldBase<LocalDate,DateResolution,
	 										  DateField> {

	private static final long serialVersionUID = 4401268432891162533L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDateMonthRangeField() {
		super(DateField::new,
			  DateResolution.MONTH);
	}
	public VaadinDateMonthRangeField(final String dateFormat) {
		super(DateField::new,
				DateResolution.MONTH,
				dateFormat);
	}

}
