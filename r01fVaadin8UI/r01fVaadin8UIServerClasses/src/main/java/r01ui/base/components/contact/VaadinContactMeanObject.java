package r01ui.base.components.contact;

import r01f.types.contact.ContactInfoUsage;
import r01f.ui.viewobject.UIViewObject;

public interface VaadinContactMeanObject
		 extends UIViewObject {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public ContactInfoUsage getUsage();
	public void setUsage(final ContactInfoUsage usage);

	public boolean isDefault();
	public void setDefault(final boolean def);

	public boolean isPrivate();
	public void setPrivate(final boolean priv);
}
