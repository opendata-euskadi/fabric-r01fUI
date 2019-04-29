package r01f.view;

import r01f.patterns.reactive.Observable;


public interface ObservableLazyLoadedView 
	     extends Observable {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Notify view observers (normally the model) that the view needs data
	 * (ie: when a node is clicked in a lazy-loaded tree view and child nodes are needed) 
	 */
	public void notifiyObserversAboutNeedOfLazyLoadedData();
}
