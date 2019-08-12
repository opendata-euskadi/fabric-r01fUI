package r01f.ui.subscriber;

import org.slf4j.Logger;

import r01f.patterns.OnErrorSubscriber;
import r01f.patterns.OnSuccessSubscriber;
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
/////////////////////////////////////////////////////////////////////////////////////////
//	UTIL
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Utility to create a {@link Subscriber} fron an {@link OnSuccessSubscriber} and
	 * an {@link OnErrorSubscriber}
	 * ... this is handy when a {@link Subscriber} is required as a method param but
	 *     one want to use lambda expressions
	 * <pre class='brush:java'>
	 * 		util.doSomething(params,
	 * 						 UISubscriber.from(result -> { ..on success.. },
	 * 										   th -> {.. on error..}));
	 * </pre>
	 * @param <T>
	 * @param onSuccess
	 * @param onError
	 * @return
	 */
	public static <T> UISubscriber<T> from(final OnSuccessSubscriber<T> onSuccess,
										   final OnErrorSubscriber onError) {
		// Create a subscriber delegating to the onSuccess & onError subscribers
		return new UISubscriber<T>() {
						@Override
						public void onSuccess(final T result) {
							onSuccess.onSuccess(result);
						}
						@Override
						public void onError(final Throwable th) {
							onError.onError(th);
						}
			   };
	}
	public static <T> UISubscriber<T> nop() {
		return UISubscriber.from(result -> {},	// do nothing on success
							     error -> {});	// do nothing on error
	}
	public static <T> UISubscriber<T> log(final Logger log) {
		return UISubscriber.from(result -> {
												log.debug("{} on-success subscriber",UISubscriber.class);
										   },
							      error -> {
								   				log.error("{} on-error subscriber: {}",UISubscriber.class,error.getMessage(),error);
							   			   });
	}
}
