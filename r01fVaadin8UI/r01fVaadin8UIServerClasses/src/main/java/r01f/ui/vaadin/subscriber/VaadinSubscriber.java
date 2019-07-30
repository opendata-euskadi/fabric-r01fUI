package r01f.ui.vaadin.subscriber;

import r01f.patterns.Subscriber;

@FunctionalInterface
public interface VaadinSubscriber<T> 
		 extends Subscriber<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	default void onError(final Throwable th) {
		th.printStackTrace(System.out);
	}
}
