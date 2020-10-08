package r01f.ui.vaadin.view;

import r01f.guids.OIDBaseMutable;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;

/**
 * An UI key
 */
public class VaadinUIID 
	 extends OIDBaseMutable<String> {

	private static final long serialVersionUID = -908558415727183397L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUIID(final String oid) {
		super(oid);
	}
	public static VaadinUIID forId(final String id) {
		return new VaadinUIID(id);
	}
	public static VaadinUIID valueOf(final String id) {
		return VaadinUIID.forId(id);
	}
	public static VaadinUIID from(final Url url) {
		return VaadinUIID.fromUrlPath(url.getUrlPath());
	}
	public static VaadinUIID fromUrlPath(final UrlPath urlPath) {
		if (urlPath == null) throw new IllegalArgumentException("illegal url path!!");
		// vaadin url path is like: /{ui-id}#!{viewId}/{param}={value}/{param}={value}...
		// extract the uiId
		return VaadinUIID.forId(urlPath.getFirstPathElement());
	}
	@Override
	public boolean is(final String id) {
		return this.asString().equals(id);
	}
	public boolean is(final Url url) {
		UrlPath urlPath = url.getUrlPath();
		return this.isUrlPath(urlPath);
	}
	public boolean isUrlPath(final UrlPath urlPath) {
		VaadinUIID viewId = VaadinUIID.fromUrlPath(urlPath);
		return this.is(viewId);
	}	
}
