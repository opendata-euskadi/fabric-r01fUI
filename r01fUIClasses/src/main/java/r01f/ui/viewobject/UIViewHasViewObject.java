package r01f.ui.viewobject;

/**
 * A view that contains a R01MUIViewObject
 * @param <T>
 */
public interface UIViewHasViewObject<T extends UIViewObject> {
	public T getViewObject();
	public void setViewObject(final T viewObject);
}
