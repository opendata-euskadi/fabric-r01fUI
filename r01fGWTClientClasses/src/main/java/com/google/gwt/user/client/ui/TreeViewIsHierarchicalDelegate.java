package com.google.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.Element;

import lombok.RequiredArgsConstructor;
import r01f.types.hierarchy.IsHierarchical;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;
import r01f.view.CanBePainted;
import r01f.view.ViewObject;

@RequiredArgsConstructor
       class TreeViewIsHierarchicalDelegate<P extends TreeViewItemsContainCapable<T> & HasTreeViewItems<TreeViewItem<T>,T>,
       										T extends CanBePainted>
  implements HasTreeViewItems<TreeViewItem<T>,T>,
  			 IsHierarchical<TreeViewItem<T>> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Transforms a {@link CanBePainted} {@link ViewObject} to a GWT {@link Widget}
	 */
	private final CanBePaintedAsWidget<T> _viewObjectToWidgetTransformer;
	/**
	 * The container where these items are stored (must be a {@link TreeViewItemsContainCapable})
	 */
	private final P _itemOrTree;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The parent item if this item is NOT a root node (if this is the case _parentItem is null)
	 */
	private TreeViewItem<T> _parentItem;
	/**
	 * The children
	 */
	private List<TreeViewItem<T>> _children;
