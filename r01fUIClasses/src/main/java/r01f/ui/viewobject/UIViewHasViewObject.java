package r01f.ui.viewobject;

/**
 * A view that contains a R01MUIViewObject
 * @param <T>
 */
public interface UIViewHasViewObject<T extends UIViewObject> {
	/**
	 * Returns the view object
	 * @return
	 */
	public T getViewObject();
	/**
	 * Sets the view object
	 * 
	 * @param uiViewObject
	 */
	public void setViewObject(final T uiViewObject);
}
