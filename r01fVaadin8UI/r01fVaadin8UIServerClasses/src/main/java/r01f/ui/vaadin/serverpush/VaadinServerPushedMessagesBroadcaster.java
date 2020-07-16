package r01f.ui.vaadin.serverpush;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

import lombok.extern.slf4j.Slf4j;
import r01f.concurrent.ExecutorServiceManager;
import r01f.util.types.collections.CollectionUtils;

/**
 * Server-side broadcaster for server push events
 * Broadcasts messages to to UIs in other user sessions
 * To do so, UIs MUST implement {@link VaadinServerPushedMessagesBroadcastListener} and
 * implement a receiveBroadcast(...) method
 *
 * The typical scenario is:
 * 		a) The CORE post a message at the guava {@link EventBus} signaling a server event (ie someone has pushed a panic button
 *		 which has sent a message to a REST endpoint)
 *	  b) the CORE upon persisting the raised alarm posts a message to the {@link EventBus}
 *	  c) the UI is subscribed to that type of messages at the {@link EventBus}
 *	  d) when handling the {@link EventBus}-received message, another message is posted
 *		 to the {@link VaadinServerPushedMessagesBroadcaster} which in turn invokes the receiveBroadcast(...)
 *		 method on every registered listener (instances of {@link VaadinServerPushedMessagesBroadcastListener})
 *
 * BEWARE!
 * 		Server requests for different UIs are processed concurrently in different threads of the application server
 * 		... so locking the threads properly is very important to avoid deadlock situations
 * 		The [broadcaster] registers the UIs and safely broadcasts messages to them
 * 		To avoid deadlocks, the messages should be sent to other UIs through a message queue in a separate thread
 * 		from a single-threaded {@link ExecutorService}
 *
 * see https://vaadin.com/docs/v8/framework/advanced/advanced-push.html
 */
@Slf4j
@Singleton	// BEWARE!!!!!!!! MUST be a singleton
public class VaadinServerPushedMessagesBroadcaster
  implements Serializable {

	private static final long serialVersionUID = -7954570718959285145L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// Single-threaded ExecutorService in charge of send the messages to other UIs
	private final ExecutorService _executorService;	 //Executors.newSingleThreadExecutor();

	// Other UIs listening for messages
	private final LinkedList<VaadinServerPushedMessagesBroadcastListener<? extends VaadinServerPushedMessage>> _listeners = new LinkedList<>();
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public VaadinServerPushedMessagesBroadcaster(final ExecutorServiceManager executorServiceManager) {
		_executorService = executorServiceManager.getExecutorService();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public synchronized <M extends VaadinServerPushedMessage> void register(final VaadinServerPushedMessagesBroadcastListener<M> listener) {
		_listeners.add(listener);
		log.info("[server-push]: {} server push broadcast listeners registered",
				 _listeners.size());
	}

	public synchronized <M extends VaadinServerPushedMessage> void unregister(final VaadinServerPushedMessagesBroadcastListener<M> listener) {
		_listeners.remove(listener);
		log.info("[server-push]: {} server push broadcast listeners registered",
				 _listeners.size());
	}
	/**
	 * This method is called when "someone" at the server-side wants all UIs to be updated
	 * (ie show a notification message for an event)
	 * @param <M>
	 * @param message
	 */
	public synchronized <M extends VaadinServerPushedMessage>  void broadcast(final M message) {
		if (CollectionUtils.isNullOrEmpty(_listeners)) {
			log.warn("NO server pusth broadcast listener registered!");
			return;		// nothing to do
		}
		log.warn("Broadcasting a server push message...");
		for (final VaadinServerPushedMessagesBroadcastListener<? extends VaadinServerPushedMessage> listener : _listeners) {
			_executorService.execute(new Runnable() {
											@Override @SuppressWarnings("unchecked")
											public void run() {
												if (listener.acceptsMessagesOfType(message.getClass())) {
													log.info("...sending server-push message to a listener of type: {}",
															 listener.getClass());
													((VaadinServerPushedMessagesBroadcastListener<M>)listener).receiveBroadcast(message);
												} else {
													log.debug("server push broadcast listener of type " + listener.getClass() + " does NOT accespts messages of type " + message.getClass());
												}
											}
									 });
		}
	}

}
