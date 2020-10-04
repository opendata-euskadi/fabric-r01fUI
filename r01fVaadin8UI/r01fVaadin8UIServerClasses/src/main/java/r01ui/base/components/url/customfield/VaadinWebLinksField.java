package r01ui.base.components.url.customfield;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import r01f.ui.i18n.UII18NService;
import r01f.ui.weblink.UIViewWebLink;
import r01ui.base.components.url.weblink.VaadinWebLinkGrid;

/**
 * A vaadin field used to include a {@link VaadinWebLinkGrid} ([web link] grid) in a form
 */
public class VaadinWebLinksField
	 extends CustomField<Collection<UIViewWebLink>> {

	private static final long serialVersionUID = -6020522807343940165L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * A [web link] grid
	 */
	private final VaadinWebLinkGrid _weblinkGrid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinWebLinksField(final UII18NService i18n) {
		_weblinkGrid = new VaadinWebLinkGrid(i18n);
		// fire a value change event whenever something changes
		_weblinkGrid.addValueChangeListener(valChangeEvent -> {
												ValueChangeEvent<Collection<UIViewWebLink>> evt = new ValueChangeEvent<>(this,			// source component
																														 this.getValue(),// before & after
																														 true);			// user originated
												this.fireEvent(evt);
											});
		_weblinkGrid.addDataProviderListener(dataChangeEvent -> {
												ValueChangeEvent<Collection<UIViewWebLink>> evt = new ValueChangeEvent<>(this,			// source component
																														 this.getValue(),// before & after
																														 true);			// user originated
												this.fireEvent(evt);
											 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CustomField
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component initContent() {
		return _weblinkGrid;
	}
	@Override
	public Collection<UIViewWebLink> getValue() {
		// do not return empty picks
		Collection<UIViewWebLink> links = _weblinkGrid.getItems();
		return links;
	}
	@Override
	protected void doSetValue(final Collection<UIViewWebLink> value) {
		_weblinkGrid.setItems(value);
	}
	@Override
	public void clear() {
		_weblinkGrid.setItems(Lists.newArrayList());
	}
}
