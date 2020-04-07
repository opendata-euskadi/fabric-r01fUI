package r01ui.base.components.form;

import com.vaadin.ui.Button.ClickListener;

import r01f.locale.I18NKey;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons.VaadinAcceptCancelDeleteButton;

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
	public void forCreating(final UIPresenterSubscriber<V> saveSubscriber);
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Click the cancel button
	 */
	public default void cancel() {
		// nothing
	}
	/**
	 * Closes the form
	 */
	public default void close() {
		this.setVisible(false);			// hide by default
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	i18n                                                                         
/////////////////////////////////////////////////////////////////////////////////////////
	public default I18NKey getNewItemCaptionI18NKey() {
		return I18NKey.named("creating");
	}
	public default I18NKey getEditItemCaptionI18NKey() {
		return I18NKey.named("editing");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Listeners
/////////////////////////////////////////////////////////////////////////////////////////
	public void addCancelButtonClickListner(final ClickListener clickListener);
	public void addAcceptButtonClickListner(final ClickListener clickListener);
	public void addDeleteButtonClickListner(final ClickListener clickListener);
/////////////////////////////////////////////////////////////////////////////////////////
//	Enable / visible
/////////////////////////////////////////////////////////////////////////////////////////
	public void setButtonsVisibleStatus(final boolean visible,
								  		final VaadinAcceptCancelDeleteButton... btns);
	public void setButtonsEnableStatus(final boolean enabled,
								  	   final VaadinAcceptCancelDeleteButton... btns);
}
