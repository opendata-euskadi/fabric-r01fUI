package r01f.ui.vaadin.coremediator;

import r01f.guids.PersistableObjectOID;
import r01f.model.API;
import r01f.model.PersistableModelObject;

public abstract class VaadinCOREMediatorForPersistableObjectBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>,
																  A extends API> 
              extends VaadinCOREMediatorBase<A> {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinCOREMediatorForPersistableObjectBase(final A api) {
		super(api);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CRUD
/////////////////////////////////////////////////////////////////////////////////////////
	public abstract M load(final O oid);
	public void load(final O oid,
				  	 final VaadinCOREMediatorSubscriber<M> subscriber) {
		try {
			M loadedObj = this.load(oid);
			subscriber.onSuccess(loadedObj);
		} catch(Throwable th) {
			subscriber.onError(th);
		}
	}
	
	public abstract M save(final M obj);
	public void save(final M obj,
				  	 final VaadinCOREMediatorSubscriber<M> subscriber) {
		try {
			M savedObj = this.save(obj);
			subscriber.onSuccess(savedObj);
		} catch(Throwable th) {
			subscriber.onError(th);
		}
	}
	
	public abstract M delete(final O oid);
	public void delete(final O oid,
					   final VaadinCOREMediatorSubscriber<M> subscriber) {
		try {
			M deletedObj = this.delete(oid);
			subscriber.onSuccess(deletedObj);
		} catch(Throwable th) {
			subscriber.onError(th);
		}
	}

}
