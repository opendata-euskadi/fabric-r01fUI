package r01f.ui.vaadin.security.user;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.ContactInfo;
import r01f.types.contact.ContactInfoUsage;
import r01f.types.contact.ContactMail;
import r01f.types.contact.ContactPhone;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;
import r01f.types.contact.PersonalData;
import r01f.types.contact.Phone;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors(prefix="_")
public abstract class VaadinViewUser<U extends User>
	 		  extends UIViewObjectWrappedBase<U> {

	private static final long serialVersionUID = -2129390001586971529L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewUser(final U user) {
		super(user);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER SOURCE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The directory from which this user was loaded (if any)
	 * useful when picking uses and the source directory is used in other layers
	 */
	@Getter @Setter private VaadinSecurityUserDirectory _sourceUserDirectory;
/////////////////////////////////////////////////////////////////////////////////////////
//	OID & USER CODE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OID_FIELD = "oid";
	public UserOID getOid() {
		return _wrappedModelObject.getOid();
	}

	public static final String PERSON_ID_FIELD = "personId";
	public PersonID getPersonId() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData().getPersonId()
															 : null;
	}
	public void setPersonId(final PersonID personId) {
		if (_wrappedModelObject.getPersonalData() == null) _wrappedModelObject.setPersonalData(new PersonalData());
		_wrappedModelObject.getPersonalData().setPersonId(personId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DESCRIPTION
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Used when presenting an user's summary in places like grids
	 * @return
	 */
	public abstract String getDescription();
/////////////////////////////////////////////////////////////////////////////////////////
//	NAME & SURNAME
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String NAME_FIELD = "name";
	public String getName() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData()
								  												  .getName()
								  							 : null;
	}
	public void setName(final String name) {
		_wrappedModelObject.setName(name);
	}

	public static final String SURNAME1_FIELD = "surname1";
	public String getSurname1() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData()
								  												  .getSurname1()
								  							 : null;
	}
	public void setSurname1(final String surname1) {
		_wrappedModelObject.setSurname1(surname1);
	}

	public static final String SURNAME2_FIELD = "surname2";
	public String getSurname2() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData()
								  												  .getSurname1()
								  							 : null;
	}
	public void setSurname2(final String surname2) {
		_wrappedModelObject.setSurname2(surname2);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DISPLAY NAME
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String DISPLAY_NAME_FIELD = "displayName";
	public String getDisplayName() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getDisplayName()
															 : null;
	}

	public static final String SURNAME_FIELD = "surname";
	public String getSurname() {
		return _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData()
								  												  .getSurname()
								  							 : null;
	}
	public void setSurname(final String surname) {
		_wrappedModelObject.setSurname1(surname);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BIRTH DATE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String BIRTH_DATE_FIELD = "birthDate";
	public LocalDate getBirthDate() {
		Date birthDate = _wrappedModelObject.getPersonalData() != null ? _wrappedModelObject.getPersonalData().getBirthDate()
																	   : null;
		return birthDate != null ? birthDate.toInstant()
											.atZone(ZoneId.systemDefault())
											.toLocalDate()
								 : null;
	}
	public void setBirthDate(final LocalDate lDate) {
		if (_wrappedModelObject.getPersonalData() == null) _wrappedModelObject.setPersonalData(new PersonalData());
		Date date = lDate != null ? Date.from(lDate.atStartOfDay()
												   .atZone(ZoneId.systemDefault())
												   .toInstant())
								  : null;
		_wrappedModelObject.getPersonalData()
						   .setBirthDate(date);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String MAIL_FIELD = "mail";
	public EMail getMail() {
		return _wrappedModelObject.getContactInfo() != null ? _wrappedModelObject.getContactInfo()
																				 .getMailAddress()
															: null;
	}
	public void setMail(final EMail mail) {
		if (_wrappedModelObject.getContactInfo() == null) _wrappedModelObject.setContactInfo(new ContactInfo());
		_wrappedModelObject.getContactInfo()
						   .addMailAddress(ContactMail.createToBeUsedFor(ContactInfoUsage.PERSONAL)
								   					  .mailTo(mail));
	}
	public String getMailAsStringOrNull() {
		EMail email = this.getMail();
		return email != null ? email.asString() : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PHONE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PHONE_FIELD = "phone";
	public Phone getPhone() {
		return _wrappedModelObject.getContactInfo() != null ? _wrappedModelObject.getContactInfo()
								  												 .getPhone()
								  							: null;
	}
	public void setPhone(final Phone phone) {
		if (_wrappedModelObject.getContactInfo() == null) _wrappedModelObject.setContactInfo(new ContactInfo());
		_wrappedModelObject.getContactInfo()
						   .addPhone(ContactPhone.createToBeUsedFor(ContactInfoUsage.PERSONAL)
								   				 .allwaysAvailableForCalling()
								   				 .withNumber(phone));
	}
	public String getPhoneAsStringOrNull() {
		Phone phone = this.getPhone();
		return phone != null ? phone.asString() : null;
	}
}
