package r01f.view;


/**
 * A marker interface for model objects that can be painted 
 */
public interface CanBePainted {	
	/**
	 * Sets a reference to the painter
	 * @param painter
	 */
	public void paintedInto(CanPaint painter);
	/**
	 * @return the data to be stored on the view
	 */
	public <T> T getDataToStoreAtViewComponent();
	/**
	 * @return the {@link ViewComponent} where the {@link ViewObject} is painted
	 */
	public <V extends CanPaint> V getViewComponentWhereItsPainted();
}
