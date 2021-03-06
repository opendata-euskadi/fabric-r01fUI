package r01ui.base.components.tree;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.vaadin.data.TreeData;

import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.collections.CollectionUtils;

/**
 * Extends the Vaadin's {@link TreeData} with some useful methods to:
 * 		- get a sub-tree
 * 		- remove an entire sub-tree
 * 		- filter items
 */
public class VaadinTreeData<T>
     extends TreeData<T> 
  implements UIViewObject {

	private static final long serialVersionUID = 7385317969673861714L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinTreeData() {
		// default no-args constructor
	}
	public VaadinTreeData(final TreeData<T> other) {
		this();
		this.importTree(other);
	}
	public static <T> VaadinTreeData<T> from(final TreeData<T> other) {
		return new VaadinTreeData<T>(other);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IMPORT                                                                        
/////////////////////////////////////////////////////////////////////////////////////////
	public void replaceWith(final TreeData<T> other) {
		this.clear();
		this.importTree(other);
	}
	@SuppressWarnings("unchecked")
	public void importTree(final TreeData<T> other) {
		if (other == null) return;
		for (T rootItem : other.getRootItems()) {
			// import the root
			this.addRootItems(rootItem);
			// recurse
			_recurseImport(other,			
						   rootItem);
		}
	}
	private void _recurseImport(final TreeData<T> other,
								final T item) {
		Collection<T> childItems = other.getChildren(item);
		if (CollectionUtils.isNullOrEmpty(childItems)) return;
		
		this.addItems(item,
					  childItems);
		for (T childItem : childItems) {
			_recurseImport(other,
						   childItem);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a root node AFTER another given root node
	 * @param nodeToBeAdded
	 * @param prevSibling
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VaadinTreeData<T> addRootItemAfterSibling(final T nodeToBeAdded,final T prevSibling) {
		if (prevSibling != null
		 && this.getParent(prevSibling) != null) throw new IllegalArgumentException("The given [sibling node] is NOT a [root node]");
		
		this.addRootItems(nodeToBeAdded);
		if (prevSibling != null) this.moveAfterSibling(nodeToBeAdded,prevSibling);
		return this;
	}
	/**
	 * Adds a node AFTER another given sibling node
	 * @param parentNode
	 * @param nodeToBeAdded
	 * @param prevSibling
	 * @return
	 */
	public VaadinTreeData<T> addItemAfterSibling(final T parentNode,
												 final T nodeToBeAdded,final T prevSibling)	{
		if (parentNode != null
		 && prevSibling != null
		 && this.getParent(prevSibling) != parentNode) throw new IllegalArgumentException("The given [parent node] is NOT the parent of the given [sibling node]");
		
		if (parentNode == null
		 && prevSibling != null
		 && this.getParent(prevSibling) != null) throw new IllegalArgumentException("The given [sibling node] is NOT a [root node]");
		
		this.addItem(parentNode,nodeToBeAdded);
		if (prevSibling != null) this.moveAfterSibling(nodeToBeAdded,prevSibling);
		return this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET                                                                         
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<T> getSiblingsOf(final T node) {
		T parent = this.getParent(node);
		return this.getChildren(parent)
				   .stream()
				   .filter(otherNode -> otherNode.equals(node))
				   .collect(Collectors.toList());
	}
	public List<T> getAncestorsOf(final T node) {
		List<T> ancestors = Lists.newArrayList();
		T currParent = this.getParent(node);
		while (currParent != null) {
			ancestors.add(currParent);
			currParent = this.getParent(currParent);	// up!
		}
		return ancestors;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET SUB-TREE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the subtree from the given item
	 * @param item
	 * @return
	 */
	public TreeData<T> getSubTreeOf(final T item) {
		TreeData<T> outSubTreeData = new TreeData<T>();
		_recurseGetSubTreeItem(null,	// root node > parent = null
							   item,
							   outSubTreeData);
		return outSubTreeData;
	}
	@SuppressWarnings("unchecked")
	protected void _recurseGetSubTreeItem(final T parentItem,final T item,
									  	  final TreeData<T> subTreeData) {
		// add the item
		if (parentItem == null) {
			subTreeData.addRootItems(item);
		} else {
			subTreeData.addItems(parentItem,
							     item);
		}
		// recurse add the child items
		Collection<T> itemChildren = this.getChildren(item);
		if (CollectionUtils.hasData(itemChildren)) {
			itemChildren.forEach(itemChild -> _recurseGetSubTreeItem(item,			// parent item
														   		   	 itemChild,
														   		   	 subTreeData));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADD SUB-TREE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Add the sub-tree from the given item
	 * @param parentItem
	 * @param subTreeData
	 */
	public void addSubTree(final T parentItem,
						   final TreeData<T> subTreeData) {
		subTreeData.getRootItems()
				   .forEach(item -> _recurseAddSubTreeItem(parentItem,item,
						   								   subTreeData));
	}
	/**
	 * Adds the sub-tree below the given item
	 * @param parentItem
	 * @param subTreeParent
	 */
	public void addSubTreeBelow(final T parentItem,
								final T subTreeParent,final TreeData<T> subTree) {
		Collection<T> childItems = subTree.getChildren(subTreeParent);
		if (CollectionUtils.hasData(childItems)) {
			childItems.forEach(childItem -> _recurseAddSubTreeItem(parentItem,childItem,
															  	   subTree));
		}
	}
	@SuppressWarnings("unchecked")
	protected void _recurseAddSubTreeItem(final T parentItem,final T item,
										  final TreeData<T> subTreeData) {
		// add the item
		if (parentItem == null) {
			this.addRootItems(item);
		} else {
			this.addItems(parentItem,
						  item);
		}
		// add child
		Collection<T> childItems = subTreeData.getChildren(item);
		if (CollectionUtils.hasData(childItems)) {
			childItems.forEach(childItem -> _recurseAddSubTreeItem(item,childItem,
																   subTreeData));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REMOVE SUB-TREE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Removes the sub-tree from the given item
	 * @param subTreeData
	 */
	public void removeSubTree(final TreeData<T> subTreeData) {
		subTreeData.getRootItems()
				   .forEach(item -> _recurseRemoveSubTreeItem(item,
						   									  subTreeData));
	}
	/**
	 * Removes the sub-tree below the given item
	 * @param subTreeParent
	 * @param subTreeData
	 */
	public void removeSubTreeBelow(final T subTreeParent,
								   final TreeData<T> subTreeData) {
		Collection<T> childItems = subTreeData.getChildren(subTreeParent);
		if (CollectionUtils.hasData(childItems)) {
			childItems.forEach(item -> _recurseRemoveSubTreeItem(item,
																 subTreeData));
		}
	}
	protected void _recurseRemoveSubTreeItem(final T item,
											 final TreeData<T> subTreeData) {
		// remove child
		Collection<T> childItems = subTreeData.getChildren(item);
		if (CollectionUtils.hasData(childItems)) {
			childItems.forEach(childItem -> _recurseRemoveSubTreeItem(childItem,
																	  subTreeData));
		}
		// LAST!! remove the item
		this.removeItem(item);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
    public static <I extends VaadinTreeItem<?>> StringBuilder debugInfoOf(final TreeData<I> treeData) {
		StringBuilder sb = new StringBuilder();
		if (CollectionUtils.hasData(treeData.getRootItems())) {
			treeData.getRootItems()
					.forEach(rootNode -> _recurseTreeDebugInfo(treeData,rootNode,
														   	   0,
														   	   sb));
		}
		return sb;
    }
	private static <I extends VaadinTreeItem<?>>  void _recurseTreeDebugInfo(final TreeData<I> treeData,final I node,
																	   		 final int deepth,
																	   		 final StringBuilder sb) {
		sb.append(_tabs(deepth))
		  .append("- ")
		  .append(node.getCaption())
		  .append("\n");
		Collection<I> children = treeData.getChildren(node);
		if (CollectionUtils.hasData(children )) {
			int nextDeepth = deepth + 1;
			children.forEach(child -> _recurseTreeDebugInfo(treeData,child,
															nextDeepth,
															sb));
		}
	}
	private static CharSequence _tabs(final int num) {
		if (num == 0) return "";
		StringBuilder sb = new StringBuilder(num);
		for (int i=0; i < num; i++) sb.append("\t");
		return sb;
	}
}
