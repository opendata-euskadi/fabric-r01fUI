package r01f.view;


/**
 * Models a view component that can be expanded / collapsed
 */
public interface IsCollapsible {
	/**
	 * @return <code>true</code> if the item is collapsed
	 */
	public boolean isCollapsed();
	/**
	 * @return <code>true</code> if the item is expanded
	 */
	public boolean isExpanded();
	/**
	 * Sets whether this item's children are NOT displayed
	 */
	public void collapse();
	/**
	 * Sets whether this item's children are displayed
	 */
	public void expand();
	/**
	 * Expands all items up to the root
	 */
	public void expandUpToRoot();
}
