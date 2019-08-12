package r01f.ui.presenter;

import r01f.guids.PersistableObjectOID;
import r01f.ui.viewobject.UIViewObject;

public interface UIObjectDetailPresenter<O extends PersistableObjectOID,
										 V extends UIViewObject>
  		 extends UIPresenter {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadRequested(final O oid,
								final UIPresenterSubscriber<V> subscriber);

	public void onSaveRequested(final V viewObj,
								final UIPresenterSubscriber<V> subscriber);

	public void onDeleteRequested(final O oid,
								  final UIPresenterSubscriber<V> subscriber);
}
