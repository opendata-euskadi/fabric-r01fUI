package r01ui.base.components;

import r01f.locale.I18NKey;
import r01f.patterns.Factory;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.view.VaadinDetailView;
import r01f.ui.viewobject.UIViewObject;

public interface VaadinDetailEditWindow<V extends UIViewObject> 
		 extends VaadinDetailView<V> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Opens the window in creation mode
	 * @param viewObjFactory
	 * @param saveSubscriber
	 */
	public void forCreating(final Factory<V> viewObjFactory,
							final UIPresenterSubscriber<V> saveSubscriber);
	/**
	 * Opens the window in edition mode
	 * @param viewObj
	 * @param saveSubscriber
	 * @param deleteSubscriber
	 */
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber);
	/**
	 * Closes the window
	 */
	public void close();
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public I18NKey getNewItemCaptionI18NKey();

	public I18NKey getEditItemCaptionI18NKey();
}
