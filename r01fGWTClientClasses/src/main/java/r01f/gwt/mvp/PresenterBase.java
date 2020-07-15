package r01f.gwt.mvp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.google.gwt.event.shared.EventBus;

/**
 * PRESENTER
 * -------------------------------------------------------------------------------------------------
 * Presenters in the MVP pattern exposes two facets:
 * 1.- They are event RECEIVERS because they handle events that changes the view 
 * 			In order to act as RECEIVER, the presenter must define the interface that exposes to the world 
 * 			to handle the event 
 * 				-they must define the metods: void on[event name](event)-
 * 
 * 			In order to be able to change the view in response of the event handle, the VIEW must expose
 * 			to the PRESENTER an interface with methods like:
 * 				public void changeLabelText(String newText)
 *
 * 2.- They are EMITTERS of events in two scenarios:
 * 			2.1 - In the handling of a received event the presenter must fire other events to inform other application modules
 * 				  of some condition
 * 
 * 			2.2.- The presenters handle some view events that involves other application parts and therefore end with an event
 * 				  firing (the presenter is an EMITTER) 
 * 				  The clearer sample is a [save] button click event that is handled at the presenter because it fires 
 * 				  a save event.  
 *     			  NOTE that the events associated with view components that only act within the view MUST be handled INSIDE
 *     				   the view and NOT at the presenter
 *     				   An example of this is the onClick event of a button that only hides some other view component
 *     
 *     			  The presenter ONLY SHOULD handle view component's events that implies other application module, normally
 *     			  firing some event
 *     
 *  			  In order to be able to suscribe (be RECEIVER) of the view's component's events (ie save button):
 *     				2.2.1 	The component (acting as EMITTER) should expose a method for the receiver (the handler) to suscribe:
 *     						ie: 	saveButton.addClickHandler(clickHandler)
 *     				2.2.2 	In order to avoid the neccesity for the presenter to know the button implementation 
 *     						the VIEW should expose the PRESENTER an interfaz that let it "reach" the component's suscription
 *     						method
 *     						ie: 	view.addSaveButtonClickHandler(clickHandler)
 *     
 * So:
 * A.- In order to be able to act as event RECEIVER, the presenter should:
 * 		A.1- Suscribe to some EMISOR events (another application module) 
 * 		A.2- Implement the event handling methods
 * 				public void on[EventName]Event(event)	
 * 
 * B.- In order to be able to act as EMITTER, the presenter should:
 * 		B.1- Have access to an EventBus that let it fire events to other application modules
 * 		B.2- Expose methods to let other components interested in that events fired by the presenter (the RECEIVERS), suscribe to them  
 * 			 ie:	public void add[EventName]Handler(theEventHandler)
 * 
 * To do all the necessary wiring for [A] and [B] another piece exists in the architecture: the CONTROLLER
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
 * SEE: http://stackoverflow.com/questions/2951621/gwt-custom-events
 * 
 * @param <B> the event bus
 */
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class PresenterBase           
           implements EventHandler {
///////////////////////////////////////////////////////////////////////////////
//	FIELDS
///////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private EventBus _eventBus;
	
///////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
///////////////////////////////////////////////////////////////////////////////
	public PresenterBase(final EventBus newEventBus) {
		_eventBus = newEventBus;
	}
///////////////////////////////////////////////////////////////////////////////
//	EVENT HANDLING OF UI COMPONENTS THAT INVOLVES OTHER APPLICATION MODULES
//	(usually through an event fired at the presenter when handling an ui event)
///////////////////////////////////////////////////////////////////////////////
	/**
	 * In this method implementation, every event involving other application modules
	 * should be handled
	 * ie: the [save] button is clicked 
	 * 
	 * In order to do this, the view should expose an interface to let the presenter
	 * suscribe to the ui component's fired events
	 * ie:	view.addSaveButtonClickHandler(Handler)
	 */
	public abstract void handleUIEvents();

}
