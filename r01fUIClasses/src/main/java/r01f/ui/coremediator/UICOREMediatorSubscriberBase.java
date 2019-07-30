package r01f.ui.coremediator;

import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.subscriber.UISubscriberBase;

public abstract class UICOREMediatorSubscriberBase<T> 
		      extends UISubscriberBase<T> 
		   implements UICOREMediatorSubscriber<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UICOREMediatorSubscriberBase() {
		this(null);
	}
	public UICOREMediatorSubscriberBase(final UIPresenterSubscriber<?> subscriber) {
		super(subscriber);
	}
}
