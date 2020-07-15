package r01ui.base.components.form;

import com.vaadin.data.Binder;

import r01f.ui.viewobject.UIViewObject;

/**
 * Vaadin offers TWO ways of binding [ui controls] to [view objects] (beans):
 * 
 * [1] - Vaadin AUTOMATIC binding
 * ==============================
 *
 * Binds the a [view object]'s properties to the underlying [ui control]s and back 
 *	
 * Uses vaadin's {@link Binder}  #setBean(bean) method that BINDs the [UI controls] to 
 * the [bean's properties] so when an [UI control] is changed, the bean's property 
 * is also updated. 
 * The inverse is also true: when the [view object] is updated, the [ui controls] 
 * are also updated
 * 
 * ... so changes in the [UI controls] are immediately reflected to the received bean 
 *     so it's NOT possible to CANCEL editions
 *	
 * If this binding method is used, use #getBean() method to get the [view object]
 * 
 * Vaadin MANUAL binding
 * =====================
 * 
 * Sets the [UI control]s values from the [view object]'s properties values BUT not back
 * 
 * This method uses vaadin's {@link Binder} #readBean(bean) method that READSs the [UI controls]  
 * from the [view object]'s properties 
 * BUT contrary to the [automatic binding]:
 * 		- when the [ui control] is updated, the changes are NOT reflected on the [view object]
 * 		- when the [view object] changes the [ui control] is NOT 
 *
 * ... so changes in the [UI controls] are NOT reflected to the [view object]  
 * so it's possible to CANCEL editions
 * 
 * If this binding method is used, use #readBean(viewObj) method to get the [ui-control] values
 * copied to the given [view object]
 * 
 * IMPORTANT!!
 * ===========
 * r01ui uses MANUAL BINDING ONLY!!!
 * ... so NEVER use vaadinUIBinder.setBean(viewObj) USE vaadinUIBinder.readBean(viewObj) instead
 * @param <V>
 */
public interface VaadinFormEditsViewObject<V extends UIViewObject> {
/////////////////////////////////////////////////////////////////////////////////////////
//	Binding
/////////////////////////////////////////////////////////////////////////////////////////
	////////// [viewObject] > [UI control] --------------
	/**
	 * Copies the [view object] properties to the corresponding [ui control]
	 * @param viewObj
	 */
	public void editViewObject(final V viewObj);
	
	////////// [UI control] > [viewObject] --------------
	/**
	 * Copies the [ui control] data to the given [view object] even if any
	 * validation fails (beware that this method just copies the validated data)
	 * @param viewObj
	 */
	public void writeAsDraftEditedViewObjectTo(final V viewObj);
	
	/**
	 * Copies the [ui control] data to the given [view object]
	 * but ONLY if all the validations are checked ok in which case the method returns true
	 * ...if any validation fails, NO data is copied and the method returns false
	 * @param viewObj
	 * @return
	 */
	public boolean writeIfValidEditedViewObjectTo(final V viewObj);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * @return the Vaadin {@link Binder}
	 */
	public default Binder<V> getVaadinUIBinder() {
		return null;
	}
}
