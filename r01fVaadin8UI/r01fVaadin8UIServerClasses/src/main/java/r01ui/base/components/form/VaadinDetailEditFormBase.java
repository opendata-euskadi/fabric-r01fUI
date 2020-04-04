package r01ui.base.components.form;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.facets.view.HasCaptionLanguageDependent;
import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.Strings;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons.VaadinAcceptCancelDeleteButton;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

/**
 * Creates a detail edit form like:
 * <pre>
 *      +++++++++++++++++++++++++++++++++++++++++++++++
 *      + [Form]                                      +
 *      +                                             +
 *      +                                             +
 *      +                                             +
 *      +---------------------------------------------+
 *      + [Delete]                  [Cancel] [Accept] +
 *      +++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * BEWARE! DO NOT CONFUSE with it's pop-up window counterpart {@link VaadinDetailEditFormWindowBase}
 * @param <V>
 */
@Accessors(prefix="_")
public abstract class VaadinDetailEditFormBase<V extends UIViewObject,
											   F extends VaadinDetailForm<V>
												       & VaadinFormHasVaadinUIBinder<V>>
     		  extends Composite
     	   implements VaadinDetailEditForm<V> {

	private static final long serialVersionUID = 9150166467660862183L;

/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final UII18NService _i18n;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final F _form;
			protected final VaadinAcceptCancelDeleteButtons _btnAcepCancDelete;

	// OUTSIDE WORLD SUBSCRIBERS
	protected UIPresenterSubscriber<V> _saveSubscriber;
	protected UIPresenterSubscriber<V> _deleteSubscriber;
	
	// the view object being managed
	private V _viewObj;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDetailEditFormBase(final UII18NService i18n,
									final F form) {
		_i18n = i18n;

		// Form
		_form = form;

		// OK | CANCEL | DELETE
		_btnAcepCancDelete = new VaadinAcceptCancelDeleteButtons(i18n);
		_setAcceptCancelDeleteButtonsBehavior();

		// Layout
		CssLayout layout = new CssLayout(_form,
							 			 _btnAcepCancDelete);
		layout.setWidthFull();
		this.setCompositionRoot(layout);
	}
	private void _setAcceptCancelDeleteButtonsBehavior() {
		// - CANCEL
		_btnAcepCancDelete.addCancelButtonClickListner(event -> VaadinDetailEditFormBase.this.close());
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
		_viewObj = viewObjFactory.create();		// set _viewObj BEFORE binding!

		// bind the view object to the view
		_form.bindUIControlsTo(_viewObj);
		
		// set window caption
		this.setCaption(_i18n.getMessage(this.getNewItemCaptionI18NKey()));
		
		// set the buttons status
		_btnAcepCancDelete.setCreatingNewRecordStatus();
		this.setVisible(true);
	}
	@Override
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber) {
		if (viewObj == null) throw new IllegalArgumentException("view object MUST NOT be null!");

		// store the subscribers
		_saveSubscriber = saveSubscriber;
		_deleteSubscriber = deleteSubscriber;
		
		// store the view object
		_viewObj = viewObj;						// set _viewObj BEFORE binding!
		
		// bind the view object to the view
		_form.bindUIControlsTo(_viewObj);		// BEWARE!!! the given [view object] will NOT be modified when the UI controls are updated!

		// sets window caption
		if (viewObj instanceof HasCaptionLanguageDependent) {
			HasCaptionLanguageDependent hasCaption = (HasCaptionLanguageDependent)viewObj;
			this.setCaption(Strings.customized("{}: {}",
											   _i18n.getMessage(this.getEditItemCaptionI18NKey()),
											   hasCaption.getCaption(_i18n.getCurrentLanguage())));
		} else {
			this.setCaption(_i18n.getMessage(this.getEditItemCaptionI18NKey()));
		}
		
		// set the buttons status
		_btnAcepCancDelete.setEditingExistingRecordStatus();
		
		// if no delete subscriber is handed, do NOT show the delete button
		if (_deleteSubscriber == null) _btnAcepCancDelete.setDeleteButtonVisible(false);
		this.setVisible(true);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void cancel() {
		// simulate the [cancel] button click
		_btnAcepCancDelete.cancel();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addCancelButtonClickListner(final ClickListener clickListener) {
		_btnAcepCancDelete.addCancelButtonClickListner(clickListener);
	}
	@Override
	public void addAcceptButtonClickListner(final ClickListener clickListener) {
		_btnAcepCancDelete.addAcceptButtonClickListner(clickListener);
	}
	@Override
	public void addDeleteButtonClickListner(final ClickListener clickListener) {
		_btnAcepCancDelete.addDeleteButtonClickListner(clickListener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setButtonsVisibleStatus(final boolean visible,
								  		final VaadinAcceptCancelDeleteButton... btns) {
		_btnAcepCancDelete.setButtonsVisibleStatus(visible, 
												   btns);
	}
	@Override
	public void setButtonsEnableStatus(final boolean enabled,
								  	   final VaadinAcceptCancelDeleteButton... btns) {
		_btnAcepCancDelete.setButtonsEnableStatus(enabled, 
												   btns);
	}
}
