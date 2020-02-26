package com.google.gwt.event.logical.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.google.gwt.event.shared.GwtEvent;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class ValueChangeAtEvent<S,T>
     extends GwtEvent<ValueChangeAtHandler<S,T>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private static Type<ValueChangeAtHandler<?,?>> _TYPE = new Type<ValueChangeAtHandler<?,?>>();

	@Override @SuppressWarnings({"unchecked", "rawtypes"})
	public final Type<ValueChangeAtHandler<S,T>> getAssociatedType() {
		// The instance knows its ValueChangeAtHandler is of type S,T, but the TYPE
		// field itself does not, so an unsafe cast must be done here.
		return (Type)_TYPE;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final S _source;
	@Getter private final T _value;
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Fires a value change event on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * @param <S> the value change source type
	 * @param <T> the old value type
	 * @param source the source of the handlers
	 * @param sourceItem source item
	 * @param value the value
	 */
	public static <S,T> void fire(final HasValueChangeAtHandlers<S,T> source,
							      final S sourceItem,final T value) {
		if (_TYPE != null) {
			ValueChangeAtEvent<S,T> event = new ValueChangeAtEvent<S,T>(sourceItem,value);
			source.fireEvent(event);
		}
	}
	/**
	 * Fires value change event if the old value is not equal to the new value.
	 * Use this call rather than making the decision to short circuit yourself for
	 * safe handling of null.
	 * @param <S> the value change source type
	 * @param <T> the old value type
	 * @param source the source of the handlers
	 * @param sourceItem source item
	 * @param oldValue the oldValue, may be null
	 * @param newValue the newValue, may be null
	 */
	public static <S,T> void fireIfNotEqual(final HasValueChangeAtHandlers<S,T> source,
											final S sourceItem,
										    final T oldValue,final T newValue) {
		if (_shouldFire(oldValue, newValue)) {
			ValueChangeAtEvent<S,T> event = new ValueChangeAtEvent<S,T>(sourceItem,newValue);
			source.fireEvent(event);
		}
	}
	/**
	 * Convenience method to allow subtypes to know when they should fire a value
	 * change event in a null-safe manner.
	 * 
	 * @param <T> value type
	 * @param source the source
	 * @param oldValue the old value
	 * @param newValue the new value
	 * @return whether the event should be fired
	 */
	protected static <T> boolean _shouldFire(final T oldValue,final T newValue) {
		return _TYPE != null 
			&& oldValue != newValue
			&& (oldValue == null || !oldValue.equals(newValue));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void dispatch(final ValueChangeAtHandler<S,T> handler) {
		handler.onValueChangeAt(this);
	}
}
