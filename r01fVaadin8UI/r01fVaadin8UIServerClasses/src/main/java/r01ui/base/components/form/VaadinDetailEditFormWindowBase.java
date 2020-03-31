package r01ui.base.components.form;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons.VaadinAcceptCancelDeleteButton;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

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
public abstract class VaadinDetailEditFormWindowBase<V extends UIViewObject,
												 	 F extends VaadinDetailForm<V>
															 & VaadinFormHasVaadinUIBinder<V>>
     		  extends Window
     	   implements VaadinDetailEditForm<V> {

	private static final long serialVersionUID = 7719084020409366076L;

/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final UII18NService _i18n;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	protected final F _form;
	protected final VaadinAcceptCancelDeleteButtons _btnAcepCancDelete;

	// OUTSIDE WORLD SUBSCRIBERS
	protected UIPresenterSubscriber<V> _saveSubscriber;
	protected UIPresenterSubscriber<V> _deleteSubscriber;
	
	// the view object being managed
	private V _viewObj;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDetailEditFormWindowBase(final UII18NService i18n,
									  	  final F form) {
		_i18n = i18n;

		// Form
		_form = form;
		
		
		// OK | CANCEL | DELETE
		_btnAcepCancDelete = new VaadinAcceptCancelDeleteButtons(i18n);
		// - CANCEL
		_btnAcepCancDelete.addCancelButtonClickListner(event -> VaadinDetailEditFormWindowBase.this.close());
		// - OK
		_btnAcepCancDelete.addAcceptButtonClickListner(event -> {
															// collect ui controls values & tell
															Binder<V> vaadinBinder = null;
															try {
																vaadinBinder = _form.getVaadinUIBinder();	// sometimes the binder is not available
															} catch (Throwable th) {
																/* ignored */
															}
															if (vaadinBinder != null) {
																BinderValidationStatus<V> status = vaadinBinder.validate();
																if (status.hasErrors()) {
																	StringBuffer errorMessages = new StringBuffer();
																	for (final ValidationResult v : status.getValidationErrors()) {
																		errorMessages.append(v.getErrorMessage() + "\n\r");
																	}
																	Notification.show(errorMessages.toString(),
																					  Type.WARNING_MESSAGE);
																	return;
																}
															}
															// is valid or maybe not if not implementing VaadinValidatesForm
															_form.writeIfValidFromUIControlsTo(_viewObj);
															if (_saveSubscriber != null) _saveSubscriber.onSuccess(_viewObj);
															this.close();
											  		  });
		// - DELETE
		_btnAcepCancDelete.addDeleteButtonClickListner(event -> {
															if (_deleteSubscriber != null) _deleteSubscriber.onSuccess(_form.getViewObject());
													   });

		// Layout
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		layout.addComponents(_form,
							 _btnAcepCancDelete);
		this.setContent(layout);
		
		// style window
		this.center();
		this.setModal(true);
		this.setCaption("");
		this.setResizable(false);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENTRY POINT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void forCreating(final Factory<V> viewObjFactory,
							final UIPresenterSubscriber<V> saveSubscriber) {
		// what to do after saving
		_saveSubscriber = saveSubscriber;
		_deleteSubscriber = null;		// cannot delete from a create window
		
		// create the view object
		_viewObj = viewObjFactory.create();

		// bind the view object to the view
		_form.bindUIControlsTo(_viewObj);
		
		// set window caption
		this.setCaption(_i18n.getMessage(this.getNewItemCaptionI18NKey()));
		
		// set the buttons status
		_btnAcepCancDelete.setCreatingNewRecordStatus();
	}
	@Override
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber) {
		if (viewObj == null) throw new IllegalArgumentException("view object MUST NOT be null!");
		_viewObj = viewObj;

		// store the subscribers
		_saveSubscriber = saveSubscriber;
		_deleteSubscriber = deleteSubscriber;
		
		// sets window caption
		this.setCaption(_i18n.getMessage(this.getEditItemCaptionI18NKey()));

		// bind the view object to the view
		_form.readUIControlsFrom(_viewObj);		// BEWARE!!! the given [view object] will NOT be modified when the UI controls are updated!

		// set the buttons status
		_btnAcepCancDelete.setEditingExistingRecordStatus();
		
		// if no delete subscriber is handed, do NOT show the delete button
		if (_deleteSubscriber == null) _btnAcepCancDelete.setDeleteButtonVisible(false);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void setButtonsVisibleStatus(final boolean visible,
								  		final VaadinAcceptCancelDeleteButton... btns) {
		_btnAcepCancDelete.setButtonsVisibleStatus(visible, 
												   btns);
	}
	public void setButtonsEnableStatus(final boolean enabled,
								  	   final VaadinAcceptCancelDeleteButton... btns) {
		_btnAcepCancDelete.setButtonsEnableStatus(enabled, 
												   btns);
	}
}
