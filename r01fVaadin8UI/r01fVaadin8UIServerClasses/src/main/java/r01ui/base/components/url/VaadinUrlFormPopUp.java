package r01ui.base.components.url;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewUrl;

/**
 * Just the [url builder] popup
 * 		+----------------------------------------------+
 * 		| popup title                                  |
 *		+----------------------------------------------+
 *		|                                              |
 *		| +------------------------------------------+ |
 *		| | Url [                                ]   | |
 *		| +------------------------------------------+ |
 *		|                                [Cancel] [OK] | 
 *		+----------------------------------------------+
 */
public class VaadinUrlFormPopUp
     extends VaadinUrlFormPopUpBase<UIViewUrl,			// the view object 
								 	VaadinUrlForm> {	// the form

	private static final long serialVersionUID = 7929829400216069995L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FACTORY
/////////////////////////////////////////////////////////////////////////////////////////
	public static Factory<VaadinUrlFormPopUp> createFactoryFrom(final UII18NService i18n,
																final VaadinUrlForm form) { 
		return new Factory<VaadinUrlFormPopUp>() {
						@Override
						public VaadinUrlFormPopUp create() {
							return new VaadinUrlFormPopUp(i18n,
														  form);
						}
			   };
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlFormPopUp(final UII18NService i18n,
							  final VaadinUrlForm form) {
		super(i18n,
			  form,					// the form shown in the popup
			  UIViewUrl.FACTORY);	// view obje factory
	}
}