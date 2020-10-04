package r01ui.base.components.url.weblink;

import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewWebLink;
import r01ui.base.components.url.VaadinUrlForm;

/////////////////////////////////////////////////////////////////////////////////////////
/**
 * The form: just contains the [url builder] component
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		| +-------------------------------------------------------------+ |
 * 		| +   	                                                        + |
 * 		| + Url [                                                 ]     + |
 * 		| +                                                             + |
 * 		| +-------------------------------------------------------------+ |
 * 		| +-------------------------------------------------------------+ |
 * 	    | +                                                             + |
 * 		| +           The [url presentation data] form                <--------- this is an addition from the base {@link VaadinUrlForm}
 * 		| +                                                             + |
 * 		| +-------------------------------------------------------------+ |
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public class VaadinWebLinkForm
	 extends VaadinWebLinkFormBase<UIViewWebLink> {

	private static final long serialVersionUID = 5451877165188105219L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinWebLinkForm(final UII18NService i18n) {
		this(i18n,
			 null);	// no features
	}
	public VaadinWebLinkForm(final UII18NService i18n,
							 // sets the [link builder wizard] features
							 final VaadinWebLinkFormFeatures linkBuilderFeatures) {
		super(UIViewWebLink.class,
			  i18n,
			  linkBuilderFeatures);
		// BEWARE!!!
		_bindUIComponents();
		_setUII18NLabels();
	}
}