package r01f.gwt.mvp;

import com.google.gwt.event.shared.EventBus;
/**
 * EVENTHANDLER
 * -------------------------------------------------------------------------------------------------
 * EventHandlers unlike presenters that intermediates with a view, DO NOT have an associated view
 * So:
 * 	1.- Handles -and so RECEIVE- events that DO NOT affect a view; they usually affect a backend
 * 		(ie: loadRecod(), saveRecord(), etc)
 * 			In oreder to act as RECEIVER, the EventHandler must define the world-exposed interface for the event handling
 * 				-define the methods: void on[event name](event)-
 *
 * 2.- As a presenter, inside an event handling usually, the EventHandler must throw another events to inform other application
 * 	   modules of some condition. In this moment, the EventHandler acts as event EMITTER
 *      
 * A.- In order to be able to act as event RECEIVER, the EventHandler should:
 * 		A.1- Suscribe to the events fired by some EMITTER (another application module as a presenter) 
 * 		A.2- implement the event handling methods:
 * 				public void on[EventName]Event(event)	
 * 
 * B.- In order to be able to act as EMITTER, the EventHandler should:
 * 		B.1- Have access to an EventBus that let it fire events to other application modules
 * 		B.2- Expose the methods for other application components interested in the events fired by the EventHandler, be able to suscribe to them
 * 			 id:	public void add[EventName]Handler(theEventHandler)
 * 
 * To do all the wiring needed for [A] and [B], the CONTROLLER is used:
 * At the controller:
 * 		1.- The EventBus is created
 * 				EventBus _eventBus = new SimpleEventBus();
 * 		2.- The presenter and the EventHandler (if used) are created
 * 				ie: MyPresenter<EventBus> myPresenter = new MyPresenter<EventBus>();
 * 					EventHandler<EventBus> myEventHandler = new EventHandler<EventBus>();
 * 		3.- The EventBus instance is passed to the presenter just to be able to act as event EMITER: to tell something to other application modules
 * 				myPresenter.setEventBus(_eventBus);
 * 		4.- The common events are mapped in a CENTRALIZED way (ie: backEnd events)
 * 				In the case that other presenter should receive the event
 * 					eventBus.addHandler(SaveRecordEvent.TYPE,onePresenter);
 * 				In the case that an eventHandler should receive the event
 * 					eventBus.addHandler(SaveRecordEvent.TYPE,oneEventHandler);
 * 
 * 			 IMPORTANT:	
 * 				The mapping of events LEAVING (fired at) the presenter is done at the CONTROLLER and NOT at the PRESENTER 
 * 				that acts as event EMITTER as described at B.2
 * 				This is done this way because a lot of events fired at a presenter also are fired by other presenters
 * 				ie: As result of a [Save] button click, the presenter fires an onSaveRecord(theRecord) event that 
 * 				    is usually handled at a common EventHandler (RECEIVER)
 * 					If there are more than one presenters where a record is saved, it would make no sense that the
 * 					EventHandler should suscribe to the events fired by every presenter
 * 								myPresenter1.addSaveEventHandler(backEndEventHandler)
 * 								myPresenter2.addSaveEventHandler(backEndEventHandler)
 * 					The same thing -suscribing the EventHandler to an event fired by a presenter- is being done many times
 * 					(one for presenter) 
 * 					This should be done ONLY one time in a centralized way: at the CONTROLLER
 *
 */
public interface EventHandler {
	/**
	 * Set an event bus to the event handler
	 * @param eventBus event bus to set
	 */
	public void setEventBus(EventBus eventBus);
	/**
	 * Get the event bus associated with the event handler
	 * @return eventBus manipulated by the event handler.
	 */
	public EventBus getEventBus();
}
