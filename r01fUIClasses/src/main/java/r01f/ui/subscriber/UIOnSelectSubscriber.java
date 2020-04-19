package r01f.ui.subscriber;

/**
 * A subscriber for [select] operations
 */
public interface UIOnSelectSubscriber<T> {
	public void onSelect(final T item);
}
