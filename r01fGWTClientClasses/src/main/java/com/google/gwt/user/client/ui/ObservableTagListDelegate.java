package com.google.gwt.user.client.ui;

import java.util.Collection;

import r01f.model.facets.view.HasCaption;
import r01f.patterns.CommandOn;
import r01f.patterns.reactive.ForChildAdditionOrRemovalObserver;
import r01f.patterns.reactive.ObservableBase;
import r01f.patterns.reactive.ObservableForChildAdditionOrRemoval;
import r01f.patterns.reactive.Observer;
import r01f.util.types.collections.CollectionUtils;

     class ObservableTagListDelegate<T extends HasCaption> 
   extends ObservableBase
implements ObservableForChildAdditionOrRemoval<T> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void notifyObserversAboutChildAddition(final T addedChild) {
		CollectionUtils.executeOn(this.allObservers(),
								  new CommandOn<Observer>() {
											@Override @SuppressWarnings("unchecked")
											public void executeOn(final Observer obs) {											
												ForChildAdditionOrRemovalObserver<T> viewObserver = (ForChildAdditionOrRemovalObserver<T>)obs;
												viewObserver.onChildAddition(addedChild);
											}
								  });
	}
	@Override
	public void notifyObserversAboutChildRemoval(final T removedChild) {
		CollectionUtils.executeOn(this.allObservers(),
								  new CommandOn<Observer>() {
											@Override @SuppressWarnings("unchecked")
											public void executeOn(final Observer obs) {											
												ForChildAdditionOrRemovalObserver<T> viewObserver = (ForChildAdditionOrRemovalObserver<T>)obs;
												viewObserver.onChildRemoval(removedChild);
											}
								  });
	}
	@Override
	public <O extends Observer> Collection<Observer> observersOfType(final Class<O> observerType) {
		return null;
	}
	 
}
