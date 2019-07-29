package r01f.ui.vaadin.viewobject;

/**
 * A view that contains a R01MUIViewObject
 * @param <T>
 */
public interface VaadinViewHasViewObject<T extends VaadinViewObject> {
	public T getViewObject();
	public void setViewObject(final T viewObject);
}
