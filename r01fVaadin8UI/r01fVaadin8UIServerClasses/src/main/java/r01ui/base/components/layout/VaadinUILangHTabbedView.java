package r01ui.base.components.layout;

import java.util.Collection;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinComponent;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactoryFrom;
import r01f.ui.viewobject.UIViewObject;
import r01f.ui.viewobject.UIViewObjectByLanguage;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

/**
 * lang-dependent HORIZONTAL tabbed views
 * <pre>
 * ++[R01UILangTabbedView]++++++++++++++++++++++++++++++++++++++++++
 * +                                                               +
 * +   +- [ es ] [ eu ] ---------------------------------------- + +
 * +   |                                                         | +
 * +   |                                                         | +
 * +   |   Each of the tabs is a [V]                             | + 
 * +   |                                                         | +
 * +   |                                                         | +
 * +   +---------------------------------------------------------+ +
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinUILangHTabbedView<// the data being binded at the view; usually D=VIL but it does NOT have to be that way
									 D extends UIViewObject,
									 // the component used to edit / show the [lang-dependent] view object (VIL)
									 V extends Component & VaadinComponent & HasLanguage
									 		 & VaadinFormHasVaadinUIBinder<D>, 		// the view uses vaadin ui binder
									 // the [view obj] that contains [lang dependent view objs] (VIL)
									 VBL extends UIViewObjectByLanguage<VIL>,				// the view obj that contains lang dependent view objs
									 // the [lang dependent view obj]
									 VIL extends UIViewObjectInLanguage> 					// the lang dependent view obj	{
	 extends VaadinUILangTabbedViewBase<D,V,
	 									VBL,VIL> {

	private static final long serialVersionUID = 8242794789527302463L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TabSheet _tabs = new TabSheet();
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUILangHTabbedView(final UII18NService i18n,
								   final VaadinViewFactoryFrom<Language,V> viewByLangFactory) {
		super(i18n,
			  viewByLangFactory);
		super.setCompositionRoot(_tabs);
	}
	public VaadinUILangHTabbedView(final UII18NService i18n,
								   final Collection<Language> langs,
								   final VaadinViewFactoryFrom<Language,V> viewByLangFactory) {
		this(i18n,
			 viewByLangFactory);
		this.addTabsFor(langs);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TABS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _addLanguageTabComponent(final V view) {
		_tabs.addComponent(view);
	}
	
	@Override @SuppressWarnings("unchecked")
	public V getSelectedTab() {
		return (V)_tabs.getSelectedTab();
	}
	@Override
	public void setSelectedTab(final V view) {
		_tabs.setSelectedTab(view);
	}
	@Override
	public void setSelectedTab(final int index) {
		_tabs.setSelectedTab(index);
	}
	@Override
	public Registration addSelectedTabChangeListener(final SelectedTabChangeListener listener) {
		return _tabs.addSelectedTabChangeListener(listener);
	}
}
