package r01f.ui.vaadin.serverpush;

/**
 * Interface for types that listens to server-pushed messages
 * These messages are typically posted by a broadcaster like {@link VaadinServerPushedMessagesBroadcaster}
 * in which listeners registers to receive the posts
 * ... usually the message listener is the vaadin UI which MUST implement {@link VaadinServerPushedMessagesBroadcastListener}
 * @param <M>
 */
public interface VaadinServerPushedMessagesBroadcastListener<M extends VaadinServerPushedMessage> {
	/**
	 * Does the listener accepts messages of the given type?
	 * @param <M2>
	 * @param messageType
	 * @return
	 */
	public <M2 extends VaadinServerPushedMessage> boolean acceptsMessagesOfType(final Class<M2> messageType);
    /**
     * Receive the broadcasted message
     * @param message
     */
    public void receiveBroadcast(final M message);
}