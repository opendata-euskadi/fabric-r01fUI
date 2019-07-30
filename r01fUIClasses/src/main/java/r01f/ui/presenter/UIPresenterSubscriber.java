package r01f.ui.presenter;

import r01f.ui.subscriber.UISubscriber;

@FunctionalInterface
public interface UIPresenterSubscriber<T> 
		 extends UISubscriber<T> {
	// just a marker interface
}
