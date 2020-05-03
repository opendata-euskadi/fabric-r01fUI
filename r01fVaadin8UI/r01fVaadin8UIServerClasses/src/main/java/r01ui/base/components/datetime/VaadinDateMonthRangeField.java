package r01ui.base.components.datetime;

import java.time.LocalDate;

import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.DateField;

public class VaadinDateMonthRangeField  
	 extends VaadinDateTimeRangeFieldBase<LocalDate,DateResolution,
	 										  DateField> {

	private static final long serialVersionUID = 4401268432891162533L;

	public VaadinDateMonthRangeField() {
		super(DateField::new,
			  DateResolution.MONTH);
	}

}
