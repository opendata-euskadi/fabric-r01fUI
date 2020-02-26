package com.google.gwt.user.client.ui;

import r01f.types.hierarchy.HasChildren;
import r01f.view.CanBePainted;
import r01f.view.ViewObject;



public interface HasTreeViewItems<P extends TreeViewItemsContainCapable<T>,
								  T extends CanBePainted>
	     extends HasChildren<TreeViewItem<T>> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds an item to the item container
	 * @param item the item
	 * @return the {@link TreeView} item container to enable fluent item insertion
	 */
	public HasTreeViewItems<P,T> addItem(final TreeViewItem<T> item);
	/**
	 * Adds a {@link ViewObject} to the item container
	 * @param canBePainted
	 * @return the {@link TreeView} item container to enable fluent item insertion
	 */
	public void addItem(final T canBePainted);
	/**
	 * Inserts a child tree item at the specified index containing the specified {@link ViewObject}.
	 * @param canBePainted the paintable {@link ViewObject} to be added
	 * @param index the index where the item will be inserted
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public TreeViewItem<T> insertItemAt(final T canBePainted,
									 	final int index);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Removes an item
	 * @param isItem
	 */
	public void removeItem(final TreeViewItem<T> isItem);
}
