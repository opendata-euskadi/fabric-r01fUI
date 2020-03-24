package r01ui.base.components;

import r01f.ui.i18n.UII18NService;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinViewHasVaadinViewObjectBinder;

/**
 * Creates a detail edit popup-window like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++
 * 	    + Caption                                     +
 *      +++++++++++++++++++++++++++++++++++++++++++++++
 *      + [Form]                                      +
 *      +                                             +
 *      +                                             +
 *      +                                             +
 *      +---------------------------------------------+
 *      + [Delete]                  [Cancel] [Accept] +
 *      +++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * @param <V>
 */
@Deprecated	// see VaadinDetailEditFormWindowBase
public abstract class VaadinDetailEditWindowBase<V extends UIViewObject,
												 F extends VaadinDetailForm<V> 
														 & VaadinViewHasVaadinViewObjectBinder<V>>
     		  extends VaadinDetailEditFormWindowBase<V,F> {

	private static final long serialVersionUID = 3039822348497615734L;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDetailEditWindowBase(final UII18NService i18n,
									  final F detailView) {
		super(i18n,
			  detailView);
	}
}
