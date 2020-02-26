package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.EventHandler;


public interface TreeViewItemExpandHandler<S>
         extends EventHandler {
	
	public void onItemExpand(TreeViewItemExpandEvent<S> event);
}
