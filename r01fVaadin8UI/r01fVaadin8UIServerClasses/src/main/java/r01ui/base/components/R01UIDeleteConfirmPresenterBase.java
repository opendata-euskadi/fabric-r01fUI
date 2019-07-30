package r01ui.base.components;

import com.google.common.base.Function;

import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.vaadin.coremediator.VaadinCOREMediatorForPersistableObjectBase;
import r01f.ui.vaadin.coremediator.VaadinCOREMediatorSubscriberBase;
import r01f.ui.vaadin.presenter.VaadinPresenter;
import r01f.ui.vaadin.presenter.VaadinPresenterSubscriber;
import r01f.ui.vaadin.viewobject.VaadinViewObject;

public abstract class R01UIDeleteConfirmPresenterBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
													  V extends VaadinViewObject,
												  	  C extends VaadinCOREMediatorForPersistableObjectBase<O,M,?>>
           implements VaadinPresenter {

	private static final long serialVersionUID = 106480819336910484L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final C _coreMediator;
	private final Function<M,V> _objToViewObj;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public R01UIDeleteConfirmPresenterBase(final C coreMediator,
										   final Function<M,V> objToViewObj) {
		_coreMediator = coreMediator;
		_objToViewObj = objToViewObj;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PUBLIC METHODS 
/////////////////////////////////////////////////////////////////////////////////////////
	public void onDeleteRequested(final O objToBeDeletedOid,
								  final VaadinPresenterSubscriber<V> subscriber) {
		_coreMediator.delete(objToBeDeletedOid,
							 new VaadinCOREMediatorSubscriberBase<M>(subscriber) {
									@Override
									public void onSuccess(final M obj) {
										V viewObj = _objToViewObj.apply(obj);
										subscriber.onSuccess(viewObj);
									}
							 });
	}
}
