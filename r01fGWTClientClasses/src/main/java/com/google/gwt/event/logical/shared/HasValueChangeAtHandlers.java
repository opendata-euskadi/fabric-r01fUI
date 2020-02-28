package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasValueChangeAtHandlers<S,T>
		 extends HasHandlers {
	/**
	 * Adds a {@link ValueChangeEvent} handler.
	 *
	 * @param handler the handler
	 * @return the registration for the event
	 */
  HandlerRegistration addValueChangeAtHandler(ValueChangeAtHandler<S,T> handler);
}
