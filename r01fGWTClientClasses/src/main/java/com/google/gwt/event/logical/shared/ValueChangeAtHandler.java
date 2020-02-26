package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.EventHandler;


public interface ValueChangeAtHandler<S,T>
         extends EventHandler {
	
	public void onValueChangeAt(ValueChangeAtEvent<S,T> event);
}
