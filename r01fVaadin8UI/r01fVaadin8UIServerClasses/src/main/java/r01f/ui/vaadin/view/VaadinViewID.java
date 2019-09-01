package r01f.ui.vaadin.view;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import r01f.guids.OIDBaseMutable;
import r01f.types.url.Url;
import r01f.util.types.StringSplitter;
import r01f.util.types.Strings;

/**
 * A view key
 */
public class VaadinViewID 
	 extends OIDBaseMutable<String> {

	private static final long serialVersionUID = 7659549152655411096L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewID(final String oid) {
		super(oid);
	}
	public static VaadinViewID forId(final String id) {
		return new VaadinViewID(id);
	}
	public static VaadinViewID valueOf(final String id) {
		return VaadinViewID.forId(id);
	}
	public static VaadinViewID from(final Url url) {
		return VaadinViewID.fromUrlFragment(url.getUrlPathFragment());
	}
	public static VaadinViewID fromUrlFragment(final String urlPathFragment) {
		if (Strings.isNullOrEmpty(urlPathFragment)) throw new IllegalArgumentException("illegal url path fragment!!");
		// vaadin url path fragment is like: #!{viewId}/{param}={value}/{param}={value}...
		// extract the viewId
		String withoutExclamation = urlPathFragment.indexOf("!") == 0 ? urlPathFragment.substring(1)
																	  : urlPathFragment;
		String viewId = Iterables.getFirst(StringSplitter.using(Splitter.on("/")
																		.omitEmptyStrings()
																		.trimResults())
														 .at(withoutExclamation)
														 .split(),
										   null);
		return VaadinViewID.forId(viewId);
	}
	@Override
	public boolean is(final String id) {
		return this.asString().equals(id);
	}
	public boolean is(final Url url) {
		String urlPathFragment = url.getUrlPathFragment();
		return this.isUrlPathFragment(urlPathFragment);
	}
	public boolean isUrlPathFragment(final String urlPathFragment) {
		VaadinViewID viewId = VaadinViewID.fromUrlFragment(urlPathFragment);
		return this.is(viewId);
	}	
}
