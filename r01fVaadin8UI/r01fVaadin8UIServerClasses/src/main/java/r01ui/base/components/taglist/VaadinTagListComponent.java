package r01ui.base.components.taglist;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DescriptionGenerator;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import r01f.patterns.reactive.ForDisposeObserver;
import r01f.patterns.reactive.ForSelectObserver;
import r01f.util.types.StringSplitter;
import r01f.util.types.Strings;

/**
 * A component [tag list] of values like
 * <pre>
 * 			[x] value 1    [x] value 2
 * 			...
 * </pre>
 * @param <T>
 */
@Accessors(prefix="_")
public class VaadinTagListComponent<T>
	 extends CustomField<Collection<T>> {

	private static final long serialVersionUID = 5420720079052991200L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String BTN_SELECTED_STYLE = "selected";
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	private final ItemCaptionGenerator<T> _itemCaptionGenerator;
	private final DescriptionGenerator<T> _itemDescriptionGenerator;
	private final String _deleteButtonDescription;

	private final HorizontalLayout _hlyTagsContainer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinTagListComponent() {
		_itemCaptionGenerator = null;
		_itemDescriptionGenerator = null;
		_deleteButtonDescription = null;
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
		_hlyTagsContainer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_itemCaptionGenerator = itemCaptionGenerator;
		_itemDescriptionGenerator = null;
		_deleteButtonDescription = null;
		
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
		_hlyTagsContainer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator) {
		_itemCaptionGenerator = itemCaptionGenerator;
		_itemDescriptionGenerator = itemDescriptionGenerator;
		_deleteButtonDescription = null;
		
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
		_hlyTagsContainer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
	}
	
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final String deleteButtonDescription) {
		_itemCaptionGenerator = itemCaptionGenerator;
		_itemDescriptionGenerator = itemDescriptionGenerator;
		_deleteButtonDescription = deleteButtonDescription;
		
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
		_hlyTagsContainer.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
	}
	public VaadinTagListComponent(final String caption) {
		this();
		this.setCaption(caption);
	}
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final String deleteButtonDescription) {
		this(itemCaptionGenerator, 
			 itemDescriptionGenerator,
			 deleteButtonDescription);
		
		this.setCaption(caption);
	}
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator) {
		this(itemCaptionGenerator, 
			 itemDescriptionGenerator);
		this.setCaption(caption);
	}
