package com.google.gwt.user.client.ui;

import java.util.Collection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.DOM;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.view.CanBePainted;


class TreeViewUtils {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	static final String TREEVIEW_CAPTION_CSS_CLASS_NAME = "treeviewCaption";
	static final String TREEVIEW_CSS_CLASS_NAME = "treeview";
	static final String CONTAINER_UL_CSS_CLASS_NAME = "childContainer";
	static final String EXPANDER_CLASS_NAME = "expander";
	static final String CHECKER_CLASS_NAME = "selector";
	static final String BROKEN_RULES_OK_CLASS_NAME = "brokenRulesOK";
	static final String BROKEN_RULES_NOK_CLASS_NAME = "brokenRulesNOK";

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public static class TreeViewExpanderDOMElements {
		@Getter private final InputElement _inputElement;
		@Getter private final SpanElement _spanElement;
	}
	/**
	 * Creates an structure for a {@link TreeViewItem} or {@link TreeView} expander
	 * 		<ul class='treeview'>
	 *			<li>
	 *				// [1] - Expander
	 *				<input type="checkbox" class="expander" enabled>
	 *        		<span class="expander"></span>
	 *
	 *				// [2] - The widget
	 *				<!-- The widget -->	<!-- usually is a caption -->
	 *
	 *				// [3] - The child items
	 *        		<ul class='childContainer'>..</ul>
	 *			</li>
	 *		</ul>
	 */
	public static TreeViewExpanderDOMElements createExpanderDOMElements() {
		InputElement expanderINPUT = DOM.createInputCheck().cast();
		expanderINPUT.addClassName(TreeViewUtils.EXPANDER_CLASS_NAME);
		//expanderINPUT.setDisabled(false);

		SpanElement expanderSPAN = DOM.createSpan().cast();
		expanderSPAN.addClassName(TreeViewUtils.EXPANDER_CLASS_NAME);

		return new TreeViewExpanderDOMElements(expanderINPUT,
										   	   expanderSPAN);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static <T extends CanBePainted> int itemLevelOf(final TreeViewItem<T> item) {
		int outLevel = 0;		// Initialize itemLevel to 0 because the level value is zero-based.
		TreeViewItem<T> tempItem = item;
		while (tempItem != null) {
			tempItem = tempItem.getDirectAncestor();
			++outLevel;
		}
		return outLevel;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds the TreeView item that contains the provided {@link Element}
	 * (this {@link Element} usually is the {@link EventTarget})
	 * @param element
	 * @param tree
	 * @return
	 */
	public static <T extends CanBePainted> TreeViewItem<T> findTreeViewItemContaining(final Element element,
														  	  	 					  final TreeView<T> tree) {
		// Find the eventTarget parent LI and the tree view item associated with it
		LIElement liElement = TreeViewUtils.findTreeViewItemLIElement(element);
		TreeViewItem<T> item = _findItemIn(tree.getChildren(),
										liElement);
		return item;
	}
	public static LIElement findTreeViewItemLIElement(final Element element) {
		LIElement outLIElement = null;

		Element currElement = element;
		while (currElement != null) {
			if (LIElement.is(currElement)) {
				Element parentElement = currElement.getParentElement();
				if (UListElement.is(parentElement) && CONTAINER_UL_CSS_CLASS_NAME.equals(parentElement.getClassName())) {
					outLIElement = currElement.cast();
				} else {
					currElement = parentElement.getParentElement();
				}
			} else {
				currElement = currElement.getParentElement();
			}
			if (outLIElement != null) break;
		}
		return outLIElement;
	}
	private static <T extends CanBePainted> TreeViewItem<T> _findItemIn(final Collection<TreeViewItem<T>> items,
												   					    final LIElement element) {
		TreeViewItem<T> outItem = null;
		if (items != null && items.size() > 0) {
			for (TreeViewItem<T> item : items) {
				if (outItem != null) break;
				if (item.getElement().cast() == element) {
					outItem = item;
				} else if (item.hasChildren()) {
					outItem = _findItemIn(item.getChildren(),element);
				}
			}
		}
		return outItem;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isEventAssociatedWithItemExpander(final Element eventTarget) {
		boolean outIsExpander = false;
		if (InputElement.is(eventTarget)) {
			InputElement inputEl = eventTarget.cast();
			if (inputEl.getType().equalsIgnoreCase("checkbox")
			 && inputEl.getClassName().equalsIgnoreCase(EXPANDER_CLASS_NAME)) {
				outIsExpander = true;
			}
		}
		return outIsExpander;
	}
	public static boolean isEventAssociatedWithItemChecker(final Element eventTarget) {
		boolean outIsChecker = false;
		if (InputElement.is(eventTarget)) {
			InputElement inputEl = eventTarget.cast();
			if (inputEl.getType().equalsIgnoreCase("checkbox")
			 && inputEl.getClassName().equalsIgnoreCase(CHECKER_CLASS_NAME)) {
				outIsChecker = true;
			}
		}
		return outIsChecker;
	}
}
