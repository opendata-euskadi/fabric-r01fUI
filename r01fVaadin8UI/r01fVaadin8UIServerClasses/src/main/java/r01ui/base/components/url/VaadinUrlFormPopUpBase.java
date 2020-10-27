package r01ui.base.components.url;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.weblink.IsUIViewUrl;
import r01f.util.types.Strings;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons.VaadinAcceptCancelDeleteButton;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;

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
public abstract class VaadinUrlFormPopUpBase<V extends IsUIViewUrl,
										 	 F extends VaadinUrlFormBase<V>>
     		  extends VaadinDetailEditFormWindowBase<V,		// the view object 
								 					 F> {	// the form

	private static final long serialVersionUID = 8514986088855640087L;		// the form that edits that view object
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUrlFormPopUpBase(final UII18NService i18n,
							  	  final F form,
							  	  final Factory<V> viewObjFactory) {
		super(i18n,
			  form,				// the form shown in the popup
			  viewObjFactory);	// view obje factory
		
		////////// styling
		this.setHeight(85,Unit.PERCENTAGE);
		this.setWidth(85,Unit.PERCENTAGE);
		this.setButtonsVisibleStatus(false,
									 VaadinAcceptCancelDeleteButton.ACCEPT);
		form.addUrlValueChangeListener(event -> this.setButtonsVisibleStatus(Strings.isNOTNullOrEmpty(event.getValue().asString()),
									                                         VaadinAcceptCancelDeleteButton.ACCEPT));
		////////// behavior
		_setBehavior();
	}
	private void _setBehavior() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber) {
		// just delegate
		super.forEditing(viewObj,
						 saveSubscriber, 
						 deleteSubscriber);
		// the [delete] button is NEVER visible in the [url builder]
		// ... but the upper #forEditing() methods makes the [delete]
		// 	   button visible by default
		this.setButtonsVisibleStatus(false,
									 VaadinAcceptCancelDeleteButton.DELETE);
	}
}