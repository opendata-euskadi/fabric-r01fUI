package r01ui.base.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.view.VaadinDetailView;
import r01f.ui.vaadin.view.VaadinViewHasVaadinViewObjectBinder;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.button.VaadinAcceptCancelDeleteButtons;

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
public abstract class VaadinDetailEditWindowBase<V extends UIViewObject,
												 W extends VaadinDetailView<V> & VaadinViewHasVaadinViewObjectBinder<V> & Component>
     		  extends Window
     	   implements VaadinDetailView<V>,
			 		  VaadinDetailEditWindow<V> {

	private static final long serialVersionUID = 7719084020409366076L;

/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final UII18NService _i18n;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	protected final W _detailView;
	protected final VaadinAcceptCancelDeleteButtons _btnAcepCancDelete;

	//  OUTSIDE WORLD SUBSCRIBERS
	protected UIPresenterSubscriber<V> _saveSubscriber;
	protected UIPresenterSubscriber<V> _deleteSubscriber;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDetailEditWindowBase(final UII18NService i18n,
									  final W detailView) {
		_i18n = i18n;

		// style window
		this.center();
		this.setModal(true);
		this.setCaption("");
		this.setResizable(false);

		// Detail view
		_detailView = detailView;

		// OK | CANCEL | DELETE
		_btnAcepCancDelete = new VaadinAcceptCancelDeleteButtons(i18n);
		// - CANCEL
		_btnAcepCancDelete.addCancelButtonClickListner(event -> VaadinDetailEditWindowBase.this.close());
		// - OK
		_btnAcepCancDelete.addAcceptButtonClickListner(event -> {
															// collect ui controls values & tell
															if (_detailView.isValid()) {	
																V viewObj = _detailView.getViewObject();
																_saveSubscriber.onSuccess(viewObj);
																this.close();
															} else {
																Notification.show(_i18n.getMessage("notification.empty.fields"));
															}
											  			});
		// - DELETE
		_btnAcepCancDelete.addDeleteButtonClickListner(event -> {
															_deleteSubscriber.onSuccess(_detailView.getViewObject());
													   });

		// Layout
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		layout.addComponents(_detailView,
							 _btnAcepCancDelete);

		this.setContent(layout);
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
		V viewObj = viewObjFactory.create();

		//sets window caption
		this.setCaption(_i18n.getMessage(this.getNewItemCaptionI18NKey()));

		// bind the view object to the view
		_detailView.bindViewTo(viewObj);
		
		// set the buttons status
		_btnAcepCancDelete.setCreatingNewRecordStatus();
	}
	@Override
	public void forEditing(final V viewObj,
						   final UIPresenterSubscriber<V> saveSubscriber,
						   final UIPresenterSubscriber<V> deleteSubscriber) {
		if (viewObj == null) throw new IllegalArgumentException("contact mean view object not valid!");

		// store the subscribers
		_saveSubscriber = saveSubscriber;
		_deleteSubscriber = deleteSubscriber;

		//sets window caption
		this.setCaption(_i18n.getMessage(this.getEditItemCaptionI18NKey()));

		// bind the view object to the view
		_detailView.bindViewTo(viewObj);

		// set the buttons status
		_btnAcepCancDelete.setEditingExistingRecordStatus();
		// set the favorite status
	}

}
