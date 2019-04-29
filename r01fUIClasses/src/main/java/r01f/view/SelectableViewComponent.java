package r01f.view;

/**
 * A view component that can be selected / de-selected
 */
public interface SelectableViewComponent
		 extends ViewComponent {
	/**
	 * @return <code>true</code> if this item is selected
	 */
	public boolean isSelected();
	/**
	 * Selects this item.
	 */
	public void setSelected();
	/**
	 * DeSelect this item
	 */
	public void setDeSelected();
	/**
	 * Toggles the selection status
	 */
	public void toggleSelected();

}