/////////////////////////////////////////////////////////////////////////////////////////
//  Part of TreeViewItemsContainCapable interface
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isItem() {
		return _itemOrTree instanceof TreeViewItem;
	}
	public boolean isTree() {
		return _itemOrTree instanceof TreeView;
	}
	@SuppressWarnings("unchecked")
	public TreeViewItem<T> asTreeViewItem() {
		assert(this.isItem());
		return (TreeViewItem<T>)_itemOrTree;
	}
	@SuppressWarnings("unchecked")
	public TreeView<T> asTreeView() {
		assert(this.isTree());
		return (TreeView<T>)_itemOrTree;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public TreeViewItem<T> getDirectAncestor() {
		return _parentItem;
	}
	@Override
	public void setDirectAncestor(final TreeViewItem<T> parent) {
		_parentItem = parent;
	}
	@Override
	public TreeViewItem<T> branchRoot() {
		TreeViewItem<T> outBranchRoot = null;
		if (this.isItem()) {
			TreeViewItem<T> currItem = _itemOrTree.asTreeViewItem();
			TreeViewItem<T> currItemParent = currItem.getDirectAncestor();
			while (currItemParent != null) {
				currItem = currItemParent;
				currItemParent = currItem.getDirectAncestor();
			}
			outBranchRoot = currItem;
		}
		return outBranchRoot;
	}
	@Override
	public boolean isDescendantOf(final TreeViewItem<T> ancestor) {
		boolean outIsDescendant = false;
		if (this.isItem()) {
			TreeViewItem<T> item = _itemOrTree.asTreeViewItem();
			TreeViewItem<T> currItemParent = item.getDirectAncestor();
			while (currItemParent != null) {
				if (currItemParent == ancestor) {
					outIsDescendant = true;
					break;
				}
				currItemParent = currItemParent.getDirectAncestor();
			}
		}
		return outIsDescendant;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public TreeViewItem<T> addChild(final TreeViewItem<T> item) {
		this.addItem(item);
		return item;
	}
	@Override
	public void addChildren(final Collection<TreeViewItem<T>> items) {
		if (CollectionUtils.hasData(items)) {
			for (TreeViewItem<T> item : items) this.addChild(item);
		}
	}
	@Override
	public HasTreeViewItems<TreeViewItem<T>,T> addItem(final TreeViewItem<T> item) {
		this.insertChildAt(item,
						   this.getChildCount());
		return item;
	}
	@Override
	public void addItem(final T canBePainted) {
		TreeViewItem<T> item = new TreeViewItem<T>(_viewObjectToWidgetTransformer,
											 	   canBePainted);
		this.addChild(item);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public TreeViewItem<T> insertItemAt(final T canBePainted,
									 	final int index) throws IndexOutOfBoundsException {
		TreeViewItem<T> item = new TreeViewItem<T>(_viewObjectToWidgetTransformer,
											 	   canBePainted);
		this.insertChildAt(item,
						   index);
		return item;
	}
	@Override
	public TreeViewItem<T> insertChildAt(final TreeViewItem<T> item,
									  	 final int index) {
		if (_mayRemoveItemFromParent(item)) _removeFromParent(item);	// Detach item from existing parent.

		// Check the index
		if (index < 0 || index > this.getChildCount()) throw new IndexOutOfBoundsException();

		// If the container is not prepared to adopt children... tell it to be prepared
		if (_children == null) {
			_itemOrTree.prepareToAdoptChildren();	// Tell the parent to be prepared to adopt children
			_children = new ArrayList<TreeViewItem<T>>();
		}

		// Physical attach.
		// GWT.log(">>>Physical attach: " + item.getElement() + " to " + _container.getChildContainerElement() + " at " + beforeIndex);
		if (index == this.getChildCount()) {
			_itemOrTree.getChildContainerElement()
					   .appendChild(item.getElement());
		} else {
			Element beforeElem = this.getChildAt(index)
									 .getElement();
			_itemOrTree.getChildContainerElement()
					   .insertBefore(item.getElement(),
									 beforeElem);
		}
		// Logical attach.
		_children.add(index,item);
		TreeView<T> containerTree = null;
		if (_itemOrTree.isItem()) {
			TreeViewItem<T> containerItem = _itemOrTree.asTreeViewItem();
			item.setDirectAncestor(containerItem);		// parent item = container parent item
			containerTree = containerItem.getTree(); 	// Adopted by the same tree as the parent container item
		} else {
			containerTree = _itemOrTree.asTreeView();
		}
		item.setTree(containerTree);	// Adopted by the tree

		// Attach the DOM event listeners of the item's element to the tree event listeners so the
		// tree's onBrowserEvent(event) event method also handles the items element's DOM events
//		DOM.setEventListener(item._expanderINPUTElement(),
//							 item.getTree());
//		DOM.setEventListener(item._checkINPUTElement(),
//							 item.getTree());
		// Return the event
		return item;
	}
	/**
	 * Remove a tree item from its parent if it has one.
	 * @param item the tree item to remove from its parent
	 */
	static <T extends CanBePainted> boolean _mayRemoveItemFromParent(final TreeViewItem<T> item) {
		return ((item.getDirectAncestor() != null) || (item.getTree() != null));
	}
	static <T extends CanBePainted> void _removeFromParent(final TreeViewItem<T> item) {
		if (item.getDirectAncestor() != null) {
			item.getDirectAncestor()
				.removeChild(item);	// tell the parent item to remove this item

		} else if (item.getTree() != null) {
			item.getTree()
				.removeChild(item);	// tell the parent tree to remove this item
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void removeChild(final TreeViewItem<T> item) {
		if (_children == null || !_children.contains(item)) return;

		// Physical detach the li from it's parent li.
		_itemOrTree.getChildContainerElement()
				   .removeChild(item.getElement());
		if (_itemOrTree.getChildContainerElement().getChildCount() == 0) {
			_itemOrTree.allChildrenGone();
		}

		// Logical detach.
		item.setTree(null);
		item.setDirectAncestor(null);
		_children.remove(item);
	}
	@Override
	public void removeChildAt(final int index) {
		TreeViewItem<T> item = this.getChildAt(index);
		this.removeChild(item);
	}
	@Override
	public void removeItem(final TreeViewItem<T> item) {
		if (item != null) this.removeChild(item);
	}
	@Override
	public void removeAllChilds() {
		while (this.getChildCount() > 0) {
			this.removeChild(this.getChildAt(0));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasChildren() {
		return _children != null && _children.size() > 0;
	}
	@Override
	public TreeViewItem<T> getChildAt(final int index) {
		if ((index < 0) || (index >= getChildCount())) return null;
		return _children.get(index);
	}
	@Override
	public int getChildCount() {
		if (_children == null) return 0;
		return _children.size();
	}
	@Override
	public int getChildIndex(final TreeViewItem<T> child) {
		if (_children == null) return -1;
		return _children.indexOf(child);
	}
	@Override
	public List<TreeViewItem<T>> getChildren() {
		return _children;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	SIBLINGS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<TreeViewItem<T>> getSiblings() {
		assert(this.isItem());		// a tree cannot have slibings!!

		Collection<TreeViewItem<T>> allParentChilds = _itemSlibingsContainer().getChildren();
		return Lists.getSiblings((List<TreeViewItem<T>>)allParentChilds,
								 this.asTreeViewItem());
	}
	@Override
	public List<TreeViewItem<T>> getSiblingsAfter() {
		assert(this.isItem());		// a tree cannot have slibings!!

		Collection<TreeViewItem<T>> allParentChilds = _itemSlibingsContainer().getChildren();
		return Lists.getSiblingsAfter((List<TreeViewItem<T>>)allParentChilds,
									  this.asTreeViewItem());
	}
	@Override
	public TreeViewItem<T> getNextSibling() {
		Collection<TreeViewItem<T>> allParentChilds = _itemSlibingsContainer().getChildren();
		return Lists.getNextSibling((List<TreeViewItem<T>>)allParentChilds,
									this.asTreeViewItem());
	}
	@Override
	public List<TreeViewItem<T>> getSiblingsBefore() {
		assert(this.isItem());		// a tree cannot have slibings!!

		Collection<TreeViewItem<T>> allParentChilds = _itemSlibingsContainer().getChildren();
		return Lists.getSiblingsBefore((List<TreeViewItem<T>>)allParentChilds,
									   this.asTreeViewItem());
	}
	@Override
	public TreeViewItem<T> getPrevSibling() {
		Collection<TreeViewItem<T>> allParentChilds = _itemSlibingsContainer().getChildren();
		return Lists.getPrevSibling((List<TreeViewItem<T>>)allParentChilds,
									this.asTreeViewItem());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the {@link HasTreeViewItems} object that contains this item
	 * This can be either a {@link TreeViewItem} or directly the {@link TreeView} if the item is a
	 * root item
	 * It this is a {@link TreeView} this method returns null
	 * @return
	 */
	private HasTreeViewItems<TreeViewItem<T>,T> _itemSlibingsContainer() {
		HasTreeViewItems<TreeViewItem<T>,T> parent = null;
		if (this.isItem()) {
			// If this is an item, the container should be another item,
			parent = _itemOrTree.asTreeViewItem()
								.getDirectAncestor();

			// BUT if this is a root item (_parentItem == null), the container is the tree
			if (parent == null) parent = _itemOrTree.asTreeView();
		}
		return parent;
	}
}