// ---------------------------------
	public VaadinTagListComponent(final String caption,
								  final Collection<T> values) {
		this(caption);
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final String caption,
								  final T... values) {
		this(caption);
		this.setValues(values);
	}
	public VaadinTagListComponent(final Collection<T> values) {
		this();
		this.setValues(values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final T... values) {
		this();
		this.setValues(values);
	}
// ---------------------------------
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final Collection<T> values) {
		this(itemCaptionGenerator);
		this.setCaption(caption);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final T... values) {
		this(itemCaptionGenerator);
		this.setCaption(caption);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final Collection<T> values) {
		this(itemCaptionGenerator);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final T... values) {
		this(itemCaptionGenerator);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final Collection<T> values) {
		this(itemCaptionGenerator, 
			itemDescriptionGenerator);
		this.setValues(itemCaptionGenerator,
					   itemDescriptionGenerator,
					   values);
	}
	
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final T... values) {
		this(itemCaptionGenerator,
			 itemDescriptionGenerator);
		this.setValues(itemCaptionGenerator,
					   itemDescriptionGenerator,
					   values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final String deleteButtonDescription,
								  final T... values) {
		this(itemCaptionGenerator,
			 itemDescriptionGenerator,
			 deleteButtonDescription);
		this.setValues(itemCaptionGenerator,
					   itemDescriptionGenerator,
					   deleteButtonDescription,
					   values);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final DescriptionGenerator<T> itemDescriptionGenerator,
								  final String deleteButtonDescription,
								  final Collection<T> values) {
		this(itemCaptionGenerator, 
			itemDescriptionGenerator,
			deleteButtonDescription);
		this.setValues(itemCaptionGenerator,
					   itemDescriptionGenerator,
					   deleteButtonDescription,
					   values);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		////////// layout
		return _hlyTagsContainer;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALUE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Collection<T> getValue() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(_itemIterator(),Spliterator.ORDERED),
																		false)	// not parallel
							.map(item -> item.getData())	// items have the data stored
							.collect(Collectors.toSet());
	}
	@Override
	protected void doSetValue(final Collection<T> value) {
		this.setValues(_itemCaptionGenerator,
					   _itemDescriptionGenerator,
					   value);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE! if an event is NOT raised when the value changes, the vaadin binder
//			does NOT works
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
    public Registration addValueChangeListener(final ValueChangeListener<Collection<T>> listener) {
		Registration reg = super.addValueChangeListener(listener);
		return reg;
	}
	public void setDisposeSubscriber(final ForDisposeObserver<T> subscriber) {
		_itemIterator().forEachRemaining(item -> item.setDisposeSubcriber(subscriber));
	}
	public void setSelectSubscriber(final ForSelectObserver<T> subscriber) {
		_itemIterator().forEachRemaining(item -> item.setSelectSubscriber(subscriber));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTIONS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setItemCaptionGenerator(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_itemIterator().forEachRemaining(item -> item.setDescription(itemCaptionGenerator.apply(item.getData())));	// remember that button vaadin's DATA object contains the VALUE
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public void setValues(final T... values) {
		this.setValues(Lists.<T>newArrayList(values));
	}
	public void setValues(final Collection<T> values) {
		this.setValues(null,// no item caption generator > use the value
					   null, // no description generator > use the value
					   values);
	}
	@SuppressWarnings("unchecked")
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final T... values) {
		this.setValues(itemCaptionGenerator,
						null, // no description generator > use caption generator
					   Lists.<T>newArrayList(values));
	}
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final Collection<T> values) {
		ItemCaptionGenerator<T> theItemCaptionGen = itemCaptionGenerator != null
															? itemCaptionGenerator
															: val -> val.toString();
		_replaceValueButtons(values,
							 theItemCaptionGen,
							 null,
							 null);
	}
	@SuppressWarnings("unchecked")
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final DescriptionGenerator<T> itemDescriptionGenerator,
						  final T... values) {
		this.setValues(itemCaptionGenerator,
					  itemDescriptionGenerator,
					   Lists.<T>newArrayList(values));
	}
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final DescriptionGenerator<T> itemDescriptionGenerator,
						  final Collection<T> values) {
		ItemCaptionGenerator<T> theItemCaptionGen = itemCaptionGenerator != null
															? itemCaptionGenerator
															: val -> val.toString();
		_replaceValueButtons(values, 
							 theItemCaptionGen, 
							 itemDescriptionGenerator,
							 null);
		
	}
	
	@SuppressWarnings("unchecked")
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final DescriptionGenerator<T> itemDescriptionGenerator,
						  final String deleteButtonDescription,
						  final T... values) {
		this.setValues(itemCaptionGenerator,
					  itemDescriptionGenerator,
					  deleteButtonDescription,
					   Lists.<T>newArrayList(values));
	}
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final DescriptionGenerator<T> itemDescriptionGenerator,
						  final String deleteButtonDescription,
						  final Collection<T> values) {
		ItemCaptionGenerator<T> theItemCaptionGen = itemCaptionGenerator != null
															? itemCaptionGenerator
															: val -> val.toString();
		_replaceValueButtons(values, 
							 theItemCaptionGen, 
							 itemDescriptionGenerator,
							 deleteButtonDescription);
		
	}
	
	private void _replaceValueButtons(final Collection<T> values,
									  final ItemCaptionGenerator<T> itemCaptionGen,
									  final DescriptionGenerator<T> descriptionGen,
									  final String deleteButtonDescription) {
		// [1] - Remove all components
		_hlyTagsContainer.removeAllComponents();

		// [2] - Create items
		Iterator<T> it = values.iterator();
		if (it.hasNext()) {
			T val = it.next();
			// item
			if (descriptionGen != null) {
				_addTagListItemToContainer(val,itemCaptionGen, descriptionGen, deleteButtonDescription);
			}
			else {
				_addTagListItemToContainer(val, itemCaptionGen, deleteButtonDescription);
			}
		}
		it.forEachRemaining(val -> {
								// separator
								Label separator = new Label();
								separator.setValue(VaadinIcons.CHEVRON_RIGHT.getHtml());
								separator.setContentMode(ContentMode.HTML);
								separator.addStyleName(ValoTheme.LABEL_LIGHT);
								separator.setSizeFull();
								_hlyTagsContainer.addComponent(separator);
								_hlyTagsContainer.setComponentAlignment(separator,Alignment.MIDDLE_CENTER);
								
								// item
								if (descriptionGen != null) {
									_addTagListItemToContainer(val,itemCaptionGen, descriptionGen, deleteButtonDescription);
								}
								else {
									_addTagListItemToContainer(val, itemCaptionGen, deleteButtonDescription);
								}
							});
	}
	private void _addTagListItemToContainer(final T val, 
											final ItemCaptionGenerator<T> itemCaptionGen,
											final String deleteButtonDescription) {
		VaadinTagListItem item = new VaadinTagListItem(val,
				  									   itemCaptionGen,
				  									   deleteButtonDescription);
		item.addItemButtonClickListener(// when clicking a [button] select the corresponding [item]
											e -> itemClick(e));
		_hlyTagsContainer.addComponent(item);
	}
		

	private void _addTagListItemToContainer(final T val, 
											final ItemCaptionGenerator<T> itemCaptionGen,
											final DescriptionGenerator<T> itemDescriptionGen,
											final String deleteButtonDescription) {
		VaadinTagListItem item = new VaadinTagListItem(val,
				  									   itemCaptionGen,
				  									   itemDescriptionGen,
				  									   deleteButtonDescription);
		item.addItemButtonClickListener(// when clicking a [button] select the corresponding [item]
											e -> itemClick(e));
		_hlyTagsContainer.addComponent(item);
	}
	
	@SuppressWarnings("unchecked")
	private void itemClick(final ClickEvent e) {
		// [1] - If there exists an already selected button, unselect it
			VaadinTagListItem prevItem = _findSelectedItem();
			if (prevItem != null) prevItem.setSelected(false);

			// [2] - Select the button
			Button btn = e.getButton();		// should be btnVal
			VaadinTagListItem selItem = (VaadinTagListItem)btn.getParent()
															  .getParent(); 
										// _findItem(theItem -> theItem._btnItem == btn);	// the item that contains the button
			selItem.setSelected(true);
	}
	private VaadinTagListItem _findSelectedItem() {
		return _findItem(item -> item.isSelected());
	}
	private VaadinTagListItem _findItemFor(final T item) {
		return _findItem(tagListIem -> tagListIem.getData().equals(item));
	}
	@SuppressWarnings("null")
	private VaadinTagListItem _findItem(final Predicate<VaadinTagListItem> pred) {
		VaadinTagListItem outItem = null;
		Iterator<VaadinTagListItem> itemIt = _itemIterator();
		do {
			VaadinTagListItem item = itemIt.hasNext() ? itemIt.next() : null;
			if (item != null
			 && pred.test(item)) {
				outItem = item;
				break;
			}
		} while (itemIt.hasNext() 
			  && outItem == null);
		return outItem;
	}
	@SuppressWarnings("unchecked")
	private Iterator<VaadinTagListItem> _itemIterator() {
		// create an stream of components
		Stream<Component> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(_hlyTagsContainer.iterator(),
										                        							Spliterator.ORDERED),
										                false);
		// filter the VaadinTagListItem ones
		Iterator<Component> itemIt = stream.filter(c -> c instanceof VaadinTagListComponent.VaadinTagListItem)
										   .iterator();
		// return an iterator
		return Iterators.transform(itemIt,
								   comp -> (VaadinTagListItem)comp);
	}
	@SuppressWarnings("unused")
	private Iterable<VaadinTagListItem> _itemIterable() {
		return new Iterable<VaadinTagListItem>() {
						@Override
						public Iterator<VaadinTagListComponent<T>.VaadinTagListItem> iterator() {
							return _itemIterator();
						}
			   };
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean removeTagFor(final T item) {
		VaadinTagListItem tagListItem = _findItemFor(item);
		if (tagListItem == null) return false;
		
		_hlyTagsContainer.removeComponent(tagListItem);
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * A component like:
	 * <pre>
	 * 		[x] [item]
	 * </pre>
	 */
	private class VaadinTagListItem
		  extends Composite {

		private static final long serialVersionUID = -3528551824350605879L;
		
		private final Button _btnDispose;
		private final Button _btnItem;
		
		private final T _data;
		
		private ForDisposeObserver<T> _disposeSubscriber;
		private ForSelectObserver<T> _selectSubscriber;
		
		public VaadinTagListItem(final T val) {
			_data = val;
			
			////////// UI
			// dispose button
			_btnDispose = new Button(VaadinIcons.CLOSE_CIRCLE);
			_btnDispose.addStyleNames(ValoTheme.BUTTON_TINY,
									  ValoTheme.BUTTON_ICON_ONLY,
									  ValoTheme.BUTTON_BORDERLESS,
									  "r01-labelpicker-item-delete");
			_btnDispose.setDescription(_deleteButtonDescription);
			
			// item button
			_btnItem = new Button();
			_btnItem.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
								   ValoTheme.BUTTON_TINY,
									"r01-labelpicker-item-value");					
			
			////////// Layout
			HorizontalLayout ly = new HorizontalLayout(_btnDispose,_btnItem);
			ly.addStyleName("r01-labelpicker-item");
			ly.setSpacing(false);
			ly.setSizeFull();
			ly.setExpandRatio(_btnDispose, 1);
			ly.setExpandRatio(_btnItem, 3);
			ly.setComponentAlignment(_btnDispose, Alignment.MIDDLE_RIGHT);
			this.setCompositionRoot(ly);
			
			////////// Behavior
			_setBehavior();
		}
		
		public VaadinTagListItem(final T val,
								 final ItemCaptionGenerator<T> itemCaptionGen,
								 final DescriptionGenerator<T> itemDescriptionGen) {
			this(val);
			
			_btnItem.setCaption(itemCaptionGen.apply(val));
			_btnItem.setDescription(itemDescriptionGen.apply(val));							
			
		}
		
		public VaadinTagListItem(final T val,
								 final ItemCaptionGenerator<T> itemCaptionGen) {
			this(val);
			
			_btnItem.setCaption(itemCaptionGen.apply(val));
			_btnItem.setDescription(itemCaptionGen.apply(val));							
			
		}
		
		public VaadinTagListItem(final T val,
								 final ItemCaptionGenerator<T> itemCaptionGen,
								 final DescriptionGenerator<T> itemDescriptionGen,
								 final String deleteButtonDescription) {
			this(val);
			
			_btnItem.setCaption(itemCaptionGen.apply(val));
			_btnItem.setDescription(itemDescriptionGen.apply(val));
			_btnDispose.setDescription(deleteButtonDescription);
			
		}
		
		public VaadinTagListItem(final T val,
								 final ItemCaptionGenerator<T> itemCaptionGen,
								 final String deleteButtonDescription) {
			this(val);
			
			_btnItem.setCaption(itemCaptionGen.apply(val));
			_btnItem.setDescription(itemCaptionGen.apply(val));
			_btnDispose.setDescription(deleteButtonDescription);
			
		}
		private void _setBehavior() {
			// when the [dispose] button is clicked...
			_btnDispose.addClickListener(clickEvent -> {
											if (_disposeSubscriber != null) _disposeSubscriber.onDispose(_data);
										 });
			// when the [item] is selected
			_btnItem.addClickListener(clickEvent -> {
											if (_selectSubscriber != null) _selectSubscriber.onSelect(_data);
									  });
		}
		@Override
		public T getData() {
			return _data;
		}
		public void setSelected(final boolean selected) {
			if (!selected) {
				_btnItem.removeStyleName(BTN_SELECTED_STYLE);
			} else {
				_btnItem.addStyleName(BTN_SELECTED_STYLE);
			}
		}
		public boolean isSelected() {
			boolean outHasSelectedStyle = false;
			String style = _btnItem.getStyleName();
			if (Strings.isNOTNullOrEmpty(style)) {
				outHasSelectedStyle = StringSplitter.using(Splitter.on(" "))
												  	.at(style)
												  	.indexOf(BTN_SELECTED_STYLE) >= 0;
			}
			return outHasSelectedStyle;
		}
		public Registration addItemButtonClickListener(final Button.ClickListener clickListener) {
			return _btnItem.addClickListener(clickListener);
		}
		public Registration addDisposeButtonClickListener(final Button.ClickListener clickListener) {
			return _btnDispose.addClickListener(clickListener);
		}
		public void setDisposeSubcriber(final ForDisposeObserver<T> subscriber) {
			_disposeSubscriber = subscriber;
		}
		public void setSelectSubscriber(final ForSelectObserver<T> subscriber) {
			_selectSubscriber = subscriber;
		}
	}
}
