package r01f.ui.vaadin.view;

import r01f.guids.OIDBaseMutable;

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
	@Override
	public boolean is(final String id) {
		return this.asString().equals(id);
	}
}
