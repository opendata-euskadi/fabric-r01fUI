package com.google.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Delegate;
import r01f.model.facets.view.HasCaption;
import r01f.patterns.reactive.Observable;
import r01f.patterns.reactive.ObservableForChildAdditionOrRemoval;
import r01f.patterns.reactive.Observer;
import r01f.util.types.collections.CollectionUtils;

import com.google.common.collect.Iterables;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * A tag list
 */
public class TagList<T extends HasCaption> 
	 extends Widget
  implements HasClickHandlers,
  			 ObservableForChildAdditionOrRemoval<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	private static LIElement BASE_TAG_ELEM = _createBaseTagElement();
	private static LIElement _createBaseTagElement() {
		// 		<ul class='tags'>
		//			<li><span><img />Tag1</span></li>
		//			<li><span><img />Tag1</span></li>
		//			...
		//		</ul>
		LIElement containerLIEl = DOM.createElement("li").cast();
		SpanElement spanEl = DOM.createSpan().cast();
		ImageElement imgEl = DOM.createImg().cast();
		
		// assemble the structure
		spanEl.appendChild(imgEl);
		spanEl.appendChild(Document.get().createTextNode(""));
		containerLIEl.appendChild(spanEl);
		return containerLIEl;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The list of tags
	 */
	private LinkedHashMap<T,LIElement> _tags;
	/**
	 * The tags container DOM Element
	 */
	private UListElement _tagsContainerULElement;
/////////////////////////////////////////////////////////////////////////////////////////
//  ObservableForChildAdditionOrRemoval
/////////////////////////////////////////////////////////////////////////////////////////
	@Delegate(excludes=Observable.class)	
	private ObservableTagListDelegate<T> _observableDelegate;
	@Override
	public <O extends Observer> void addObserver(final O observer) {
		_observableDelegate.addObserver(observer);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructs an empty tag list.
	 */
	public TagList() {
		// [1] - Create a wrapper DIV
		_tagsContainerULElement = _createContainerULElement();
		// [2] - sets the widget dom element
		this.setElement(_tagsContainerULElement);
		// Capture events
		this.sinkEvents(Event.ONCLICK);	// = DOM.sinkEvents(this.getElement(),Event.ONCLICK)
		// Create the observable delegate
		_observableDelegate = new ObservableTagListDelegate<T>();
	}
	/**
	 * Builds the element structure:
	 * <pre class='brush:html'>
	 * 		<ul class='tags'>
	 *			<li><span><img />Tag1</span></li>
	 *			<li><span><img />Tag1</span></li>
	 *			<li><span><img />Tag1</span></li>
     *		</ul>
	 * </pre>
	 */
	private static UListElement _createContainerULElement() {
		UListElement outULElement = DOM.createElement("ul").cast();
		outULElement.addClassName("tags");
		return outULElement;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a tag
	 * @param tag
	 */
	public void addTag(final T tag) {
		this.insertTagAt(tag,
						 this.getTagCount());
	}
	/**
	 * Inserts a tag at a position
	 * @param tag
	 * @param position
	 */
	public void insertTagAt(final T tag,final int position) {
		// Check the index 
		if (position < 0 || position > this.getTagCount()) throw new IndexOutOfBoundsException();
		
		// Check the tag existence
		if (_elementForTag(tag) != null) return;	// Tag already exists
		
		// Create the LI element for the tag
		LIElement liElement = DOM.clone(BASE_TAG_ELEM,	// a simple LI	
								   		true)			// deep cloning
								 .cast();
		// Set the span text
		liElement.getChild(0)	// span
						.getChild(1)	// span text	(child(0) is the image)
							.setNodeValue(tag.getCaption());

		// Physical attach.
		if (position == this.getTagCount()) {
			_tagsContainerULElement.appendChild(liElement);
		} else {
			LIElement beforeLIElem = _tagsContainerULElement.getChild(position).cast();
			_tagsContainerULElement.insertBefore(liElement,
									 		   beforeLIElem);
		}
		// Logical attach.
		if (_tags == null) _tags = new LinkedHashMap<T,LIElement>();
		_insertMapEntryAt(_tags,
						  tag,liElement,
						  position);
		
		// Nofify observers
		_observableDelegate.notifyObserversAboutChildAddition(tag);
	}
	/**
	 * Removes a tag
	 * @param tag
	 */
	public void removeTag(final T tag) {
		LIElement tagLIElem = _elementForTag(tag);
		if (tagLIElem == null) return;

		// Physical detach the li from it's parent ul.
		_tagsContainerULElement.removeChild(tagLIElem);

		// Logical detach.
		_tags.remove(tag);
		
		// Nofify observers
		_observableDelegate.notifyObserversAboutChildRemoval(tag);
	}
	/**
	 * Removes a tag at a position
	 * @param position
	 */
	public void removeTagAt(final int position) {
		if (position < 0 || position > this.getTagCount()) throw new IndexOutOfBoundsException();
		T tag = Iterables.get(_tags.keySet(),position);
		this.removeTag(tag);
	}
	/**
	 * Removes all tags
	 */
	public void removeAllTags() {
		if (CollectionUtils.hasData(_tags)) {
			for (T tag : _tags.keySet()) this.removeTag(tag);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the number of tags
	 * @return
	 */
	public int getTagCount() {
		return CollectionUtils.hasData(_tags) ? _tags.size() : 0;
	}
	/**
	 * @return true if the tag list is empty
	 */
	public boolean isEmpty() {
		return this.getTagCount() == 0;
	}
	/**
	 * @return true if the're tags 
	 */
	public boolean hasTags() {
		return this.getTagCount() > 0;
	}
	/**
	 * @return the tags
	 */
	public Collection<T> getTags() {
		return this.hasTags() ? _tags.keySet()
							  : null;
	}
	/**
	 * @return a tags iterator
	 */
	public Iterator<T> getTagsIterator() {
		return this.hasTags() ? _tags.keySet().iterator()
							  : null;
	}
	/**
	 * @return the first tag
	 */
	public T getFirstTag() {
		return this.hasTags() ? Iterables.get(_tags.keySet(),0)
							  : null;
	}
	/**
	 * @return the last tag
	 */
	public T getLastTag() {
		return this.hasTags() ? Iterables.getLast(_tags.keySet())
							  : null;
	}
	/**
	 * Returns the tag at the provided position
	 * @param index
	 * @return
	 */
	public T getTagAt(final int index) {
		if (index < 0 || index > this.getTagCount()) throw new IndexOutOfBoundsException();
		return this.hasTags() ? Iterables.get(_tags.keySet(),index)
							  : null;
	}
	/**
	 * Returns true if the provided tag is contained within the taglist's tags
	 * @param tag
	 * @return
	 */
	public boolean contains(final T tag) {
		boolean outContains = false;
		if (this.hasTags()) {
			for (T currTag : _tags.keySet()) {
				if (currTag.equals(tag)) {
					outContains = true;
					break;
				}
			}
		}
		return outContains;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@SuppressWarnings("fallthrough")
	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		
		int eventType = DOM.eventGetType(event);
		Element eventTargetEl = DOM.eventGetTarget(event);
		
//		GWT.log("_____" + eventTargetEl.getNodeName() + ": " + event.getType());
		
		switch (eventType) {
		case Event.ONCLICK: {
			if (ImageElement.is(eventTargetEl)) {
				LIElement tagLIElement = _findTagLIElementContaining(eventTargetEl);
				T tag = _tagForElement(tagLIElement);
				this.removeTag(tag);
			}
			break;
		}
		}
	}
	@Override
	public HandlerRegistration addClickHandler(final ClickHandler handler) {
		return this.addDomHandler(handler, 
								  ClickEvent.getType());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	private LIElement _elementForTag(final T tag) {
		return _tags != null ? _tags.get(tag) : null;
	}
	private T _tagForElement(final LIElement liElement) {
		T outTag = null;
		for (Map.Entry<T,LIElement> tagsMapEntry : _tags.entrySet()) {
			if (tagsMapEntry.getValue() == liElement) {
				outTag = tagsMapEntry.getKey();
				break;
			}
		}
		return outTag;
	}
	private static LIElement _findTagLIElementContaining(final Element element) {
		LIElement outLIElement = null;
		Element currElement = element;
		while (currElement != null) {
			if (LIElement.is(currElement)) {
				outLIElement = currElement.cast();
			} else {
				currElement = currElement.getParentElement();
			}
			if (outLIElement != null) break;
		}
		return outLIElement;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Although {@link LinkedHashMap} is an insertion-ordered map, an entry cannot be inserted
	 * at a certain position so a trick must be used: 
	 * <ul>
	 * 	<li>Create a new map</li>
	 * 	<li>Divide the original map in two</li>
	 * 	<li>Insert the entries before the position in the new map</li>
	 * 	<li>insert the new entry</li>
	 * 	<li>Insert the trailing entries in the new map</li>
	 * </ul>
	 * @param map
	 * @param key
	 * @param value
	 * @param position
	 */
	private static <K,V> void _insertMapEntryAt(final LinkedHashMap<K,V> map,
												final K key,final V value,
												final int index) {
		assert (map != null);
		assert !map.containsKey(key);
		assert (index >= 0) && (index <= map.size());
		
		if (index == map.size()) {
			map.put(key,value);
			return;
		}

		// [1] - Store the elements after the position of the new entry
		int i = 0;
		List<Entry<K,V>> rest = new ArrayList<Entry<K,V>>();
		for (Entry<K,V> entry : map.entrySet()) {
			if (i++ >= index) rest.add(entry);
		}
		// [2] - Put the new entry
		map.put(key,value);
		// [3] - Put again the stored elements in the map
		for (int j = 0; j < rest.size(); j++) {
			Entry<K,V> entry = rest.get(j);
			map.remove(entry.getKey());
			map.put(entry.getKey(),entry.getValue());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
}
