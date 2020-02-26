package com.google.gwt.event.logical.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.google.gwt.event.shared.GwtEvent;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class TreeViewItemExpandEvent<S>
     extends GwtEvent<TreeViewItemExpandHandler<S>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private static Type<TreeViewItemExpandHandler<?>> _TYPE =  new Type<TreeViewItemExpandHandler<?>>();

	@Override @SuppressWarnings({"unchecked", "rawtypes"})
	public final Type<TreeViewItemExpandHandler<S>> getAssociatedType() {
		// The instance knows its TreeViewItemExpandHandler is of type S, but the TYPE
		// field itself does not, so an unsafe cast must be done here.
		return (Type)_TYPE;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final S _source;
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Fires a tree view item expand event on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * @param <S> the value change source type
	 * @param source the source of the handlers
	 * @param sourceItem source item
	 */
	public static <S> void fire(final HasTreeViewItemExpandHandlers<S> source,
							    final S sourceItem) {
		if (_TYPE != null) {
			TreeViewItemExpandEvent<S> event = new TreeViewItemExpandEvent<S>(sourceItem);
			source.fireEvent(event);
		}
	}
	@Override
	protected void dispatch(final TreeViewItemExpandHandler<S> handler) {
		handler.onItemExpand(this);
	}
}
