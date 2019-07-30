package r01f.ui.vaadin.coremediator;

import r01f.ui.vaadin.presenter.VaadinPresenterSubscriber;
import r01f.ui.vaadin.subscriber.VaadinSubscriberBase;

public abstract class VaadinCOREMediatorSubscriberBase<T> 
		      extends VaadinSubscriberBase<T> 
		   implements VaadinCOREMediatorSubscriber<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinCOREMediatorSubscriberBase() {
		this(null);
	}
	public VaadinCOREMediatorSubscriberBase(final VaadinPresenterSubscriber<?> subscriber) {
		super(subscriber);
	}
}
