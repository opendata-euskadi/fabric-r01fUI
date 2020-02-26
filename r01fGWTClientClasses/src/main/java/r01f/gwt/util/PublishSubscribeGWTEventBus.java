package r01f.gwt.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * {@link EventBus} used for publish / suscription of events that can "transport" anything, from a simple text 
 * to a GWT component like a label (see http://code.google.com/p/gwt-structs/wiki/Eventbus)
 * <pre class='brush:java'>
 * 		// Publish an event when clicking the button
 *		final EventPublisher<String> pub = new EventPublisher<String>();
 * 
 * 		final Button b = new Button("Send Message");
 *		b.addClickHandler(new ClickHandler() {
 *		        				@Override
 *		        				public void onClick(ClickEvent event) {
 *		                			pub.publish(t.getText(), "com.eventbus.test");
 *		        				}
 *						 });
 *
 *
 *		// Subscribe to the event
 *		final TextBox t1 = new TextBox();
 *		EventSubscriber<String> subs = new EventSubscriber<String>();
 *		s.addHandler("com.eventbus.test", 
 *					 new PublishedEventHandler<String>() {
 *		        			@Override
 *		        			public void onSubscribedEvent(PublishedEvent<String> e) {
 *		                		t1.setText(e.getData());
 *		        			}
 *					 });
 * </pre>
 */
public class PublishSubscribeGWTEventBus {
///////////////////////////////////////////////////////////////////////////////
//	EVENT BUS INSTANCE
///////////////////////////////////////////////////////////////////////////////	
    @Getter private static final EventBus eventBus = new SimpleEventBus();
    
///////////////////////////////////////////////////////////////////////////////
//	EVENTO
///////////////////////////////////////////////////////////////////////////////    
	@AllArgsConstructor
	public static class PublishedEvent<T> 
	            extends GwtEvent<PublishedEventHandler<T>> {
		
		@Getter @Setter private T data;
		
		public static Type<PublishedEventHandler<?>> TYPE = new Type<PublishedEventHandler<?>>();

		@Override @SuppressWarnings({ "rawtypes","unchecked" })
		public final Type<PublishedEventHandler<T>> getAssociatedType() {
			return (Type)TYPE;
		}
		@Override
		protected void dispatch(PublishedEventHandler<T> handler) {
			handler.onSubscribedEvent(this);
		}
    }
///////////////////////////////////////////////////////////////////////////////
//	EVENT HANDLER
///////////////////////////////////////////////////////////////////////////////
    public static abstract class PublishedEventHandler<T> 
                      implements EventHandler {
    	public abstract void onSubscribedEvent(PublishedEvent<T> e);
    }
///////////////////////////////////////////////////////////////////////////////
//	PUBLISHER
///////////////////////////////////////////////////////////////////////////////
	public class EventPublisher<T> {
		public void publish(final String code,
							final T data) {
			PublishSubscribeGWTEventBus.getEventBus()
									   .fireEventFromSource(new PublishedEvent<T>(data),code);
		}
	}
///////////////////////////////////////////////////////////////////////////////
//	SUBSCRIBER
///////////////////////////////////////////////////////////////////////////////	
	public class EventSubscriber<T> {
		public HandlerRegistration addHandler(final String code,
											  final PublishedEventHandler<T> handler) {
			return PublishSubscribeGWTEventBus.getEventBus()
											  .addHandlerToSource(PublishedEvent.TYPE,code,handler);
		}
	}
}
