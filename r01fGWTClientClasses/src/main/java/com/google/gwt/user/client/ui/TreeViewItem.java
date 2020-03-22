package com.google.gwt.user.client.ui;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.TreeViewItemCollapseEvent;
import com.google.gwt.event.logical.shared.TreeViewItemExpandEvent;
import com.google.gwt.event.logical.shared.ValueChangeAtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.TreeViewUtils.TreeViewExpanderDOMElements;

import lombok.Delegate;
import lombok.experimental.Accessors;
import r01f.facets.Facetable;
import r01f.guids.OID;
import r01f.model.facets.view.IsDisableCapable;
import r01f.model.facets.view.IsFiltrable;
import r01f.model.facets.view.IsSelectable;
import r01f.patterns.reactive.ForLazyLoadedObserver;
import r01f.patterns.reactive.ForUpdateObserver;
import r01f.patterns.reactive.Observable;
import r01f.patterns.reactive.ObservableForUpdate;
import r01f.patterns.reactive.ObservableLazyLoaded;
import r01f.patterns.reactive.Observer;
import r01f.types.hierarchy.IsHierarchical;
import r01f.types.lazy.LazyLoaded;
import r01f.util.types.collections.CollectionUtils;
import r01f.view.CanBeDisabled;
import r01f.view.CanBeHidden;
import r01f.view.CanBePainted;
import r01f.view.CanPaint;
import r01f.view.IsCollapsible;
import r01f.view.LazyLoadedViewObserver;
import r01f.view.ObservableLazyLoadedView;
import r01f.view.SelectableViewComponent;
import r01f.view.ViewComponent;
import r01f.view.ViewObject;

/**
 * A {@link TreeView} item of a {@link TreeView}
 *
 * When a {@link ViewComponent} like this represents a model object's view (a {@link ViewObject}), it's interested in being informed of any change
 * that takes place at the model object's view
 * To be informed of model's changes, this {@link ViewComponent} should be attached to the {@link ViewObject} so when the later
 * changes it notifies this {@link ViewComponent} about the change.
 * This is the reason why this {@link ViewComponent} is observing the {@link ViewObject}
 * <ul>
 * 		<li>THIS {@link ViewComponent} is a {@link ForUpdateObserver} of the {@link ViewObject}</li>
 * 		<li>The {@link ViewObject} is an {@link ObservableForUpdate}</li>
 * </ul>
 *
 * The inverse behavior is also needed: the {@link ViewObject} should be informed of {@link ViewComponent}'s changes because these changes
 * should be reflected at the {@link ViewObject}.
 * This is the reason why the {@link ViewObject} is observing this {@link ViewComponent}
 * <ul>
 * 		<li>THIS {@link ViewComponent} is an {@link ObservableForUpdate}</li>
 * 		<li>The {@link ViewObject} is a {@link ForUpdateObserver} on THIS {@link ViewComponent}</li>
 * </ul>
 *
 * So the {@link ViewObject} and the {@link ViewComponent} are connected in both directions:
 * <ul>
 * 	<li>The {@link ViewComponent} observes (is an {@link Observer}) the {@link ViewObject} (which is an {@link Observable} object</li>
 * 	<li>The {@link ViewObject} observes (is an {@link Observer}) the {@link ViewComponent} (which is an {@link Observable} object</li>
 * </ul>
 *
 * The lazy loading behavior is done using a similar technique:
 * <ul>
 * 	<li>The {@link ViewComponent} is an {@link Observable} object observed by the {@link ViewObject}</li>
 * 	<li>When the {@link ViewComponent} needs data, it notifies the {@link ViewObject} (calls the {@link ObservableLazyLoadedView}'s notifiyObserversAboutNeedOfLazyLoadedData method)</li>
 * 	<li>The {@link ViewObject}, on being notified in the on onLazyLoadedDataNeeded() method:
 * 		<ul>
 * 			<li>Notifies the {@link ViewComponent} that lazy loaded data is started to being loaded by issuing a lazyLoadingInProgress message.
 * 				The {@link ViewComponent} reacts to this message by showing a [loading...] label
 * 			</li>
 * 			<li>The {@link ViewObject} Loads the data asynchronously</li>
 * 			<li>When the data is loaded, the {@link ViewObject} notifies again the {@link ViewComponent} by issuing a lazyLoadingComplete message
 * 				The {@link ViewComponent} reacts to this message by hiding the [loading...] label and painting the lazy loaded data
 * 			</li>
 * 		</ul>
 * 	</li>
 * </ul>
 */
