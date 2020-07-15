package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasTreeViewItemExpandHandlers<S> extends HasHandlers {
	/**
	 * Adds a {@link TreeViewItemExpandEvent} handler.
	 * 
	 * @param handler the handler
	 * @return the registration for the event
	 */
  HandlerRegistration addItemExpandHandler(TreeViewItemExpandHandler<S> handler);
}
