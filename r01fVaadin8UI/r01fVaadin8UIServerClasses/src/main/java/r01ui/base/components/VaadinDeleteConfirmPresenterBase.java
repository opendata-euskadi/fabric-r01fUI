package r01ui.base.components;

import com.google.common.base.Function;

import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.coremediator.UICOREMediatorForPersistableObjectBase;
import r01f.ui.coremediator.UICOREMediatorSubscriberBase;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.viewobject.UIViewObject;

public abstract class VaadinDeleteConfirmPresenterBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
													   V extends UIViewObject,
												  	   C extends UICOREMediatorForPersistableObjectBase<O,M,?>>
           implements UIPresenter {

	private static final long serialVersionUID = 106480819336910484L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final C _coreMediator;
	private final Function<M,V> _objToViewObj;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDeleteConfirmPresenterBase(final C coreMediator,
										   final Function<M,V> objToViewObj) {
		_coreMediator = coreMediator;
		_objToViewObj = objToViewObj;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PUBLIC METHODS 
/////////////////////////////////////////////////////////////////////////////////////////
	public void onDeleteRequested(final O objToBeDeletedOid,
								  final UIPresenterSubscriber<V> subscriber) {
		_coreMediator.delete(objToBeDeletedOid,
							 new UICOREMediatorSubscriberBase<M>(subscriber) {
									@Override
									public void onSuccess(final M obj) {
										V viewObj = _objToViewObj.apply(obj);
										subscriber.onSuccess(viewObj);
									}
							 });
	}
}
