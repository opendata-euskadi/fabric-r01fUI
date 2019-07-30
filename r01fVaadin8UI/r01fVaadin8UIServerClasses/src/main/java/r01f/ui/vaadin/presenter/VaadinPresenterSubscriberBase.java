package r01f.ui.vaadin.presenter;

import r01f.ui.vaadin.subscriber.VaadinSubscriberBase;

public abstract class VaadinPresenterSubscriberBase<T> 
		      extends VaadinSubscriberBase<T> 
		   implements VaadinPresenterSubscriber<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinPresenterSubscriberBase() {
		this(null);
	}
	public VaadinPresenterSubscriberBase(final VaadinPresenterSubscriber<?> other) {
		super(other);
	}
}
