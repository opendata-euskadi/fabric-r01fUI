package r01f.ui.vaadin.presenter;

import r01f.ui.vaadin.subscriber.VaadinSubscriber;

@FunctionalInterface
public interface VaadinPresenterSubscriber<T> 
		 extends VaadinSubscriber<T> {
	// just a marker interface
}
