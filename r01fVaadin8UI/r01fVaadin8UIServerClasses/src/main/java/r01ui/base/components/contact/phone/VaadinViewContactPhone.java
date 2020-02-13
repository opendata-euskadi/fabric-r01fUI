package r01ui.base.components.contact.phone;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.google.common.collect.Range;

import lombok.experimental.Accessors;
import r01f.types.contact.ContactPhone;
import r01f.types.contact.ContactPhoneType;
import r01f.types.contact.Phone;
import r01f.types.contact.PhoneExtension;
import r01f.util.types.Dates;
import r01f.util.types.Ranges;
import r01f.util.types.Strings;
import r01ui.base.components.contact.VaadinContactMeanObjectBase;

@Accessors( prefix="_" )
public class VaadinViewContactPhone
     extends VaadinContactMeanObjectBase<ContactPhone> {

	private static final long serialVersionUID = 981595816336929199L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	NUMBER
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PHONE_NUMBER_FIELD = "number";
	public Phone getNumber() {
		return _wrappedModelObject.getNumber();
	}
	public String getNumberAsString() {
		return this.getNumber() != null ? this.getNumber().asString() : null;
	}
	public void setNumber(final Phone number) {
		_wrappedModelObject.setNumber(number);
	}
	public void setNumberFromString(final String number) {
		if (Strings.isNullOrEmpty(number)) {
			_wrappedModelObject.setNumber(null);
		} else {
			_wrappedModelObject.setNumber(Phone.of(number));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PHONE_EXTENSION_FIELD = "extension";
	public PhoneExtension getExtension() {
		return _wrappedModelObject.getExtension();
	}
	public String getExtensionAsString() {
		return this.getExtension() != null ? this.getExtension().asString() : null;
	}
	public void setExtension(final PhoneExtension extension) {
		_wrappedModelObject.setExtension(extension);
	}
	public void setExtensionFromString(final String extension) {
		if (Strings.isNullOrEmpty(extension)) {
			_wrappedModelObject.setExtension(null);
		} else {
			_wrappedModelObject.setExtension(PhoneExtension.of(extension));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AVAILABLE RANGE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String AVAILABLE_RANGE = "availableRange";
	public Range<LocalDateTime> getAvailableRange() {
		Range<Integer> intRange = _wrappedModelObject.getAvailableRangeForCalling() != null ? _wrappedModelObject.getAvailableRangeForCalling()
																												 .asGuavaRange()
																							: null;
		Range<LocalDateTime> range = intRange != null
										? Ranges.rangeFrom(intRange.hasLowerBound() ? LocalDateTime.now().withHour(intRange.lowerEndpoint()) : null,
														   intRange.hasUpperBound() ? LocalDateTime.now().withHour(intRange.upperEndpoint()) : null)
												.asGuavaRange()
										: null;
		return range;
	}
	public void setAvailableRange(final Range<LocalDateTime> range) {
		if (range != null) {
			r01f.types.Range<Integer> intRange = Ranges.rangeFrom(range.hasLowerBound() ? range.lowerEndpoint().getHour() : null,
													   			  range.hasUpperBound() ? range.upperEndpoint().getHour() : null);
			_wrappedModelObject.setAvailableRangeForCalling(intRange);
		} else {
			_wrappedModelObject.setAvailableRangeForCalling(null);
		}
	}
	public String getAvailableRangeAsString() {
		return VaadinViewContactPhone.availableRangeAsString(this.getAvailableRange());
	}
	public static String availableRangeAsString(final Range<LocalDateTime> range) {
		if (range == null) return "";
		String fmt = "HH:00";
		String low = range.hasLowerBound() ? Dates.format(Date.from(range.lowerEndpoint()
																		 .atZone(ZoneId.systemDefault())
																		 .toInstant()),
														  fmt)
										   : "";
		String up = range.hasUpperBound() ? Dates.format(Date.from(range.upperEndpoint()
																		.atZone(ZoneId.systemDefault())
																		.toInstant()),
														  fmt)
										   : "";
		String renderedRangeTemplate = "{} - {}";
		return Strings.customized(renderedRangeTemplate,
								  low,up);
	}


	public static final String TYPE_FIELD = "type";
	public ContactPhoneType getType() {
		return _wrappedModelObject.getType();
	}
	public void setType(final ContactPhoneType type) {
		_wrappedModelObject.setType(type);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewContactPhone() {
		this(new ContactPhone());
		this.setAvailableRange(Ranges.rangeFrom(LocalDateTime.now().withHour(0),
										 		LocalDateTime.now().withHour(23))
									 .asGuavaRange());
	}
	public VaadinViewContactPhone(final ContactPhone phone) {
		super(phone);
	}
}
