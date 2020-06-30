package r01ui.base.components.contact;

import java.util.Collection;

import com.vaadin.data.Binder.Binding;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.renderers.HtmlRenderer;

import r01f.locale.I18NKey;
import r01f.patterns.Factory;
import r01f.types.contact.ContactInfoUsage;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.VaadinGridComponentBase;
import r01ui.base.components.VaadinListDataProviders;
import r01ui.base.components.form.VaadinDetailEditForm;

/**
 * Creates a [contact mean] (phone, email...) manage component like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ Contact Mean title  [ADD]                         +
 *      +												    +
 *      + +-----------------------------------------------+ +
 *      + | [mean]      | [usage] | [default] | [private] | +
 *      + +-------------|---------|-----------|-----------+ +
 *      + |             |         |           |           | +
 *      + |             |         |           |           | +
 *      + |             |         |           |           | +
 *      + +-----------------------------------------------+ +
 *      +++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * @param <V>
 * @param <W>
 */
public abstract class VaadinContactMeanManageBase<V extends VaadinContactMeanObject,
												  W extends VaadinDetailEditForm<V>>
	 		  extends VaadinGridComponentBase<V,W> {

	private static final long serialVersionUID = 679801385310874400L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactMeanManageBase(final UII18NService i18n,
									   final W winDetailEdit,
									   final Factory<V> viewObjFactory,
									   final I18NKey captionKey) {
		super(i18n,
			  winDetailEdit,
			  viewObjFactory,
			  captionKey);
	}
	@Override
	protected void _configureGridColumns(final UII18NService i18n) {
		// enable editing
		_grid.getEditor().setEnabled(true);			// editable items
		_grid.getEditor().setBuffered(false);		// inline editing!! no [save] / [cancel] buttons
		
		////////// Grid controls
		// usage
		ComboBox<ContactInfoUsage> cmbUsage = new ComboBox<>();
		cmbUsage.setItems(ContactInfoUsage.values());
		cmbUsage.setItemCaptionGenerator(usage -> usage.nameUsing(i18n));
		// default
		CheckBox chkDefault = new CheckBox();
		// private
		CheckBox chkPrivate = new CheckBox();

		////////// Grid binding
		// usage
		Binding<V,ContactInfoUsage> usageBinding = _gridBinder.forField(cmbUsage)
															  .asRequired()
															  .bind(V::getUsage,V::setUsage);
		// default
		Binding<V,Boolean> defaultBindig = _gridBinder.forField(chkDefault)
													  .bind(V::isDefault,V::setDefault);
		// private
		Binding<V,Boolean> privateBindig = _gridBinder.forField(chkPrivate)
													  .bind(V::isPrivate,V::setPrivate);

		

		////////// Columns
		// usage
		_grid.addColumn(viewObj -> viewObj.getUsage() != null ? viewObj.getUsage().nameUsing(i18n) : null)
				.setEditorBinding(usageBinding)
				.setCaption(i18n.getMessage("contact.mean.usage"))
				.setExpandRatio(2)
				.setResizable(false)
				.setId("usage");
		 // default
		_grid.addColumn(viewObj -> (viewObj.isDefault() ? VaadinIcons.CHECK : VaadinIcons.CLOSE).getHtml(),
        				new HtmlRenderer())
				.setEditorBinding(defaultBindig)
			   	.setCaption(i18n.getMessage("contact.mean.default"))
			   	.setExpandRatio(1)
			   	.setResizable(false)
			   	.setId("default");
		 // private
		_grid.addColumn(viewObj -> (viewObj.isPrivate() ? VaadinIcons.CHECK : VaadinIcons.CLOSE).getHtml(),
        				new HtmlRenderer())
				.setEditorBinding(privateBindig)
				.setCaption(i18n.getMessage("contact.mean.private"))
				.setExpandRatio(1)
				.setResizable(false)
				.setId("private");

		////////// specific cols
		_configureMoreGridColumns(i18n);
		
		////////// Behavior
		// when the [default] checkbox is checked/unchecked we must ensure
		// that there only exists a single checked item
		_gridBinder.addValueChangeListener(event -> {
												if (event.getComponent() == chkDefault && (Boolean)event.getValue()) {
													// un-select the [default] checkbox at other items
													_unCheckDefaults(_gridBinder.getBean());
												}
										   });
	}
	/**
	 * Adds the contact mean specific columns
	 * @param i18n
	 */
	protected abstract void _configureMoreGridColumns(final UII18NService i18n);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void _afterAddOrEdit(final V viewObj) {
		// Called just after an item is edited or added to the grid
		// ...
		_unCheckDefaults(viewObj);
	}
	private void _unCheckDefaults(final V viewObj) {
		if (!viewObj.isDefault()) return;
		
		// cycle all over the grid items and uncheck the [default] status
		Collection<V> gridItems = VaadinListDataProviders.collectionBackedOf(_grid)
			 									         .getUnderlyingItemsCollection();
		gridItems.stream()
		   .filter(viewMean -> viewMean != viewObj)
		   .forEach(otherViewMean -> {	
			   			// set & refresh
						otherViewMean.setDefault(false);
						VaadinListDataProviders.collectionBackedOf(_grid)
											   .refreshItem(otherViewMean);
				   });
	}
}
