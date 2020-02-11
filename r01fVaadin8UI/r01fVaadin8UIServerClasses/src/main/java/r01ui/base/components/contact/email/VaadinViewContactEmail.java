package r01ui.base.components.contact.email;

import com.google.common.base.Strings;

import lombok.experimental.Accessors;
import r01f.types.contact.ContactMail;
import r01f.types.contact.EMail;
import r01ui.base.components.contact.VaadinContactMeanObjectBase;

@Accessors( prefix="_" )
public class VaadinViewContactEmail
     extends VaadinContactMeanObjectBase<ContactMail> {

	private static final long serialVersionUID = 981595816336929199L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String EMAIL_FIELD = "mail";
	public EMail getMail() {
		return _wrappedModelObject.getMail();
	}
	public String getMailAsString() {
		return this.getMail() != null ? this.getMail().asString() : null;
	}
	public void setMail(final EMail eMail) {
		_wrappedModelObject.setMail(eMail);
	}
	public void setMailFromString(final String eMail) {
		if (Strings.isNullOrEmpty(eMail)) {
			_wrappedModelObject.setMail(null);
		} else {
			_wrappedModelObject.setMail(EMail.of(eMail));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewContactEmail() {
		this(new ContactMail());
	}
	public VaadinViewContactEmail(final ContactMail mail) {
		super(mail);
	}
}
