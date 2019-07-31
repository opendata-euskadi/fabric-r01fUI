package r01f.ui.presenter;

import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.viewobject.UIViewObject;

public interface UIObjectDetailPresenter<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
										 V extends UIViewObject>
  		 extends UIPresenter {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadRequested(final O oid,
								final UIPresenterSubscriber<V> subscriber);

	public void onSaveRequested(final M obj,
								final UIPresenterSubscriber<V> subscriber);

	public void onDeleteRequested(final O oid,
								  final UIPresenterSubscriber<V> subscriber);
}
