package r01f.ui.component;

import r01f.guids.OIDBaseMutable;

/**
 * An ui component's identifier
 */
public class UIComponentID
     extends OIDBaseMutable<String>   {	// usually this should extend OIDBaseInmutable BUT it MUST have a default no-args constructor to be serializable

	private static final long serialVersionUID = -4358217246961346683L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public UIComponentID() {
		/* default no args constructor for serialization purposes */
	}
	public UIComponentID(final String id) {
		super(id);
	}
	public static UIComponentID valueOf(final String s) {
		return UIComponentID.forId(s);
	}
	public static UIComponentID fromString(final String s) {
		return UIComponentID.forId(s);
	}
	public static UIComponentID forId(final String id) {
		return new UIComponentID(id);
	}
}
