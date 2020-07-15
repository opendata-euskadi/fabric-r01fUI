package r01ui.base.components.valueselector;

import java.util.Collection;

import com.vaadin.ui.ItemCaptionGenerator;

import lombok.experimental.Accessors;

/**
 * A component that combines a combo with a [tag list] of the combo-selected values
 * <pre>
 * 			[Combo       \/]
 * 			---------------------------
 * 			[x] selected combo value 1
 * 			[x] selected combo value 2
 * 			...
 * </pre>
 * @param <T>
 */
@Deprecated // see r01ui.base.components.taglist.VaadinValueSelectorComponent
@Accessors(prefix="_")
public class VaadinValueSelectorComponent<T>
	 extends r01ui.base.components.taglist.VaadinValueSelectorComponent<T> {

	private static final long serialVersionUID = 5420720079052991200L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinValueSelectorComponent(final T nullValue) {
		super(nullValue);
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue) {
		super(caption,
			  nullValue);
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final Collection<T> values) {
		super(caption,
			  nullValue,
			  values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final T... values) {
		super(caption,
			  nullValue,
			  values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final T... values) {
		super(caption,
			  nullValue,
			  itemCaptionGenerator,
			  values);
	}
	public VaadinValueSelectorComponent(final String caption,
										final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final Collection<T> values) {
		super(caption,
			  nullValue,
			  itemCaptionGenerator,
			  values);
	}
	public VaadinValueSelectorComponent(final T nullValue,
										final Collection<T> values) {
		super(nullValue,
			  values);;
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final T nullValue,
										final T... values) {
		super(nullValue,
			  values);
	}
	@SuppressWarnings("unchecked")
	public VaadinValueSelectorComponent(final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final T... values) {
		super(nullValue,
			  itemCaptionGenerator,
			  values);
	}
	public VaadinValueSelectorComponent(final T nullValue,
										final ItemCaptionGenerator<T> itemCaptionGenerator,
										final Collection<T> values) {
		super(nullValue,
			  itemCaptionGenerator,
			  values);
	}
}
