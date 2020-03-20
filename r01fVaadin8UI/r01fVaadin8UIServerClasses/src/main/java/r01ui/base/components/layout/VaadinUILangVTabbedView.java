package r01ui.base.components.layout;

import java.lang.reflect.Method;
import java.util.Collection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinComponent;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactoryFrom;
import r01f.ui.viewobject.UIViewObject;
import r01f.ui.viewobject.UIViewObjectByLanguage;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01f.util.types.locale.Languages;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;
/**
 * Base type for lang-dependent VERTICAL tabbed views
 * <pre>
 * ++[R01UILangTabbedView]++++++++++++++++++++++++++++++++++++++++++
 * +     ________________________________________________________  +
 * + [es]                                                        | +
 * + [eu]                                                        | +
 * +    |    Each of the tabs is a [V]                           | +
 * +    |                                                        | +
 * +    +________________________________________________________+ +
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinUILangVTabbedView<// the data being binded at the view; usually D=VIL but it does NOT have to be that way
									 D extends UIViewObject,
									 // the component used to edit / show the [lang-dependent] view object (VIL)
									 V extends Component & VaadinComponent & HasLanguage
									 		 & VaadinFormHasVaadinUIBinder<D>, 		// the view uses vaadin ui binder
									 // the [view obj] that contains [lang dependent view objs] (VIL)
									 VBL extends UIViewObjectByLanguage<VIL>,				// the view obj that contains lang dependent view objs
									 // the [lang dependent view obj]
									 VIL extends UIViewObjectInLanguage> 		// the lang dependent view obj	{
	 extends VaadinUILangTabbedViewBase<D,V,
	 									VBL,VIL> {


	private static final long serialVersionUID = 7796651278395051870L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Collection<V> _tabs;
	private final VerticalLayout _vlyTabHandles;
	private final HorizontalLayout _hly;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUILangVTabbedView(final UII18NService i18n,
								   final VaadinViewFactoryFrom<Language,V> viewByLangFactory) {
		super(i18n,
			  viewByLangFactory);
		// the tabs collection
		_tabs = Lists.newArrayList();
		
		// The vertical handles
		_vlyTabHandles = new VerticalLayout();
		_vlyTabHandles.setSpacing(false);
		_vlyTabHandles.setMargin(false);
		
		// layout
		_hly = new HorizontalLayout(_vlyTabHandles);
		_hly.setSizeFull();
		_hly.setMargin(false);
		_hly.setComponentAlignment(_vlyTabHandles,Alignment.TOP_LEFT);
		_hly.setExpandRatio(_vlyTabHandles,1F);
		
		super.setCompositionRoot(_hly);
	}
	public VaadinUILangVTabbedView(final UII18NService i18n,
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
		// add the view component 
		_tabs.add(view);
		
		// create a [button] for the language
		Language lang = view.getLanguage();
		Button btnLang = new Button(Languages.languageLowerCase(lang));
		btnLang.setStyleName(ValoTheme.BUTTON_LINK);
		btnLang.addClickListener(clickEvent -> this.setSelectedTab(view));
		
		// add to the handles [vertical layout]
		_vlyTabHandles.addComponent(btnLang);
	}
	@Override @SuppressWarnings("unchecked")
	public V getSelectedTab() {
		V currentView = (V)_hly.getComponent(1);
		return currentView; 
	}
	@Override
	public void setSelectedTab(final V view) {
		if (_hly.getComponentCount() > 1) {
			V currentView = this.getSelectedTab();
			if (currentView == view) return;
			
			// replace the component
			_hly.removeComponent(currentView);
		}
		view.setCaption(null);	// horizontal tabs do NOT need caption
		_hly.addComponent(view);
		_hly.setComponentAlignment(view,Alignment.TOP_LEFT);
		_hly.setExpandRatio(view,20F);
		
		// change the button style
		Language lang = view.getLanguage();
		String langCode = Languages.languageLowerCase(lang);
		_vlyTabHandles.iterator()
					  .forEachRemaining(btn -> {
						  					if (btn.getCaption().equalsIgnoreCase(langCode)) {
						  						btn.addStyleName("r01VTabSelected");
						  					} else {
						  						btn.removeStyleName("r01VTabSelected");
						  					}
					  					});
	}
	@Override
	public void setSelectedTab(final int index) {
		this.setSelectedTab(Iterables.get(_tabs,index));
	}
	@Override
	public Registration addSelectedTabChangeListener(final SelectedTabChangeListener listener) {
		return this.addListener(SelectedTabChangeEvent.class, 
						   		listener,SELECTED_TAB_CHANGE_METHOD);
	}
	private static final Method SELECTED_TAB_CHANGE_METHOD;
	static {
		try {
			SELECTED_TAB_CHANGE_METHOD = SelectedTabChangeListener.class.getDeclaredMethod("selectedTabChange",
																						   SelectedTabChangeEvent.class);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException("Internal error finding methods in TabSheet");	// This should never happen
		}
	}
}