@Accessors(prefix="_")
public class TreeViewItem<T extends CanBePainted>
	 extends UIObject
  implements IsCollapsible,							// ...that can be collapsed / expanded
  			 HasTreeViewItems<TreeViewItem<T>,T>,	// ...that has other tree view items as child items
  			 IsHierarchical<TreeViewItem<T>>,
  			 TreeViewItemsContainCapable<T>,		// ...so can contain child tree view items
  			 TakesValue<Boolean>,					// Takes boolean value (selected / deselected)
  			 CanBeDisabled,							// Can be disabled in the ui
  			 CanBeHidden,							// Can be hidden in the ui
  			 ObservableLazyLoadedView,				// Can be observed by view objects for lazy load needs
  			 ObservableForUpdate,					// Can be observed by view objects for changes
  			 ForUpdateObserver<ViewObject>, 		// Observes view object's changes
  			 ForLazyLoadedObserver<T>,				// Observes view object's lazy load events
  			 CanPaint<T>,							// Can paint a view object
  			 SelectableViewComponent {				// ... because it's a view
/////////////////////////////////////////////////////////////////////////////////////////
//  ELEMENT TEMPLATES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The base tree item element that will be cloned.
	 */
	private static LIElement BASE_ELEM = _createBaseElement();
	/**
	 * The structured table to hold images.
	 */
	private static UListElement BASE_CHILD_CONTAINER_ELEM = _createBaseChildContainerElement();
	/**
	 * The child loading message
	 */
	private static LabelElement BASE_LOADING_ELEM = _createBaseLoadingElement();

	private static UListElement _createBaseChildContainerElement() {
		UListElement ulElem = DOM.createElement("ul").cast();
		ulElem.addClassName(TreeViewUtils.CONTAINER_UL_CSS_CLASS_NAME);
		return ulElem;
	}
	private static LIElement _createBaseElement() {
		//		<li>
		//			[1] --- EXPANDER --
		//			<input type="checkbox" class="expander" disabled>	<!-- input.expander DISABLED = no child -->
        //        	<span class="expander"></span>
		//
		//			[2] --- CHECK
        //        	<input type="checkbox" class="selection">
		//
		//			[3] --- WIDGET
        //        	<!-- the widget --> <!-- if it's a text item: <label>Child Node 1</label> -->
        //		</li>

		// [1] - Create the expander hidden checkbox and span to fake the expander image over the checkbox
		TreeViewExpanderDOMElements expanderDOMEls = TreeViewUtils.createExpanderDOMElements();

		// [2] - Create the selection checkbox
		InputElement checkINPUT = DOM.createInputCheck().cast();
		checkINPUT.addClassName(TreeViewUtils.CHECKER_CLASS_NAME);

		// [3] - Create the container LI
		LIElement containerLI = DOM.createElement("li").cast();
		Roles.getTreeitemRole().set(containerLI);

		// assemble the structure
		containerLI.appendChild(expanderDOMEls.getInputElement());
		containerLI.appendChild(expanderDOMEls.getSpanElement());
		containerLI.appendChild(checkINPUT);

		return containerLI;
	}
	private static LabelElement _createBaseLoadingElement() {
		LabelElement label = DOM.createLabel().cast();
		label.addClassName("loading");
		label.setInnerHTML("loading...");
		return label;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PRIVATE FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The tree containing this item
	 */
	private TreeView<T> _tree;
	/**
	 * The widget associated to the item (normally a label)
	 */
	private Widget _widget;
	/**
	 * States if the item is loaded when it's being loaded lazily
	 */
	private boolean _loaded;
/////////////////////////////////////////////////////////////////////////////////////////
//  ViewObject's data stored at this ViewComponent
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Stores any info about the {@link ViewObject} at the {@link ViewComponent} (this {@link CanPaint} object)
	 * This info (maybe an {@link OID}) could be used to identify the {@link ViewObject}
	 */
	private T _paintedObject;

	@Override
	public T getPaintedObject() {
		return _paintedObject;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Transforms a {@link CanBePainted} {@link ViewObject} to a GWT {@link Widget}
	 */
	private final CanBePaintedAsWidget<T> _viewObjectToWidgetTransformer;

	@Delegate
	private final TreeViewIsHierarchicalDelegate<TreeViewItem<T>,T> _childItemsContainerDelegate;

	@Delegate(types=CanBeDisabled.class)
	private final ViewEnableDisableDelegate _enableDisableDelegate;

	@Delegate(types=CanBeHidden.class)
	private final ViewHideDisplayDelegate _hideDisplayDelegate;

	@Delegate(types = {ObservableLazyLoadedView.class,
					   ObservableForUpdate.class},
			  excludes = Observable.class)
	private final ObservableTreeViewItemDelegate<T> _observableDelegate;
	@Override
	public <O extends Observer> void addObserver(final O observer) {
		_observableDelegate.addObserver(observer);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates an empty tree item.
	 */
	TreeViewItem(final CanBePaintedAsWidget<T> viewObjectToWidgetTransformer) {
		// Create the item element structure
		LIElement elem = DOM.clone(BASE_ELEM,		// a simple LI
								   true)			// deep cloning
						    .cast();
		elem.setAttribute("id",DOM.createUniqueId());
		this.setElement(elem);

		// Set the ViewObject to Widget transformer
		_viewObjectToWidgetTransformer = viewObjectToWidgetTransformer;

		// Create the items container delegate
		_childItemsContainerDelegate = new TreeViewIsHierarchicalDelegate<TreeViewItem<T>,T>(_viewObjectToWidgetTransformer,
																						   	 this);
		// Create other delegates
		_hideDisplayDelegate = new ViewHideDisplayDelegate(this);
		_enableDisableDelegate = new ViewEnableDisableDelegate(this);

		// Create the observable delegate
		_observableDelegate = new ObservableTreeViewItemDelegate<T>(this);
	}
	/**
	 * Constructs a tree item with the given <code>Widget</code>.
	 * @param widget the item's widget
	 */
	public TreeViewItem(final CanBePaintedAsWidget<T> viewObjectToWidgetTransformer,
						final T canBePainted) {
		this(viewObjectToWidgetTransformer);

		// Transform the view object to a GWT widget
		Widget widget = _viewObjectToWidgetTransformer.toWidget(canBePainted);
		this.setWidget(widget);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// Accessor methods to relevant elements of the tree structure
//		<li>
//			[0] <input type="checkbox" class="expander">	<!-- input.expander DISABLED = no child / ENABLED = child present -->
//        	[1] <span class="expander"></span>
//        	[2] <input type="checkbox" class="selection">
//        	[3] <!-- the widget -->
//				<!-- if it's a text item: <label>Child Node 1</label> -->
//			- While loading children (lazy loading)
//			[4] <span>loading...</span>
//			- Once children are loaded:
//			[5] <ul>		<!-- the UL is NOT present if there're NO child -->
//					... child items ...
//				</ul>
//		</li>
/////////////////////////////////////////////////////////////////////////////////////////
	InputElement _expanderINPUTElement() {
		LIElement containerLI = this.getElement().cast();
		InputElement expanderINPUT = containerLI.getChild(0).cast();
		return expanderINPUT;
	}
	InputElement _checkINPUTElement() {
		LIElement containerLI = this.getElement().cast();
		InputElement checkINPUT = containerLI.getChild(2).cast();
		return checkINPUT;
	}
	LabelElement _childLoadingSPANElement() {
		LIElement containerLI = this.getElement().cast();
		if (containerLI.getChildCount() < 4) return null;
		LabelElement loadingSPAN = containerLI.getChild(4).cast();
		return loadingSPAN;
	}
	UListElement _childrenContainerULElement() {
		UListElement containerLI = this.getElement().cast();
		if (containerLI.getChildCount() < 4) return null;
		UListElement childContainerULElem = containerLI.getChild(4).cast();
		return childContainerULElem;
	}
	@Override
	public Element getChildContainerElement() {
		return _childrenContainerULElement();
	}
	@Override
	public void prepareToAdoptChildren() {
		if (!this.hasChildren()) {
			// BEFORE the item is prepared to adopt children, it's just like:
			//		<li>
			//			<input type="checkbox" class="expander" disabled>	<!-- input.expander DISABLED = no child -->
	        //        	<span class="expander"></span>
	        //        	<input type="checkbox" class="selection">
	        //        	<!-- the widget --> <!-- if it's a text item: <label>Child Node 1</label> -->
			//			<!-- there's NO child items container -->
	        //		</li>
			//
			// AFTER the item is prepared to adopt children, it's:
			//		<li>
			//			<input type="checkbox" class="expander">			<!-- input.expander ENABLED = has child -->
	        //        	<span class="expander"></span>
	        //        	<input type="checkbox" class="selection">
	        //        	<!-- the widget --> <!-- if it's a text item: <label>Child Node 1</label> -->
			//			<ul class='childContainer'>						 	<!-- child items container is present -->
			//				... here will come the child items
			//			</ul>
	        //		</li>
			//
			// [1] - Create a child items container UL by cloning the BASE_INTERNAL_ELEM
			UListElement childContainerULElem = DOM.clone(BASE_CHILD_CONTAINER_ELEM,// an UL
											  			  true)						// deep cloning
											  	   .cast();
			// [2] - set the new UL as a child of the item (the LI)
			LIElement parentLI = this.getElement().cast();
			parentLI.appendChild(childContainerULElem);
			// [3] - Change the <input type="checkbox" class="expander"> status from DISABLED to ENABLED
			InputElement expanderINPUT = parentLI.getFirstChild().cast();	// from Node to Element
			expanderINPUT.setDisabled(false);
		} else {
			throw new IllegalStateException();
		}
	}
	@Override
	public void allChildrenGone() {
		UListElement childContainerULElement = _childrenContainerULElement();
		assert(childContainerULElement != null && childContainerULElement.getChildCount() == 0);
		LIElement containerLI = this.getElement().cast();
		containerLI.removeChild(childContainerULElement);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PAINT METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void paint(final T paintable) {
		// [1] - Set a reference to the view in the model
		//		 and store any view object's data at this view component
		_paintedObject = paintable;	// store some info about the painted object

		// [2] - Paint the view object, supposing it's fully loaded
		// - Things from view object
		_beginToObservePaintable(this,paintable);	// Start observing the view object to reflect updates at the view
		_setStateFromPaintable(this,paintable);		// Set status from view object

		// - transform the view object to a Widget and set the item's widget
		Widget widget = _viewObjectToWidgetTransformer.toWidget(paintable);
		this.setWidget(widget);

		// - If the view object is hierarchical... paint the child nodes... and recurse
		boolean hasChild = false;
		IsHierarchical<?> hierarchicalPaintable = paintable instanceof IsHierarchical ? (IsHierarchical<?>)paintable : null;
		LazyLoaded lazyLoadedPaintable = paintable instanceof LazyLoaded ? (LazyLoaded)paintable : null;
		if (hierarchicalPaintable != null && hierarchicalPaintable.hasChildren()) {
			hasChild = true;
			Collection<T> paintableChildren = CollectionUtils.cast(hierarchicalPaintable.getChildren());
			_paintChildren(this,				// parent item
						   paintableChildren,	// child items
						   _viewObjectToWidgetTransformer);
			_loaded = lazyLoadedPaintable != null ? lazyLoadedPaintable.isLoaded()
												  : true;
		}
		// - show or hide the expander
		boolean paintableLoaded = lazyLoadedPaintable != null ? lazyLoadedPaintable.isLoaded() : true;
		boolean showExpander = hasChild || !paintableLoaded;
		_expanderINPUTElement().setDisabled(!showExpander);
	}
	@Override
	public void paintOverlay(final T paintable) {
		// [1] - Set a reference to the view in the model
		//		 and store any view object's data at this view component
		_paintedObject = paintable;	// store some info about the painted object

		// [2] - Paint children
		if (paintable instanceof IsHierarchical) {
			IsHierarchical<?> hierarchicalPaintable = (IsHierarchical<?>)paintable;
			if (!hierarchicalPaintable.hasChildren()) return;

			Collection<T> childPaintables = CollectionUtils.cast(hierarchicalPaintable.getChildren());

			// Create NOT EXISTING nodes and recurse for ALREADY EXISTING nodes
			Collection<T> notExistingNodes = Lists.newArrayList();
			for (T childPaintable : childPaintables) {
				TreeViewItem<T> existingTreeViewItem = _findItemFor(this.getChildren(),
																	childPaintable);
				if (existingTreeViewItem != null) {
					existingTreeViewItem.paintOverlay(childPaintable);	// recurse
				} else {
					notExistingNodes.add(childPaintable);
				}
			}
			if (CollectionUtils.hasData(notExistingNodes)) {
				// Simulate a lazy loading
				this.onLazyLoadingInProgress();
				this.onLazyLoadingSuccessful(notExistingNodes);

				// all children loaded? ... not if partial load = true
				LazyLoaded lazyLoadedPaintable = paintable instanceof LazyLoaded ? (LazyLoaded)paintable : null;
				_loaded = lazyLoadedPaintable != null ? lazyLoadedPaintable.isLoaded()
													  : true;
			}
		}
	}
	/**
	 * Paints a {@link Collection} of child objects
	 * @param children
	 */
	public void paintChildren(final Collection<T> children) {
		// the collection of child objects must be paintable
		Collection<T> paintables = CollectionUtils.cast(children);
		_paintChildren(this,		// parent item
					   paintables,	// the child items
					   _viewObjectToWidgetTransformer);
		// TODO informar al widget de que se le han a�adido hijos...
	}
	private static <T extends CanBePainted> TreeViewItem<T> _findItemFor(final Collection<TreeViewItem<T>> children,
											 							 final T paintable) {
		TreeViewItem<T> outItem = null;
		if (CollectionUtils.hasData(children)) {
			for (TreeViewItem<T> childItem : children) {
				// Check if the data that is stored at the ViewComponent is the same as the ViewObject
				// in order to detect if the ViewObject previously existed as child
				if (childItem.getPaintedObject() != null
				 && childItem.getPaintedObject().equals(paintable)) {
					outItem = childItem;
					break;
				}
			}
		}
		return outItem;
	}
	@SuppressWarnings("unchecked")
	private static <T extends CanBePainted> void _beginToObservePaintable(final TreeViewItem<T> item,
												 	 					  final T paintable) {
		//  Link the view with the view object and the view object with the view
		//	(changes in the view's state should be transfered to the view object
		//	and changes in the view object should be transfered to the view)
		if (paintable instanceof ObservableLazyLoaded) {
			ObservableLazyLoaded<ViewObject> observablePaintable = (ObservableLazyLoaded<ViewObject>)paintable;
			observablePaintable.addObserver(item);	// the view (this object) observes view object's changes
													// to reflect them (ie: business rules applied at the model)
		}
		if (paintable instanceof LazyLoadedViewObserver) {
			LazyLoadedViewObserver viewObserver = (LazyLoadedViewObserver)paintable;
			item.addObserver(viewObserver);			// the view object (the node) observes changes in the view
													// (ie: user selection by clicking tree-items)
		}
		if (paintable instanceof ObservableForUpdate) {
			ObservableForUpdate observablePaintable = (ObservableForUpdate)paintable;
			observablePaintable.addObserver(item);
		}
		if (paintable instanceof ForUpdateObserver) {
			ForUpdateObserver<ViewComponent> viewObserver = (ForUpdateObserver<ViewComponent>)paintable;
			item.addObserver(viewObserver);
		}
	}
	private static <T extends CanBePainted> void _setStateFromPaintable(final TreeViewItem<T> item,
											   	   					    final T paintable) {
		// If the view object is facetable and has the IsSelectable facet... check if it's selected
		if (paintable instanceof Facetable) {
			Facetable facetablePaintable = (Facetable) paintable;
			if (facetablePaintable.hasFacet(IsSelectable.class)) {
				IsSelectable selectable = facetablePaintable.asFacet(IsSelectable.class);
				if (selectable.isPrimarySelected()) {
					item.setSelected();
				} if (selectable.isSecondarySelected()) {
					item.setSelected();
					item.disable();
				}
			}
		}
	}
	private static <T extends CanBePainted> void _paintChildren(final TreeViewItem<T> parentItem,
							    	   	   						final Collection<T> paintableChildren,
							    	   	   						final CanBePaintedAsWidget<T> viewObjectToWidgetTransformer) {
		if (CollectionUtils.hasData(paintableChildren)) {
			for (T paintableChild : paintableChildren) {
				// check if the new child is already present as parent's child
				TreeViewItem<T> previouslyExistingItem = _findItemFor(parentItem.getChildren(),
																   	  paintableChild);
				if (previouslyExistingItem != null) {
					//GWT.log("____" + previouslyExistingItem.getText() + " already existed!!!: " + paintableChild.getAttachedView());
					continue;
				}

				// ... if it does not previously exists as parent's item child, paint it
				TreeViewItem<T> newChildItem = new TreeViewItem<T>(viewObjectToWidgetTransformer);	// create the child item
				parentItem.addChild(newChildItem);												// add this item as child item
				newChildItem.paint(paintableChild);				// paint the node in the new item
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the tree that contains this item.
	 * @return the containing tree
	 */
	public final TreeView<T> getTree() {
		return _tree;
	}
	void setTree(final TreeView<T> newTree) {
		if (_tree == newTree) return;
		// Remove this item from previous container tree.
		if (_tree != null && _widget != null) _tree.orphan(this);		// remove the widget from the tree

		// Put this item and it's children in the new tree
		_tree = newTree;
		if (newTree != null) {
			newTree.adopt(this); // Add my widget to the new tree.

			if (this.getChildCount() > 0) {
				// recursively set the tree for the children
				for (int i = 0, n = this.getChildCount(); i < n; ++i) {
					this.getChildAt(i)
						.setTree(newTree);
				}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Widget
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the {@link Widget} associated with this tree item.
	 * @return the widget
	 */
	public Widget getWidget() {
		return _widget;
	}
	/**
	 * Gets the {@link Widget} as an specific type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <W extends Widget> W getWidgetTyped() {
		return (W)this.getWidget();
	}
	/**
	 * Sets the current widget. Any existing child widget will be removed.
	 * @param newWidget Widget to set
	 */
	public void setWidget(final Widget newWidget) {
		// [1]-Detach new child from it's parent (if it has one)
		if (newWidget != null) newWidget.removeFromParent();

		// [2]-Detach old widget from tree
		if (_widget != null) {
			try {
				// logical detach the widget from the tree
				if (_tree != null) _tree.orphan(this);
			} finally {
				// physical detach of the old widget
				Element widgetEl = _widget.getElement();
				this.getElement().removeChild(widgetEl);
				_widget = null;
			}
		}
		// [3] - Attach new widget.
		_widget = newWidget;
		if (newWidget != null) {
			// Physical attach new widget
			this.getElement()							// the item's LI
				.appendChild(newWidget.getElement());	// the item's widget

			// Attach child to tree.
			if (_tree != null) _tree.adopt(this);

			// Set tabIndex on the widget to -1, so that it doesn't mess up the tab order of the entire tree
			if (TreeView.SHOULD_TREE_DELEGATE_FOCUS_TO_ELEMENT(_widget.getElement())) {
				_widget.getElement().setAttribute("tabIndex","-1");
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  SelectableView
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isSelected() {
		return this.getValue();
	}
	@Override
	public void setSelected() {
		this.setValue(true);
	}
	@Override
	public void setDeSelected() {
		this.setValue(false);
	}
	@Override
	public void toggleSelected() {
		if (this.isSelected()) {
			this.setDeSelected();
		} else {
			this.setSelected();
		}
	}
	/**
	 * @return <code>true</code> if this item is selected because another selection (is secondary selected)
	 */
	public boolean isSecondarySelected() {
		return _checkINPUTElement().isDisabled() && this.isSelected();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  IsCollapsible Collapse / expand
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isCollapsed() {
		return !this.isExpanded();
	}
	@Override
	public boolean isExpanded() {
		return _expanderINPUTElement().getPropertyBoolean("checked");
	}
	@Override
	public void collapse() {
		_expanderINPUTElement().setPropertyBoolean("checked",false);
		_expandOrCollapse(false);	// collapse
	}
	@Override
	public void expand() {
		this.expand(true);			// force child loading
	}
	@Override
	public void expandUpToRoot() {
		TreeViewItem<T> parentItem = this.getDirectAncestor();
		while (parentItem != null) {
			parentItem.expand(false);	// do not force child loading
			parentItem = parentItem.getDirectAncestor();
		}
	}
	public void expand(final boolean forceChildLoading) {
		_expanderINPUTElement().setPropertyBoolean("checked",true);
		if (forceChildLoading) {
			_expandOrCollapse(true);			// expand
		} else {
			_fireExpandOrCollapseEvent(true);	// expand
		}
	}
	void _expandOrCollapse(final boolean expand) {
		// If it's an expand event and the item is not loaded, do the loading
		//GWT.log("\t\t> " + (expand ? "expand":"collapse") + ": " + this.getText() + " > loaded=" + _loaded);
		if (expand 			// expand the node
		 && !_loaded) { 	// it's not loaded
			this.notifiyObserversAboutNeedOfLazyLoadedData();	// ensure the presence of children
		}
		// Fire the event
		_fireExpandOrCollapseEvent(expand);
	}
	void _fireExpandOrCollapseEvent(final boolean expand) {
		if (_tree != null) {
			if (expand) {
				OpenEvent.fire(_tree,this);
				TreeViewItemExpandEvent.fire(_tree,this);
			} else {
				CloseEvent.fire(_tree,this);
				TreeViewItemCollapseEvent.fire(_tree,this);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  TakesValue
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Boolean getValue() {
		return _checkINPUTElement().getPropertyBoolean("checked");
	}
	@Override
	public void setValue(final Boolean selected) {
	    this.setValue(selected,
	    			  true);	// fire events
	}
	public void setValue(final Boolean selected,
						 final boolean fireEvents) {
	    Boolean selectedValue = selected == null ? Boolean.FALSE
	    										 : selected;
	    Boolean oldSelectedValue = this.getValue();

		if (selectedValue) {
			_checkINPUTElement().setPropertyBoolean("checked",true);
		} else {
			_checkINPUTElement().setPropertyBoolean("checked",false);
		}
		if (fireEvents) {
			_fireSelectionEvent(oldSelectedValue,selectedValue); 		// fires the selection event
			_notifyModelAboutChanges(oldSelectedValue,selectedValue);	// updates the model
		}
	}
	void _fireSelectionEvent(final Boolean oldSelectedValue,final Boolean selectedValue) {
		// Fire selection event; this fires on selection and un-selection
		SelectionEvent.fire(_tree,
							this);
		// Fire ValueChangeAt event: this fires when selection changes
		ValueChangeAtEvent.fireIfNotEqual(_tree,
										  this,
										  oldSelectedValue,selectedValue);
	}
	void _notifyModelAboutChanges(final Boolean oldSelectedValue,final Boolean selectedValue) {
		if (oldSelectedValue != selectedValue
		 && (oldSelectedValue == null || !oldSelectedValue.equals(selectedValue))) {
			this.notifyObserversAboutUpdate();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LazyLoadedModelObserver
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onLazyLoadingInProgress() {
		// [1] - Create a child label element with the "loading..." text
		LabelElement loadingLabelEl = DOM.clone(BASE_LOADING_ELEM,		// a label
								 				true)					// deep cloning
								 		 .cast();
		// [2] - set the label as a child of the item (the LI)
		LIElement parentLI = this.getElement().cast();
		parentLI.insertAfter(loadingLabelEl,
							 _widget.getElement());			// the loading label is ALLWAYS after the widget
	}
	@Override
	public void onLazyLoadingSuccessful(final Collection<T> children) {
//		GWT.log("====>" + this.getText() + " > " + (children != null ? children.size() : 0) + " child loaded");
		// Remove the loading... label
		LabelElement loadingLabel = _childLoadingSPANElement();
		LIElement parentLI = this.getElement().cast();
		parentLI.removeChild(loadingLabel);

		// if the number of children loaded is 0 (there where no child items), hide the expander
		// otherwise paint the children
		if (CollectionUtils.isNullOrEmpty(children)) {
			_expanderINPUTElement().setDisabled(true);
		} else {
			this.paintChildren(children);
		}
	}
	@Override
	public void onLazyLoadingError(final String err) {
		// Change the loading... label for ERROR!!
		LabelElement loadingLabel = _childLoadingSPANElement();
		loadingLabel.setInnerHTML("ERROR!!!");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ModelObserver (updates FROM the model)
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onUpdate(final ViewObject modelObj) {
		if (modelObj instanceof Facetable) {
			Facetable facetableModelObj = (Facetable)modelObj;
			// [0] - Disable capable
			// Convert the view object to disable capable
			IsDisableCapable disableCapable = facetableModelObj.asFacet(IsDisableCapable.class);
			if (disableCapable != null) {
				if (disableCapable.isDisabled()) {
					this.disable();
				} else {
					this.enable();
				}
			}
			// [1] - Selectable
			// Convert the view object to selectable
			IsSelectable selectable = facetableModelObj.asFacet(IsSelectable.class);
			if (selectable != null) {
				// If this item was selected because of a secondary selection and the update states that the
				// item should be de-selected, the checkInputElement should be enabled again
				if (this.isSecondarySelected() && !selectable.isSecondarySelected()) {
					_checkINPUTElement().setDisabled(false);
				}
				// Update from view object's changes
				this.setValue(selectable.isSelected(),	// either primary or secondary selected
							  false);					// do not fire events... infinite loop risk
				// If it's a secondary selection, disable the selection checkbox as it only can be
				// de-selected de-selecting the primary selection
				if (selectable.isSecondarySelected()) {
					_checkINPUTElement().setDisabled(true);
				}
			}
			// [2] - Filtrable
			// Convert the view object to filtrable
			IsFiltrable filtrable = facetableModelObj.asFacet(IsFiltrable.class);
			if (filtrable != null) {
				if (!filtrable.isFiltered()) {
					this.display();
				} else {
					this.hide();
				}
			}
		}
	}

}
