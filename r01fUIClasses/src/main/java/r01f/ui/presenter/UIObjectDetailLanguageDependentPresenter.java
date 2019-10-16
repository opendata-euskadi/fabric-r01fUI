package r01f.ui.presenter;

import r01f.guids.PersistableObjectOID;
import r01f.locale.Language;
import r01f.ui.viewobject.UIViewObject;

public interface UIObjectDetailLanguageDependentPresenter<O extends PersistableObjectOID,
										 				  V extends UIViewObject>
  		 extends UIPresenter {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadRequested(final O oid,
								final Language lang,
								final UIPresenterSubscriber<V> subscriber);

	public void onSaveRequested(final V viewObj,
								final Language lang,
								final UIPresenterSubscriber<V> subscriber);

	public void onDeleteRequested(final O oid,
								  final Language lang,
								  final UIPresenterSubscriber<V> subscriber);
}
