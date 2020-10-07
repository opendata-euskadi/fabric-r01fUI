package com.google.gwt.event.logical.shared; 

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasTreeViewItemCollapseHandlers<S> extends HasHandlers {
	/**
	 * Adds a {@link TreeViewItemCollapseEvent} handler.
	 * 
	 * @param handler the handler
	 * @return the registration for the event
	 */
  HandlerRegistration addItemCollapseHandler(TreeViewItemCollapseHandler<S> handler);
}
