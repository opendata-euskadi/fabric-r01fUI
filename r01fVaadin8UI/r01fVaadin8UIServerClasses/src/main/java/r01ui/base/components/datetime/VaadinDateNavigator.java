package r01ui.base.components.datetime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * Creates a date navigator like:
 * <pre>
 * 		[<] [date] [>]
 * </pre>
 */
public class VaadinDateNavigator 
	 extends CustomField<LocalDate> 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -8261978419102513251L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Button _btnPrevDate;
	private final DateField _date;
	private final Button _btnNextDate;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR	
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDateNavigator(final UII18NService i18n) {
		////////// Components
		_btnPrevDate = new Button(VaadinIcons.ANGLE_LEFT);
		_btnPrevDate.setDescription(i18n.getMessage("previous"));
		_btnPrevDate.setEnabled(false);

		_date = new DateField(i18n.getMessage("date"));
		
		_btnNextDate = new Button(VaadinIcons.ANGLE_RIGHT);
		_btnNextDate.setDescription(i18n.getMessage("next"));
		_btnNextDate.setEnabled(false);
		
		////////// Behaviour
		_date.addValueChangeListener(valChangeEvent -> {
										LocalDate lDate = valChangeEvent.getValue();
										_btnNextDate.setEnabled(lDate != null);
										_btnPrevDate.setEnabled(lDate != null);
									 });
		_btnPrevDate.addClickListener(ClickEvent -> this.gotoPrevious());
		_btnNextDate.addClickListener(clickEvent -> this.gotoNext());
	}
	@Override
	protected Component initContent() {
		////////// Layout
		HorizontalLayout hly = new HorizontalLayout(_btnPrevDate,
													_date,
													_btnNextDate);	
		hly.setComponentAlignment(_btnPrevDate,Alignment.BOTTOM_LEFT);
		hly.setComponentAlignment(_date,Alignment.BOTTOM_LEFT);
		hly.setComponentAlignment(_btnNextDate,Alignment.BOTTOM_RIGHT);
		hly.setSizeFull();
		hly.setMargin(false);
		hly.setSpacing(false);
		return hly;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public LocalDate getValue() {
		return _date.getValue();
	}
	public Date getDate() {
		LocalDate ldate = this.getValue();
		Date outDate = ldate != null ? Date.from(ldate.atStartOfDay()
									       .atZone(ZoneId.systemDefault())
									       .toInstant())
									 : null;
		return outDate;
	}
	@Override
	public void doSetValue(final LocalDate date) {
		_date.setValue(date);
	}
	public void setDate(final Date date) {
		if (date == null)  {
			this.doSetValue(null);
		} else {
			LocalDate ldate = date.toInstant()
								  .atZone(ZoneId.systemDefault())
								  .toLocalDate();
			this.doSetValue(ldate);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Date gotoNext() {
		Date outDate = null;
		
		LocalDate ldate = _date.getValue();
		if (ldate != null) {
			LocalDate nextLDate = ldate.plusDays(1);
			_date.setValue(nextLDate);
			outDate = this.getDate();
		}
		return outDate;
	}
	public Date gotoPrevious() {
		Date outDate = null;
		
		LocalDate ldate = _date.getValue();
		if (ldate != null) {
			LocalDate nextLDate = ldate.minusDays(1);
			_date.setValue(nextLDate);
			outDate = this.getDate();
		}
		return outDate;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder 
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<LocalDate> listener) {
		Registration reg = super.addValueChangeListener(listener);
		
		// Raise an event when either the [lower bound] or [upper bound] change
		_date.addValueChangeListener(e -> {
											LocalDate oldVal = _date.getValue();
											ValueChangeEvent<LocalDate> evt = new ValueChangeEvent<>(this,		// component
																									 oldVal,	// old value
																									 true);		// user originated
											listener.valueChange(evt);
									 });
		return reg;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// TODO update i18n messages
	}
}
