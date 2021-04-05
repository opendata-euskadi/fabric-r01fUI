package r01ui.base.components.taglist;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;

import lombok.experimental.Accessors;
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
	private static final String BTN_SELECTED_STYLE = "danger";
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
	}
	public VaadinTagListComponent(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_itemCaptionGenerator = itemCaptionGenerator;
		////////// create components
		_hlyTagsContainer = new HorizontalLayout();
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
	@Override @SuppressWarnings("unchecked")
	public Collection<T> getValue() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(_buttonIterator(),Spliterator.ORDERED),
																		false)	// not parallel
							.map(btn -> (T)btn.getData())	// buttons have the data stored
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
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTIONS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public void setItemCaptionGenerator(final ItemCaptionGenerator<T> itemCaptionGenerator) {
		_buttonIterator().forEachRemaining(btn -> btn.setDescription(itemCaptionGenerator.apply((T)btn.getData())));	// remember that button vaadin's DATA object contains the VALUE
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
	private void _replaceValueButtons(final Collection<T> values,
									  final ItemCaptionGenerator<T> itemCaptionGen) {
		// [1] - Remove all components
		_hlyTagsContainer.removeAllComponents();

		// [2] - Create buttons
		values.stream()
			  .forEach(val -> {
				  			String lbl = itemCaptionGen.apply(val);
							Button btnVal = new Button(lbl);
							btnVal.setDescription(itemCaptionGen.apply(val));							
							btnVal.setData(val);	// BEWARE!! store the val as data
							btnVal.addClickListener(// when clicking a [button] select the corresponding [item]
													e -> {
														// [1] - If there exists an already selected button, unselect it
														Button btnPrev = _findSelectedValueButton();
														if (btnPrev != null) btnPrev.removeStyleName(BTN_SELECTED_STYLE);

														// [2] - Select the button
														Button btn = e.getButton();		// should be btnVal
														if (btn == null) throw new IllegalStateException();
														btn.addStyleName(BTN_SELECTED_STYLE);
												    });
				  			_hlyTagsContainer.addComponent(btnVal);
			  		   });
	}
	@SuppressWarnings("unused")
	private Button _findValueButton(final T val) {
		return _findValueButton(btn -> btn.getData().equals(val));
	}
	private Button _findSelectedValueButton() {
		return _findValueButton(btn -> {
									boolean outHasSelectedStyle = false;
									String style = btn.getStyleName();
									if (Strings.isNOTNullOrEmpty(style)) {
										outHasSelectedStyle = StringSplitter.using(Splitter.on(" "))
																		  	.at(style)
																		  	.indexOf(BTN_SELECTED_STYLE) >= 0;
									}
									return outHasSelectedStyle;
								});
	}
	@SuppressWarnings("null")
	private Button _findValueButton(final Predicate<Button> pred) {
		Button outButton = null;
		Iterator<Button> btnIt = _buttonIterator();
		do {
			Button btn = btnIt.hasNext() ? btnIt.next() : null;
			if (btn != null
			 && pred.test(btn)) {
				outButton = btn;
				break;
			}
		} while (btnIt.hasNext() 
			  && outButton == null);
		return outButton;
	}
	private Iterator<Button> _buttonIterator() {
		Iterator<Component> btnIt = _hlyTagsContainer.iterator();
		return Iterators.transform(btnIt,
								   comp -> (Button)comp);
	}
}
