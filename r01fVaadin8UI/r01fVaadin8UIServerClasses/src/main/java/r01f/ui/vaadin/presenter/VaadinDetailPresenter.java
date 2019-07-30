package r01f.ui.vaadin.presenter;

import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.ui.vaadin.viewobject.VaadinViewObject;

public interface VaadinDetailPresenter<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
											V extends VaadinViewObject> 
  		 extends VaadinPresenter {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public void onLoadRequested(final O oid,
								final VaadinPresenterSubscriber<V> subscriber);
	
	public void onSaveRequested(final M obj,
								final VaadinPresenterSubscriber<V> subscriber);
	
	public void onDeleteRequested(final O oid,
								  final VaadinPresenterSubscriber<V> subscriber);
}
