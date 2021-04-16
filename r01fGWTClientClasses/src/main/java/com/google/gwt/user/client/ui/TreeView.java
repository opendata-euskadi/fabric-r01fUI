package com.google.gwt.user.client.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gwt.aria.client.ExpandedValue;
import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasTreeViewItemCollapseHandlers;
import com.google.gwt.event.logical.shared.HasTreeViewItemExpandHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeAtHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.TreeViewItemCollapseEvent;
import com.google.gwt.event.logical.shared.TreeViewItemCollapseHandler;
import com.google.gwt.event.logical.shared.TreeViewItemExpandEvent;
import com.google.gwt.event.logical.shared.TreeViewItemExpandHandler;
import com.google.gwt.event.logical.shared.ValueChangeAtEvent;
import com.google.gwt.event.logical.shared.ValueChangeAtHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TreeViewUtils.TreeViewExpanderDOMElements;

import lombok.Delegate;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.types.hierarchy.HasChildren;
import r01f.types.hierarchy.IsHierarchical;
import r01f.util.types.collections.CollectionUtils;
import r01f.view.CanBeDisabled;
import r01f.view.CanBeHidden;
import r01f.view.CanBePainted;
import r01f.view.CanPaint;
import r01f.view.ViewComponent;
import r01f.view.ViewObject;

/**
 * A tree view implementation for GWT
 * It's based on:
 * <ul>
 * 		<li>css tree view by GORKY: https://github.com/grokys/css-treeview</li>
 * 		<li>The {@link Tree} GWT implementation</li>
 * </ul>
 * The tree view uses a structure built by UL/LI elements and INPUT type=checkbox like:
 * <pre class='brush:html'>
 *		<li>
 *			[1] <input type="checkbox" class="expander">
 *        	[2] <span class="expander"></span>
 *        	[3] <input type="checkbox" class="selection">
 *        	[4] <!-- the widget -->
 *		</li>
 * </pre>
 * <ol>
 * 		<li>The first INPUT type='checkbox' controls two things:
 * 			<ul>
 * 				<li>If the item has children by means of it's ENABLED/DISABLED attribute
 * 					 <ul>
 * 						<li>If the item has children, it's ENABLED (default): <pre><input type="checkbox" class="expander"></pre></li>
 * 						<li>If the item DO NOT has children, it's DISABLED  : <pre><input type="checkbox" class="expander" disabled></pre></li>
 * 					</ul>
 * 				<li>
 * 				<li>If the item has children, it's CHECKED/UNCHECKED status controls whether they're EXPANDED/COLLAPSED:
 * 					<ul>
 * 						<li>If the children are EXPANDED the checkbox is CHECKED</li>
 * 						<li>If the children are COLLAPSED the checkbox is UNCHECKED</li>
 * 					</ul>
 * 				</li>
 *			</ul>
 *		</li>
 *		<li>The SPAN is there simply to prepend a background image for the handler (normally an arrow > / \/ or a plus/minus [+] / [-])
 *			The first INPUT type=checkbox is HIDDEN by setting it's opacity to 0 so 'it's still there' but the handler image
 *			is set on the INPUT's place and when the user clicks the image he/she's CHECKING / UNCHECKING the INPUT type=checkbox,
 *			and so expanding / collapsing the children
 *		</li>
 *		<li>The INPUT type=checkbox that states if the item is selected or not</li>
 *		<li>The WIDGET. It can be ANY widget, but normally it's a TEXT item so it's simply a label: <pre><label>The text</label></pre></li>
 * </ol>
 *
 *
 * This {@link ViewComponent} implements {@link CanPaint} so any type implementing {@link IsHierarchical}
 * can be represented using this component:
 * <pre class='brush:java'>
 * 		R01MStructureView structure = _loadStructure();						// the view object that implements {@link IsHierarchical}
 *		TreeView treeForStructure = new TreeView(new CanBePaintedToLabel());// the view component in charge of representing the view object
 *		structure.paintInto(treeForStructure);								// paint the view object in the view component
 * </pre>
 */
