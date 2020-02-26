package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.EventHandler;


public interface TreeViewItemCollapseHandler<S>
         extends EventHandler {
	
	public void onItemCollapse(TreeViewItemCollapseEvent<S> event);
}
