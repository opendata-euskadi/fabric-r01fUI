package r01f.ui.presenter;

import org.slf4j.Logger;

import r01f.patterns.OnErrorSubscriber;
import r01f.patterns.OnSuccessSubscriber;
import r01f.patterns.Subscriber;
import r01f.ui.subscriber.UISubscriber;

@FunctionalInterface
public interface UIPresenterSubscriber<T>
		 extends UISubscriber<T> {
	// just a marker interface

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
	 * 						 UIPresenterSubscriber.from(result -> { ..on success.. },
	 * 										   			th -> {.. on error..}));
	 * </pre>
	 * @param <T>
	 * @param onSuccess
	 * @param onError
	 * @return
	 */
	public static <T> UIPresenterSubscriber<T> from(final OnSuccessSubscriber<T> onSuccess,
										   			final OnErrorSubscriber onError) {
		// Create a subscriber delegating to the onSuccess & onError subscribers
		return new UIPresenterSubscriber<T>() {
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
	public static <T> UIPresenterSubscriber<T> nop() {
		return UIPresenterSubscriber.from(result -> {},	// do nothing on success
											 error -> {});	// do nothing on error
	}
	public static <T> UIPresenterSubscriber<T> log(final Logger log) {
		return UIPresenterSubscriber.from(result -> {
														log.debug("{} on-success subscriber",UIPresenterSubscriber.class);
									   				},
						      			  error -> {
							   							log.error("{} on-error subscriber: {}",UIPresenterSubscriber.class,error.getMessage(),error);
						      			 		   });
	}
}