@Accessors(prefix="_")
public class TreeView<T extends CanBePainted>
	 extends Widget
  implements HasWidgets.ForIsWidget,
  			 HasValueChangeAtHandlers<TreeViewItem<T>,Boolean>,HasSelectionHandlers<TreeViewItem<T>>,
  			 HasOpenHandlers<TreeViewItem<T>>,HasCloseHandlers<TreeViewItem<T>>,
  			 HasTreeViewItemCollapseHandlers<TreeViewItem<T>>,HasTreeViewItemExpandHandlers<TreeViewItem<T>>,
  			 HasClickHandlers,
  			 HasAllKeyHandlers,
  			 HasAllMouseHandlers,
  			 HasAllFocusHandlers,
  			 Focusable,
  			 CanBeDisabled,									// Can be disabled in the UI
  			 CanBeHidden,									// Can be hidden in the UI
  			 HasTreeViewItems<TreeViewItem<T>,T>,			// has tree view items
  			 TreeViewItemsContainCapable<T>,				// ... so can contain child tree view items
  			 CanPaint<T> {  								// Can paint view objects (it's a view component)

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	static native boolean SHOULD_TREE_DELEGATE_FOCUS_TO_ELEMENT(Element elem) /*-{
		var name = elem.nodeName;
		return ((name == "SELECT") || (name == "INPUT") || (name == "TEXTAREA")
				|| (name == "OPTION") || (name == "BUTTON") || (name == "LABEL"));
	}-*/;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
//  ==== Fields for WidgetReferences =========================================================
	/**
	 * A widget for the ROOT node
	 */
	private Widget _rootWidget;
	/**
	 * An index of descendant {@link TreeViewItem} widgets (any depth level: flattens the tree view)
	 * It's only used to provide access to the {@link TreeViewItem}s
	 */
	private final Map<Widget,TreeViewItem<T>> _descendantWidgets = new HashMap<Widget,TreeViewItem<T>>();

//  ==== Fields for DOM Elements references ==================================================
	/**
	 * The root nodes container DOM Element
	 */
	private UListElement _rootNodesContainerElement;
	/**
	 * The expander of the root nodes
	 */
	private InputElement _expanderElement;

//  ==== Fields use when managing events for keyboard navigation =============================
	/**
	 * Item with the focus
	 */
	private TreeViewItem<T> _focusedItem;
	/**
	 * This field stores the item associated with events that comes together as [KeyDown]/[KeyPress]/[KeyUp] or [MouseDown]/[Click]
	 * The field is set in the first event and unset in the last
	 */
	private TreeViewItem<T> _relatedEventsTargetItem;
	/**
	 * This field stores the value associated with the item BEFORE the new value is set
	 * Normally this value is stored in the first event within a stream of related events like [KeyDown]/[KeyPress]/[KeyUp] or [MouseDown]/[Click]
	 */
	private boolean _targetItemPreviousValue;
//  ==== Other Fields ==========================================================================
	/**
	 * Scroll to selected element
	 */
	private boolean _scrollOnSelectEnabled = true;
	/**
	 * Are broken rules in use?
	 */
	private boolean _usingBrokenRules;
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
//  DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Transforms a {@link CanBePainted} {@link ViewObject} for the tree to a GWT {@link Widget}
	 * (it's used to paint the ROOT node)
	 */
	private final CanBePaintedAsWidget<T> _treeViewObjectToWidgetTransformer;
	/**
	 * Transforms a {@link CanBePainted} {@link ViewObject} to a GWT {@link Widget}
	 * (it's used to paint every child item)
	 */
	private final CanBePaintedAsWidget<T> _itemViewObjectToWidgetTransformer;

	@Delegate
	private final TreeViewIsHierarchicalDelegate<TreeView<T>,T> _childItemsContainerDelegate;

	@Delegate(types=CanBeDisabled.class)
	private final ViewEnableDisableDelegate _enableDisableDelegate;

	@Delegate(types=CanBeHidden.class)
	private final ViewHideDisplayDelegate _hideDisplayDelegate;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructs an empty tree.
	 * @param itemViewObjectToWidgetTransformer transforms an item's {@link ViewObject} to a GWT {@link Widget}
	 */
	public TreeView(final CanBePaintedAsWidget<T> treeViewObjectToWidgetTransformer,
					final CanBePaintedAsWidget<T> itemViewObjectToWidgetTransformer) {
		// Init the DOM structure
		_initDOM();

		// Set the view object to widget transformers
		_treeViewObjectToWidgetTransformer = treeViewObjectToWidgetTransformer;
		_itemViewObjectToWidgetTransformer = itemViewObjectToWidgetTransformer;

		// Delegates
		_childItemsContainerDelegate = new TreeViewIsHierarchicalDelegate<TreeView<T>,T>(_itemViewObjectToWidgetTransformer,
																					   	 this);
		_hideDisplayDelegate = new ViewHideDisplayDelegate(this);
		_enableDisableDelegate = new ViewEnableDisableDelegate(this);
	}
	/**
	 * Builds the element structure:
	 * <pre class='brush:html'>
	 * 		<ul class='treeview'>
	 *			<li>
	 *				<input type="checkbox" class="expander" disabled>	<!-- input.expander DISABLED = no child -->
     *        		<span class="expander"></span>
     *
     *        		<!-- The root node widget -->	<!-- usually is a the tree caption -->
     *
     *        		<ul class='childContainer'>..</ul>		<!-- the child items container -->
     *			</li>
     *		</ul>
	 * </pre>
	 */
	private void _initDOM() {
		// [1] - Create a main UL
		UListElement mainULEl = DOM.createElement("ul").cast();
		mainULEl.addClassName(TreeViewUtils.TREEVIEW_CSS_CLASS_NAME);

		// [2] - Create a single LI for the tree label and append it to the UL
		LIElement mainLIEl = DOM.createElement("li").cast();
		mainULEl.appendChild(mainLIEl);

		// [3] - Create an UL to contain the root nodes and append it to the title's LI
		_rootNodesContainerElement = DOM.createElement("ul").cast();
		_rootNodesContainerElement.addClassName(TreeViewUtils.CONTAINER_UL_CSS_CLASS_NAME);
		mainLIEl.appendChild(_rootNodesContainerElement);

		// [4] - set the widget DOM element
		this.setElement(mainULEl);

		// Capture events
		this.sinkEvents(Event.ONMOUSEDOWN | Event.ONCLICK | Event.KEYEVENTS);	// = DOM.sinkEvents(this.getElement(),Event.ONMOUSEDOWN | Event.ONCLICK | Event.KEYEVENTS)
		DOM.sinkEvents(this.getElement(),	// main UL
					   Event.FOCUSEVENTS);

		// Add area role "tree"
		Roles.getTreeRole()
			 .set(this.getElement());
	}

// TODO revisar

//		// [3] - Subscribe this object to the broken rules observable
//		if (paintable instanceof ObservableRulesEnforcedModelObject) {
//			((ObservableRulesEnforcedModelObject)paintable).addObserver(this);
//		}

//
//		// [3] - Create the filter textbox
//		InputElement filterInputEl = DOM.createInputText().cast();
//
//		// [4] - Create the broken rules label
//		LabelElement brokenRulesLabelEl = DOM.createLabel().cast();
//		if (brokenRules != null) {
//			brokenRulesLabelEl.setInnerHTML("NOK");
//			brokenRulesLabelEl.addClassName(TreeViewUtils.BROKEN_RULES_NOK_CLASS_NAME);
//		} else if (_usingBrokenRules) {
//			brokenRulesLabelEl.setInnerHTML("OK");
//			brokenRulesLabelEl.addClassName(TreeViewUtils.BROKEN_RULES_OK_CLASS_NAME);
//		}


//	@Override
//	public void setBrokenRules(final Collection<BrokenRule> brokenRules) {
//		_usingBrokenRules = true;
//		if (_expanderElement == null) {
//			_createRootNodesExpander(null,brokenRules);
//		} else {
//			LabelElement brokenRulesEl = _expanderElement.getParentElement()		// the LI is the expander's input parent
//												   		 .getChild(3).cast();		// the broken rules
//			brokenRulesEl.removeClassName(TreeViewUtils.BROKEN_RULES_NOK_CLASS_NAME);
//			brokenRulesEl.removeClassName(TreeViewUtils.BROKEN_RULES_OK_CLASS_NAME);
//			if (brokenRules != null) {
//				brokenRulesEl.setInnerHTML("NOK");
//				brokenRulesEl.addClassName(TreeViewUtils.BROKEN_RULES_NOK_CLASS_NAME);
//			} else {
//				brokenRulesEl.setInnerHTML("OK");
//				brokenRulesEl.addClassName(TreeViewUtils.BROKEN_RULES_OK_CLASS_NAME);
//			}
//		}
//	}
//	@Override
//	public String getBrokenRules() {
//		if (_expanderElement == null) return null;
//		LabelElement brokenRulesEl = _expanderElement.getParentElement()  // the LI
//												     .getChild(2).cast(); // the label
//		return brokenRulesEl.getInnerHTML();
//	}
/////////////////////////////////////////////////////////////////////////////////////////
//  BrokenRulesEnforcedModelObjectObserver
/////////////////////////////////////////////////////////////////////////////////////////
//	@Override
//	public void onBrokenRules(final Collection<BrokenRule> brokenRules) {
//		this.setBrokenRules(brokenRules);
//	}

	private void _createRootNodesExpander(final Widget rootWidget) {
		// Create the expander hidden checkbox and span to fake the expander image over the checkbox
		// and a Label to hold the treeview caption
		// 		<ul class='treeview'>
		//			<li>
		//				[1] - The expander
		//				<input type="checkbox" class="expander" enabled>
	    //        		<span class="expander"></span>
		//
		//				[2] - The widget
		//				<!-- The root node widget -->	<!-- usually is a the tree caption -->
		//
		//				[3] - The child container that contains the TreeView root nodes
	    //        		<ul class='childContainer'>..</ul>
	    //			</li>
	    //		</ul>

		// [1] - Create the expander
		TreeViewExpanderDOMElements expanderDOMEls = TreeViewUtils.createExpanderDOMElements();
		_expanderElement = expanderDOMEls.getInputElement();

		// [2] - insert the structure BEFORE the root nodes UL container
		_rootNodesContainerElement.getParentElement()			// the main LI containing the root nodes
									  .insertFirst(rootWidget.getElement())				// [2] insert the root widget
									  		.getParentElement()							// ... go again to the main LI
									  .insertFirst(expanderDOMEls.getSpanElement())		// [1] insert the SPAN and go again to the main LI
									  		.getParentElement()							// ... go again to the main LI
									  .insertFirst(expanderDOMEls.getInputElement());	// [1] insert the INPUT
	}
	@Override
	public void paint(final T paintable) {
		// [1] - Set a reference to the view in the model
		//		 and store any view object's data at this view component
		if (paintable != null) {
			_paintedObject = paintable;	// store some info about the painted object
		}

		// [2] - Create the expander and paint the root node (if it exists)
		Widget rootNodeWidget = _treeViewObjectToWidgetTransformer.toWidget(paintable);
		if (rootNodeWidget != null) _createRootNodesExpander(rootNodeWidget);

		// [2] - Paint root nodes creating the root tree items and all the hierarchy below
		_paintPaintables(_paintableChildNodesOf(paintable),
						 false);		// do not overlay
	}
	@Override
	public void paintOverlay(final T modelObj) {
		// Paint root nodes creating the root tree items and all the hierarchy below
		_paintPaintables(_paintableChildNodesOf(modelObj),
						 true);		// overlay
	}
	private void _paintPaintables(final Collection<T> paintables,
								  final boolean overlay) {
		CollectionUtils.executeOn(paintables,
								  paintable -> {
											TreeViewItem<T> existingTreeItem = overlay ? _alreadyExistsItem(paintable,TreeView.this.getChildren())
																					   : null;
											if (existingTreeItem == null) {
												TreeViewItem<T> rootItem = new TreeViewItem<T>(_itemViewObjectToWidgetTransformer);	// Create the root item
												rootItem.paint(paintable);															// Paint the root node in the root item
												TreeView.this.addChild(rootItem);													// Add the root node to the tree
											} else if (overlay) {
												existingTreeItem.paintOverlay(paintable);
											}
								  });
	}
	private static <T extends CanBePainted> Collection<T> _paintableChildNodesOf(final T paintable) {
		Collection<T> outPaintables = null;
		if (paintable instanceof HasChildren<?>) {
			HasChildren<?> modelObjWithChildren = (HasChildren<?>)paintable;
			if (CollectionUtils.hasData(modelObjWithChildren.getChildren())) {
				outPaintables = CollectionUtils.cast(modelObjWithChildren.getChildren());
			}
		}
		return outPaintables;
	}
	private static <T extends CanBePainted> TreeViewItem<T> _alreadyExistsItem(final T paintable,
												   	   	  					   final Collection<TreeViewItem<T>> items) {
		TreeViewItem<T> outExistingTreeViewItem = null;
		if (CollectionUtils.hasData(items)) {
			for (TreeViewItem<T> item : items) {
				// Check if the data that is stored at the ViewComponent is the same as the ViewObject
				// in order to detect if the ViewObject previously existed as child
				if (item.getPaintedObject() != null
				 && item.getPaintedObject().equals(paintable)) {
					outExistingTreeViewItem = item;
					break;
				}
			}
		}
		return outExistingTreeViewItem;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Element getChildContainerElement() {
		return _rootNodesContainerElement;
	}
	@Override
	public void prepareToAdoptChildren() {
		/* nothing: all root children MUST be loaded */
	}
	@Override
	public void allChildrenGone() {
		/* nothing */
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	Orphan & Adopt descendant widgets
/////////////////////////////////////////////////////////////////////////////////////////
	void adopt(final TreeViewItem<T> treeItem) {
		if (treeItem.getWidget() != null) {
			assert (!_descendantWidgets.containsKey(treeItem.getWidget()));
			_descendantWidgets.put(treeItem.getWidget(),treeItem);
			treeItem.getWidget().setParent(this);
		}
	}
	/**
	 * Detaches a widget from the tree
	 * @param widget
	 */
	void orphan(final TreeViewItem<T> treeItem) {
		if (treeItem.getWidget() == null) return;
		assert (treeItem.getWidget().getParent() == this);	// if a widget is to be detached, it's parent should be this tree
		try {
			treeItem.getWidget().setParent(null);			// detach!
		} finally {
			_descendantWidgets.remove(treeItem.getWidget());// Logical detach.
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * This method exists solely to support unit tests.
	 */
	Map<Widget,TreeViewItem<T>> getChildWidgets() {
		return _descendantWidgets;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  FIND
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds all tree view items with the provided data
	 * @param data
	 * @return
	 */
	public Collection<TreeViewItem<T>> findItemsWithData(final Object data) {
		if (data == null) return null;
		Collection<TreeViewItem<T>> outItems = Lists.newArrayList();
		_accumItemsWithData(data,
							this.getChildren(),
							outItems);
		return CollectionUtils.hasData(outItems) ? outItems : null;
	}
	private void _accumItemsWithData(final Object data,final Collection<TreeViewItem<T>> items,
									final Collection<TreeViewItem<T>> accum) {
		if (CollectionUtils.hasData(items)) {
			for (TreeViewItem<T> item : this.getChildren()) {
				if (item.getPaintedObject() != null && item.getPaintedObject().equals(data)) {
					accum.add(item);
				}
				// recurse
				_accumItemsWithData(data,
									item.getChildren(),
									accum);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Focusable
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public int getTabIndex() {
		return this.getElement().getTabIndex();
	}
	@Override
	public void setTabIndex(final int index) {
		this.getElement().setTabIndex(index);
	}
	@Override
	public void setFocus(final boolean focused) {
		if (_focusedItem == null) {
			if (focused) {
				this.getElement().focus();
			} else {
				this.getElement().blur();
			}
		} else {
			if (focused) {
				_focusedItem._checkINPUTElement().focus();
			} else {
				_focusedItem._checkINPUTElement().blur();
			}
		}
	}
	private void _onFocus(final TreeViewItem<T> item) {
		_focusedItem = item;
		if (_focusedItem != null) {
			_focusedItem._checkINPUTElement().scrollIntoView();
			_updateAriaAttributes();
			this.setFocus(true);
		}
	}
	@Override
	public void setAccessKey(final char key) {
		this.setAccessKey(this.getElement(),key);
	}
	public native void setAccessKey(final Element elem,final char key) /*-{
		elem.accessKey = String.fromCharCode(key);
	}-*/;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);

		int eventType = DOM.eventGetType(event);
		Element eventTargetEl = event.getEventTarget().cast();	// DOM.eventGetTarget(event);

		switch (eventType) {
		case Event.ONKEYDOWN: {
			// If nothing's focused, focus the first item.
			if (_focusedItem == null && this.getChildCount() > 0) {
				_onFocus(this.getChildAt(0));
				return;
			}
		}
		// Intentional fall through.
		case Event.ONKEYPRESS:
		case Event.ONKEYUP:
			// Issue 1890: Do not block history navigation via alt+left/right
			if (event.getAltKey() || event.getMetaKey()) return;
			break;
		}

		switch (eventType) {
		case Event.ONMOUSEDOWN: {
			// Find the item that contains the event target element
			_relatedEventsTargetItem = TreeViewUtils.findTreeViewItemContaining(eventTargetEl,
										 								    	this);
			if (_relatedEventsTargetItem != null) _targetItemPreviousValue = _relatedEventsTargetItem.getValue();
			break;
		}
		case Event.ONCLICK: {
			if (_relatedEventsTargetItem == null) break;	// there's no related events target item

			// If the click was on the handler checkbox, the expansion or collapse of the items is controlled by the CSS
			// ... so the open/close event should be fired by hand
			// almost the same goes for the selection checkbox; the selection event should be fired by hand since the
			// checkbox does NOT fire a selection event
			if (_relatedEventsTargetItem._expanderINPUTElement() == eventTargetEl) {		// the expander checkbox
				_relatedEventsTargetItem._expandOrCollapse(_relatedEventsTargetItem.isExpanded());

			} else if (_relatedEventsTargetItem._checkINPUTElement() == eventTargetEl) {	// the selection checkbox
				_relatedEventsTargetItem._fireSelectionEvent(_targetItemPreviousValue,_relatedEventsTargetItem.getValue());			// fire the selection event
				_relatedEventsTargetItem._notifyModelAboutChanges(_targetItemPreviousValue,_relatedEventsTargetItem.getValue());	// update the model
			}
			// Give the focus to the clicked item
			_onFocus(_relatedEventsTargetItem);
			// release
			_relatedEventsTargetItem = null;
			break;
		}
		}
		// Key events occur in the following order:
		//		[1] - KeyDown   : A key is pressed down
		//		[2] - KeyPress  : A character key is pressed
		//		[3] - KeyUp		: A key is released
		// 		 *  The [KeyPress] event is NOT raised by non-character keys; however the non-character keys DO raise the [KeyDown] and [KeyUp] events
		// If key {A} is pressed, the sequence of events is:
		//		[1] KeyDown   {A}
		//		[2] KeyPress  {A}
		//		[3] KeyUp	  {A}
		// If keys {shift}-{A} is pressed, the sequence of events is:
		//		[1] KeyDown   {SHIFT}
		//		[2] KeyDown   {A}
		//		[3] KeyPress  {A}	<-- KeyPress is NOT raised for non-character key {shift}
		//		[4] KeyUp	  {A}
		//		[5] KeyUp	  {SHIFT}
		switch(eventType) {
		case Event.ONKEYDOWN: {
			// Find the item that contains the event target element
			_relatedEventsTargetItem = TreeViewUtils.findTreeViewItemContaining(eventTargetEl,
										 								    	this);
			if (_relatedEventsTargetItem == null) break;

			_targetItemPreviousValue = _relatedEventsTargetItem.getValue();

			// Navigate using arrows
			_keyboardNavigation(event);
			// Selection using space bar
			if (event.getKeyCode() == KeyCodes.KEY_SPACE
			 && TreeViewUtils.isEventAssociatedWithItemChecker(eventTargetEl)) {
				_relatedEventsTargetItem._fireSelectionEvent(_targetItemPreviousValue,!_targetItemPreviousValue);		// fire an event
				_relatedEventsTargetItem._notifyModelAboutChanges(_targetItemPreviousValue,_relatedEventsTargetItem.getValue());	// update the model
			}
			break;
		}
		}
		// Stop event propagation form arrow keys
		switch (eventType) {
		case Event.ONKEYDOWN:
		case Event.ONKEYUP: {
			if (KeyCodes.isArrowKey(event.getKeyCode())) {
				event.stopPropagation();
				event.preventDefault();
				return;
			}
		}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Indicates if keyboard navigation is enabled for the Tree and for a given
	 * TreeItem. Subclasses of Tree can override this function to selectively
	 * enable or disable keyboard navigation.
	 * @param currentItem the currently selected TreeItem
	 * @return <code>true</code> if the Tree will response to arrow keys by changing the currently selected item
	 */
	@SuppressWarnings("static-method")
	protected boolean isKeyboardNavigationEnabled(final TreeViewItem<T> currentItem) {
		return true;
	}
	private void _keyboardNavigation(final Event event) {
		// Handle keyboard events if keyboard navigation is enabled
		if (this.isKeyboardNavigationEnabled(_focusedItem)) {
			int code = event.getKeyCode();

			switch (KeyCodes.maybeSwapArrowKeysForRtl(code,LocaleInfo.getCurrentLocale().isRTL())) {
			case KeyCodes.KEY_UP: {
				List<TreeViewItem<T>> slibingsBefore = _focusedItem.getSiblingsBefore();
				if (slibingsBefore != null) {
					_onFocus(slibingsBefore.get(slibingsBefore.size()-1));
				} else if (_focusedItem.getDirectAncestor() != null) {
					_onFocus(_focusedItem.getDirectAncestor());
				}
				break;
			}
			case KeyCodes.KEY_DOWN: {
				List<TreeViewItem<T>> slibingsAfter = _focusedItem.getSiblingsAfter();
				if (slibingsAfter != null) {
					_onFocus(slibingsAfter.get(0));
				} else {
					TreeViewItem<T> focusedItemAncestor = _focusedItem.getDirectAncestor();
					while (focusedItemAncestor != null && focusedItemAncestor.getNextSibling() == null) {
						focusedItemAncestor = focusedItemAncestor.getDirectAncestor();
					}
					if (focusedItemAncestor != null && focusedItemAncestor.getNextSibling() != null) _onFocus(focusedItemAncestor.getNextSibling());
				}
				break;
			}
			case KeyCodes.KEY_LEFT: {
				if (_focusedItem.getDirectAncestor() == null) return;
				_onFocus(_focusedItem.getDirectAncestor());
				break;
			}
			case KeyCodes.KEY_RIGHT: {
				if (!_focusedItem.hasChildren()) return;
				if (_focusedItem.isCollapsed()) _focusedItem.expand();
				_onFocus(_focusedItem.getChildAt(0));	// move focus
				break;
			}
			default: {
				return;
			}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Expands the root nodes
	 */
	public void expand() {
		if (_expanderElement != null) _expanderElement.setPropertyBoolean("checked",true);
	}
	/**
	 * Collapse the root nodes
	 */
	public void collapse() {
		if (_expanderElement != null) _expanderElement.setPropertyBoolean("checked",false);
	}
	/**
	 * @return true if the root nodes are expanded
	 */
	public boolean isExpanded() {
		return _expanderElement != null ? _expanderElement.getPropertyBoolean("checked")
								 		: true;		// when there's no caption it's expanded by default
	}
	/**
	 * @return true if the root nodes are collapsed
	 */
	public boolean isCollapsed() {
		return !this.isExpanded();
	}
	/**
	 * Expands all nodes
	 * WARNING If lazy loading is in use, this forces all node loading...
	 * @see TreeView#expandAllLoadedNodes()
	 */
	public void expandAllNodes() {
		_expandChildNodeset(this.getChildren(),
							false);		// forces child loading
	}
	/**
	 * Expands all loaded nodes
	 * @see #expandAllNodes()
	 */
	public void expandAllLoadedNodes() {
		_expandChildNodeset(this.getChildren(),
							true);		// do NOT force child loading
	}
	private void _expandChildNodeset(final Collection<TreeViewItem<T>> childItems,
									 final boolean onlyLoaded) {
		if (CollectionUtils.hasData(childItems)) {
			for (TreeViewItem<T> item : childItems) {
				boolean forceChildLoading = !onlyLoaded;
				item.expand(forceChildLoading);
				_expandChildNodeset(item.getChildren(),		// recurse
									onlyLoaded);
			}
		}
	}
	/**
	 * Ensures that the currently-selected item is visible, opening its parents
	 * and scrolling the tree as necessary.
	 */
	public void ensureSelectedItemVisible() {
		if (_focusedItem == null) return;
		TreeViewItem<T> parent = _focusedItem.getDirectAncestor();
		while (parent != null) {
			parent.expand();
			parent = parent.getDirectAncestor();
		}
	}
	/**
	 * Enable or disable scrolling a tree item into view when it is selected.
	 * Scrolling into view is enabled by default.
	 */
	public void setScrollOnSelectEnabled(final boolean enable) {
		_scrollOnSelectEnabled = enable;
	}
	/**
	 * Determines whether selecting a tree item will scroll it into view.
	 */
	public boolean isScrollOnSelectEnabled() {
		return _scrollOnSelectEnabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
////
/////////////////////////////////////////////////////////////////////////////////////////
	private void _updateAriaAttributes() {
		// Set the 'aria-level' state. To do this, the computation of the currently selected item's level is needed
		Roles.getTreeitemRole()
			 .setAriaLevelProperty(_focusedItem.getElement(),
					 			   TreeViewUtils.itemLevelOf(_focusedItem));

		// Set the 'aria-setsize' state. To do this, the computation of the currently selected item's slibings number is needed
		Roles.getTreeitemRole()
			 .setAriaSetsizeProperty(_focusedItem.getElement(),
					 				 _focusedItem.getChildCount());

		// Set the 'aria-posinset' state. To do this, the computation of the currently selected item's index between slibings is needed
		int curSelectionIndex = _focusedItem.getChildIndex(_focusedItem);
		Roles.getTreeitemRole()
			 .setAriaPosinsetProperty(_focusedItem.getElement(),
					 				  curSelectionIndex + 1);

		// Set the 'aria-expanded' state. This depends on the state of the currently selected item.
		// If the item has no children, we remove the 'aria-expanded' state.
		if (_focusedItem.getChildCount() == 0) {
			Roles.getTreeitemRole()
				 .removeAriaExpandedState(_focusedItem.getElement());

		} else {
			Roles.getTreeitemRole()
				 .setAriaExpandedState(_focusedItem.getElement(),
						 			   ExpandedValue.of(_focusedItem.isExpanded()));
		}

		// Make sure that 'aria-selected' is true.
		Roles.getTreeitemRole()
			 .setAriaSelectedState(_focusedItem.getElement(),
					 			   SelectedValue.of(true));

		// Update the 'aria-activedescendant' state for the focusable element to match the id of the currently selected item
		Roles.getTreeRole()
			 .setAriaActivedescendantProperty(this.getElement(),
					 						  Id.of(_focusedItem.getElement()));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public HandlerRegistration addClickHandler(final ClickHandler handler) {
		return this.addHandler(handler,
							   ClickEvent.getType());
	}
	@Override
	public HandlerRegistration addValueChangeAtHandler(final ValueChangeAtHandler<TreeViewItem<T>,Boolean> handler) {
		return this.addHandler(handler,
							   ValueChangeAtEvent.getTYPE());
	}
	@Override
	public HandlerRegistration addSelectionHandler(final SelectionHandler<TreeViewItem<T>> handler) {
		return this.addHandler(handler,
							   SelectionEvent.getType());
	}
	@Override
	public final HandlerRegistration addItemCollapseHandler(final TreeViewItemCollapseHandler<TreeViewItem<T>> handler) {
		return this.addHandler(handler,
							   TreeViewItemCollapseEvent.getTYPE());
	}
	@Override
	public HandlerRegistration addItemExpandHandler(final TreeViewItemExpandHandler<TreeViewItem<T>> handler) {
		return this.addHandler(handler,
							   TreeViewItemExpandEvent.getTYPE());
	}
	@Override
	public final HandlerRegistration addOpenHandler(final OpenHandler<TreeViewItem<T>> handler) {
		return this.addHandler(handler,
							   OpenEvent.getType());
	}
	@Override
	public HandlerRegistration addCloseHandler(final CloseHandler<TreeViewItem<T>> handler) {
		return this.addHandler(handler,
							   CloseEvent.getType());
	}
	@Override
	public HandlerRegistration addBlurHandler(final BlurHandler handler) {
		return this.addDomHandler(handler,
								  BlurEvent.getType());
	}
	@Override
	public HandlerRegistration addFocusHandler(final FocusHandler handler) {
		return this.addDomHandler(handler,
							 	  FocusEvent.getType());
	}
	@Override
	public HandlerRegistration addKeyDownHandler(final KeyDownHandler handler) {
		return this.addDomHandler(handler,
							 	  KeyDownEvent.getType());
	}
	@Override
	public HandlerRegistration addKeyPressHandler(final KeyPressHandler handler) {
		return this.addDomHandler(handler,
							 	  KeyPressEvent.getType());
	}
	@Override
	public HandlerRegistration addKeyUpHandler(final KeyUpHandler handler) {
		return this.addDomHandler(handler,
							 	  KeyUpEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
		return this.addDomHandler(handler,
								  MouseUpEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
		return this.addHandler(handler,
						  	   MouseDownEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
		return this.addDomHandler(handler,
								  MouseMoveEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
		return this.addDomHandler(handler,
								  MouseOutEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
		return this.addDomHandler(handler,
								  MouseOverEvent.getType());
	}
	@Override
	public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
		return this.addDomHandler(handler,
								  MouseWheelEvent.getType());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  HasWidgets
//	It's important the children attachments since if this is NOT done, children events
// 	would NOT be fired
//	i.e.	If an item is composed by a button widget the button's onClick() event is
//			never fired!
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void doAttachChildren() {
		try {
			AttachDetachException.tryCommand(this,
											 AttachDetachException.attachCommand);
		} finally {
			DOM.setEventListener(this.getElement(),
								 this);
		}
	}
	@Override
	protected void doDetachChildren() {
		try {
			AttachDetachException.tryCommand(this,
											 AttachDetachException.detachCommand);
		} finally {
			DOM.setEventListener(this.getElement(),
								 null);
		}
	}
	@Override
	public Iterator<Widget> iterator() {
		final Widget[] widgets = new Widget[_descendantWidgets.size()];
		_descendantWidgets.keySet().toArray(widgets);
		return WidgetIterators.createWidgetIterator(this,widgets);
	}
	@Override
	public void add(final Widget w) {
		TreeViewItem<T> item = new TreeViewItem<T>(_itemViewObjectToWidgetTransformer);
		item.setWidget(w);
		this.addItem(item);
	}
	@Override
	public void add(final IsWidget w) {
		this.add(w.asWidget());
	}
	@Override
	public boolean remove(final IsWidget w) {
		return this.remove(w.asWidget());
	}
	@Override
	public boolean remove(final Widget w) {
		TreeViewItem<T> item = _descendantWidgets.get(w);
		if (item == null) return false;
		item.setWidget(null);	// Delegate to TreeViewItem.setWidget, which performs correct removal.
		return true;
	}
	@Override
	public void clear() {
		this.removeAllChilds();
	}
}
