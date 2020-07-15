package r01ui.base.components.contact.website;

import lombok.experimental.Accessors;
import r01f.types.contact.ContactWeb;
import r01f.types.url.Url;
import r01ui.base.components.contact.VaadinContactMeanObjectBase;

@Accessors( prefix="_" )
public class VaadinViewDirectoryContactWebSite
     extends VaadinContactMeanObjectBase<ContactWeb> {

	private static final long serialVersionUID = 981595816336929199L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String URL_FIELD = "url";
	public Url getUrl() {
		return _wrappedModelObject.getUrl();
	}
	public void setUrl(final Url url) {
		_wrappedModelObject.setUrl(url);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewDirectoryContactWebSite() {
		this(new ContactWeb());
	}
	public VaadinViewDirectoryContactWebSite(final ContactWeb web) {
		super(web);
	}
}
