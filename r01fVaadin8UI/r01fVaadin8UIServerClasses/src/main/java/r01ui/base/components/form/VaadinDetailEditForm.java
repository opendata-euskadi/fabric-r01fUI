package r01ui.base.components.form;

import r01f.locale.I18NKey;
import r01f.patterns.Factory;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailForm;

public interface VaadinDetailEditForm<V extends UIViewObject> 
		 extends VaadinDetailForm<V> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Opens the form in creation mode
	 * @param viewObjFactory
	 * @param saveSubscriber
	 */
	public void forCreating(final Factory<V> viewObjFactory,
							final UIPresenterSubscriber<V> saveSubscriber);
	/**
	 * Opens the form in edition mode
	 * @param viewObj
	 * @param saveSubscriber
	 * @param deleteSubscriber
	 */
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber);
	/**
	 * Opens the form in edition mode with NO delete subscriber
	 * @param viewObj
	 * @param saveSubscriber
	 */
	public default void forEditing(final V viewObj,
						   		   final UIPresenterSubscriber<V> saveSubscriber) {
		this.forEditing(viewObj,
						saveSubscriber,
						null);		// no delete subscriber
	}
	/**
	 * Closes the form
	 */
	public void close();
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public default I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("new");
	}
	public default I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("edit");
	}
}
