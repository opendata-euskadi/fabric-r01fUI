package r01f.ui.presenter;

import r01f.ui.subscriber.UISubscriberBase;

public abstract class UIPresenterSubscriberBase<T> 
		      extends UISubscriberBase<T> 
		   implements UIPresenterSubscriber<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UIPresenterSubscriberBase() {
		this(null);
	}
	public UIPresenterSubscriberBase(final UIPresenterSubscriber<?> other) {
		super(other);
	}
}
