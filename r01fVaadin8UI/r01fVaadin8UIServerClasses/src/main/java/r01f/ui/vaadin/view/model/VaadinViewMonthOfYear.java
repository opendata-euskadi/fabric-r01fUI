package r01f.ui.vaadin.view.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.types.datetime.MonthOfYear;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class VaadinViewMonthOfYear
  implements UIViewObject {

	private static final long serialVersionUID = 1449034784861550776L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                     
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final MonthOfYear _monthOfYear;
	@Getter private final Language _showInLanguage;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public String getName() {
		return Strings.capitalizeFirstLetter(Dates.getMonthName(_monthOfYear.asInteger()-1, 
											 					_showInLanguage));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object other) {
		if (other == null) return false;
		if (this == other) return true;
		if (!(other instanceof VaadinViewMonthOfYear)) return false;
		VaadinViewMonthOfYear otherVMonthOfYear = (VaadinViewMonthOfYear)other;
		return _monthOfYear.is(otherVMonthOfYear.getMonthOfYear())
			&& _showInLanguage.is(otherVMonthOfYear.getShowInLanguage());
	}
	@Override
	public int hashCode() {
		return _monthOfYear.hashCode();
	}
}
