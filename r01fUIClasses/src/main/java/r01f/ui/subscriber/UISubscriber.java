package r01f.ui.subscriber;

import r01f.patterns.Subscriber;

@FunctionalInterface
public interface UISubscriber<T> 
		 extends Subscriber<T>,
		  		 UIOnSuccessSubscriber<T>,
		  		 UIOnErrorSubscriber {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	default void onError(final Throwable th) {
		th.printStackTrace(System.out);
	}
}
