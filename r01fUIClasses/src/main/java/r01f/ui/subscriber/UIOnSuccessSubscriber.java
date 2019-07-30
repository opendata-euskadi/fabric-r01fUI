package r01f.ui.subscriber;

import r01f.patterns.OnSuccessSubscriber;

@FunctionalInterface
public interface UIOnSuccessSubscriber<T> 
		 extends OnSuccessSubscriber<T> {
	// just extend
}
