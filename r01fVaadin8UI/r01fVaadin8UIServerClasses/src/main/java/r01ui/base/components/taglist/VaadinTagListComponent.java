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
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CustomField;
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

	private final HorizontalLayout _hlyTagsContainer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinTagListComponent() {
		_itemCaptionGenerator = null;
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_itemCaptionGenerator = itemCaptionGenerator;
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
		_hlyTagsContainer.setSpacing(false);
	}
	public VaadinTagListComponent(final String caption) {
		this();
		this.setCaption(caption);
	}
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator) {
		this(itemCaptionGenerator);
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
		this(caption,
			 itemCaptionGenerator);
		this.setValues(itemCaptionGenerator,
					   values);
	}
	@SuppressWarnings("unchecked")
	public VaadinTagListComponent(final String caption,
								  final ItemCaptionGenerator<T> itemCaptionGenerator,
								  final T... values) {
		this(caption,
			 itemCaptionGenerator);
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
					   values);
	}
	@SuppressWarnings("unchecked")
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final T... values) {
		this.setValues(itemCaptionGenerator,
					   Lists.<T>newArrayList(values));
	}
	public void setValues(final ItemCaptionGenerator<T> itemCaptionGenerator,
						  final Collection<T> values) {
		ItemCaptionGenerator<T> theItemCaptionGen = itemCaptionGenerator != null
															? itemCaptionGenerator
															: val -> val.toString();
		_replaceValueButtons(values,
							 theItemCaptionGen);
	}
	@SuppressWarnings("unchecked")
	private void _replaceValueButtons(final Collection<T> values,
									  final ItemCaptionGenerator<T> itemCaptionGen) {
		// [1] - Remove all components
		_hlyTagsContainer.removeAllComponents();

		// [2] - Create items
		Iterator<T> it = values.iterator();
		if (it.hasNext()) {
			T val = it.next();
			// item
			_addTagListItemToContainer(val, itemCaptionGen);
		}
		it.forEachRemaining(val -> {
									// separator
									Label separator = new Label();
									separator.setValue(VaadinIcons.CHEVRON_RIGHT.getHtml());
									separator.setContentMode(ContentMode.HTML);
									separator.addStyleName(ValoTheme.LABEL_LIGHT);
									separator.setSizeFull();
									_hlyTagsContainer.addComponent(separator);
									_hlyTagsContainer.setComponentAlignment(separator, Alignment.MIDDLE_CENTER);
									
									// item
									_addTagListItemToContainer(val, itemCaptionGen);
							});
	
	}

	private void _addTagListItemToContainer(final T val, final ItemCaptionGenerator<T> itemCaptionGen) {
		VaadinTagListItem item = new VaadinTagListItem(val,
				  									   itemCaptionGen);
		item.addItemButtonClickListener(// when clicking a [button] select the corresponding [item]
											e -> {
												// [1] - If there exists an already selected button, unselect it
												VaadinTagListItem prevItem = _findSelectedItem();
												if (prevItem != null) prevItem.setSelected(false);

												// [2] - Select the button
												Button btn = e.getButton();		// should be btnVal
												VaadinTagListItem selItem = (VaadinTagListItem)btn.getParent()
																								  .getParent(); // _findItem(theItem -> theItem._btnItem == btn);	// the item that contains the button
												selItem.setSelected(true);
										    });
		_hlyTagsContainer.addComponent(item);
	}
	private VaadinTagListItem _findSelectedItem() {
		return _findItem(item -> item.isSelected());
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
		Stream<Component> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
										                        _hlyTagsContainer.iterator(),
										                        Spliterator.ORDERED),
										                false);
		Iterator<Component> itemIt = stream.filter(c -> c instanceof VaadinTagListComponent.VaadinTagListItem)
										   .iterator();
		return Iterators.transform(itemIt,
								   comp -> (VaadinTagListItem)comp);
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
		
		public VaadinTagListItem(final T val,
								 final ItemCaptionGenerator<T> itemCaptionGen) {
			_data = val;
			
			////////// UI
			// dispose button
			_btnDispose = new Button(VaadinIcons.CLOSE_BIG);
			_btnDispose.addStyleNames(ValoTheme.BUTTON_ICON_ONLY,
									  ValoTheme.BUTTON_BORDERLESS,
									  ValoTheme.BUTTON_SMALL);
			
			// item button
  			String lbl = itemCaptionGen.apply(val);
			_btnItem = new Button(lbl);
			_btnItem.addStyleNames(ValoTheme.BUTTON_BORDERLESS,
								  "label-item");
			_btnItem.setDescription(itemCaptionGen.apply(val));							
			
			////////// Layout
			HorizontalLayout ly = new HorizontalLayout(_btnDispose,_btnItem);
			ly.setSpacing(false);
			ly.setSizeFull();
			ly.setExpandRatio(_btnDispose, 1);
			ly.setExpandRatio(_btnItem, 3);
			ly.setComponentAlignment(_btnDispose, Alignment.MIDDLE_RIGHT);
			this.setCompositionRoot(ly);
			
			////////// Behavior
			_setBehavior();
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
