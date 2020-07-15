package r01ui.base.components.contact;

import lombok.experimental.Accessors;
import r01f.types.contact.ContactInfoUsage;
import r01f.types.contact.ContactMeanData;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors( prefix="_" )
public abstract class VaadinContactMeanObjectBase<D extends ContactMeanData>
     		  extends UIViewObjectWrappedBase<D>
  	       implements VaadinContactMeanObject {

	private static final long serialVersionUID = -1237014920625204245L;

/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String USAGE_FIELD = "usage";
	@Override
	public ContactInfoUsage getUsage() {
		return _wrappedModelObject.getUsage();
	}
	@Override
	public void setUsage(final ContactInfoUsage usage) {
		_wrappedModelObject.setUsage(usage);;
	}

	public static final String DEFAULT_FIELD = "default";
	@Override
	public boolean isDefault() {
		return _wrappedModelObject.isDefault();
	}
	@Override
	public void setDefault(final boolean def) {
		_wrappedModelObject.setDefault(def);
	}

	public static final String PRIVATE_FIELD = "private";
	@Override
	public boolean isPrivate() {
		return _wrappedModelObject.isPrivate();
	}
	@Override
	public void setPrivate(final boolean priv) {
		_wrappedModelObject.setPrivate(priv);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactMeanObjectBase(final D meanData) {
		super(meanData);
	}
}
