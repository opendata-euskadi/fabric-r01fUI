package r01ui.base.components.layout;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.facets.HasLanguage;
import r01f.locale.Language;
import r01f.reflection.ReflectionUtils;
import r01f.reflection.ReflectionUtils.FieldAnnotated;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.LangIndependentVaadinViewField;
import r01f.ui.vaadin.view.VaadinComponent;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactoryFrom;
import r01f.ui.vaadin.view.VaadinViewHasVaadinViewObjectBinder;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViewTracksChanges;
import r01f.ui.viewobject.UIViewHasViewObject;
import r01f.ui.viewobject.UIViewObject;
import r01f.ui.viewobject.UIViewObjectByLanguage;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.locale.Languages;

/**
 * Base type for lang-dependent tabbed views
 * <pre>
 * ++[R01UILangTabbedView]++++++++++++++++++++++++++++++++++++++++++
 * +                                                               +
 * +   ++ [ es ] [ eu ] ++++++++++++++++++++++++++++++++++++++++++ +
 * +   +                                                         + +
 * +   +                                                         + +
 * +   +   Each of the tabs is a [V]                             + + 
 * +   +                                                         + +
 * +   +                                                         + +
 * +   +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ +
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * 
 * Language IN-dependent fields:
 * 		Any component of [V] annotated with @R01UILangIndependentViewField
 * 		will be updated in all languages when changed in any of them
 * 		ie:
 * 			If a view component declared as: 
 * 				@R01UILangIndependentViewField
 *				private final TextField _txtName = new TextField();
 *			when this component is updated in the [es] tab, the new value
 *			is automatically copied to the corresponding component at the [eu] tab
 *
 * @param <V>
 * @param <VBL>
 * @param <VIL>
 */
@Slf4j
public abstract class VaadinUILangTabbedView<// the data being binded at the view; usually D=VIL but it does NOT have to be that way
											 D extends UIViewObject,
											 // the component used to edit / show the [lang-dependent] view object (VIL)
										  	 V extends Component & VaadinComponent & HasLanguage
										 	         & VaadinViewHasVaadinViewObjectBinder<D>, 		// the view uses vaadin ui binder
										 	 // the [view obj] that contains [lang dependent view objs] (VIL)
										 	 VBL extends UIViewObjectByLanguage<VIL>,				// the view obj that contains lang dependent view objs
										 	 // the [lang dependent view obj]
										 	 VIL extends UIViewObjectInLanguage> 					// the lang dependent view obj										 	 
	          extends TabSheet 
           implements TabSheet.SelectedTabChangeListener,
  			 		  UIViewHasViewObject<VBL>,
  			 		  VaadinViewTracksChanges,
  			 		  VaadinViewI18NMessagesCanBeUpdated,
  			 		  VaadinViewHasVaadinViewObjectBinder<VBL> {
	
	private static final long serialVersionUID = -4245277897023751719L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final UII18NService _i18n;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	protected final R01UITabbedLangViews _langViews;
	
	protected VBL _viewObject;
	protected boolean _syncLangViews;	// stops the [lang view] lang-independent data sync-ing
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings({ "rawtypes","unchecked" })
	public VaadinUILangTabbedView(final UII18NService i18n,
						       	  final Collection<Language> langs,
						       	  final VaadinViewFactoryFrom<Language,V> viewByLangFactory) {
		_i18n = i18n;
		
		// store the language views
		_langViews = new R01UITabbedLangViews(// ... using the view factory, create a view for each language 
											  FluentIterable.from(langs)
													.transform(
														(lang) -> {
															// create the tab
															final V langView = viewByLangFactory.from(i18n,
																									  lang);
															
															// set the tab caption
															if (Strings.isNullOrEmpty(langView.getCaption())) {
																final String captionKey = Strings.customized("tab.caption.{}",
																									   		 Languages.languageLowerCase(lang));
																langView.setCaption(_i18n.getMessage(captionKey));
															}
															return new R01UITabbedLangView(lang,langView);
														})
													.toList());
		
		// There exists view fields that has the same value no matter the language
		// so when any of these fields is updated in any of the language views, 
		// the value of the same field at the other language views must be updated
		for (final V view : _langViews.viewIterable()) {
			final FieldAnnotated<LangIndependentVaadinViewField>[] annLangIndFields = ReflectionUtils.fieldsAnnotated(view.getClass(),
																											    	  LangIndependentVaadinViewField.class);
			if (CollectionUtils.hasData(annLangIndFields)) {
				for (final FieldAnnotated<LangIndependentVaadinViewField> annLangIndField : annLangIndFields) {
					final Field langIndField = annLangIndField.getField();		// a field annotated with @R01UILangIndependentViewField
					
					if (!ReflectionUtils.isImplementing(langIndField.getType(),HasValue.class)) {
						log.error("Error while registering [value change listener] for lang-dependent view: " + 
								  "field {} of {} is annotated with @{} BUT it's NOT an instance of {}: IGNORED",
								  langIndField.getName(),view,
								  LangIndependentVaadinViewField.class.getSimpleName(),
								  HasValue.class.getSimpleName());
						continue;
					}
					
					final HasValue<?> langIndHasValue = (HasValue<?>)ReflectionUtils.fieldValue(view,langIndField,false);	// ... the field value on the view
					// add a value change listener so when the field gets updated, the corresponding
					// fields at the other language dependent views are also updated with the same value					
					langIndHasValue.addValueChangeListener((e) -> {
																if (!_syncLangViews) return;
																
																// Get the updated value
																final Object updatedVal = e.getValue();
																// ... and set it to the corresponding field at the other language views
																for (final V otherView : _langViews.viewIterable()) {																
																	final HasValue otherLangIndHasValue = (HasValue)ReflectionUtils.fieldValue(otherView,langIndField,false);
																	otherLangIndHasValue.setValue(updatedVal);
																	
																	// 
																	if (otherView instanceof VaadinViewTracksChanges) {
																		VaadinViewTracksChanges tracksChanges = (VaadinViewTracksChanges)otherView;
																		tracksChanges.setViewDataChanged(false);	
																	}
																}
																if (view instanceof VaadinViewTracksChanges) {
																	VaadinViewTracksChanges tracksChanges = (VaadinViewTracksChanges)view;
																	tracksChanges.setViewDataChanged(langIndHasValue != null  
																								  && updatedVal != null 
																								  && langIndHasValue.getValue().equals(updatedVal));																	
																}																
															});
				}
			}
		}
		
		// Handle tab changes
		this.addSelectedTabChangeListener(this);
		
		// Set the first tab the one for the current language
		// ... the other language tabs are set in the received order
		final R01UITabbedLangView firstView = _langViews.tabFormFor(_i18n.getCurrentLanguage())
													  .or(_langViews.tabFormFor(Language.DEFAULT)
															  		.orNull());
		if (firstView == null) throw new IllegalStateException("Could NOT find a view for " + _i18n.getCurrentLanguage() + " nor for " + Language.DEFAULT);
		
		firstView.setCurrent(true);
		this.addComponent(firstView.getView());		// first tab: current language
		
		final Collection<R01UITabbedLangView> remainingTabs = _langViews.allTabsExcept(firstView);
		for (final R01UITabbedLangView otherView : remainingTabs) this.addComponent(otherView.getView());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TAB CHANGE EVENT HANDLER	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public void selectedTabChange(final SelectedTabChangeEvent event) {
		final R01UITabbedLangView currTabView = _langViews.currentTab();
		if (currTabView != null
		 && this.getSelectedTab() == currTabView.getView()) return;	// no changed tab
		
		// change the view
		final V newCurrView = (V)this.getSelectedTab();
		_langViews.changeCurrentTabTo(newCurrView);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UIVaadinViewTracksChanges                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setViewDataChanged(final boolean changed) {
		_langViews.viewIterable()
		  		  .forEach(view -> {
		  			  			if (view instanceof VaadinViewTracksChanges) {
			  			  			VaadinViewTracksChanges tracksChanges = (VaadinViewTracksChanges)view;
			  			  			tracksChanges.setViewDataChanged(changed);
		  			  			}
		  		  		   });
	}
	@Override
	public boolean hasViewDataChanged() {
		final boolean outChanges = false;
		for (final V view : _langViews.viewIterable()) {
			if (view instanceof VaadinViewTracksChanges) {
				VaadinViewTracksChanges tracksChanges = (VaadinViewTracksChanges)view;
				if (tracksChanges.hasViewDataChanged()) return true;
			}
		}
		return outChanges;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Although the usual case is that the view-binded object (of type D) is the [view object] of type VIL
	 * (D == VIL), there are circumstances where the view-binded object is NO of type VIL (D != VIL)
	 * ... so when binding the [view object] to the [ui controls] the [view object] of type VIL 
	 * MUST be transformed to D
	 */
	@SuppressWarnings("unchecked")
	public D transformViewObjInLangToBindedObj(final VIL viewObjInLang) {
		// Override this method if D != VIL
		return (D)viewObjInLang;	// if D == VIL there's nothing to be transformed
	}
	/**
	 * Although the usual case is that the view-binded object (of type D) is the [view object] of type VIL
	 * (D == VIL), there are circumstances where the view-binded object is NO of type VIL (D != VIL)
	 * ... so when getting back the [view object] from the [ui controls] the binded object of type D 
	 * MUST be copied into an instance of the [view object] of type VIL
	 */
	public void copyBindedObjDataToViewObjectInLang(final D bindedViewObjInLang,final VIL viewObjInLang) {
		// override this method if D != VIL
		return;	// if D == VIL just do nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[VIEW OBJECT] > [UI-CONTROLS]                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void bindViewTo(final VBL viewObject) {
		_viewObject = viewObject; 
		
		// Set the [ui control] values from [view object properties]
		// (unlike binder.readBean [ui controls] are binded to [view object properties] 
		//  so when an [ui control] changes, the [view object property] is also changed)
		
		// just "tell" all language-dependent view to bind to the corresponding
		// language-dependent view object
		_syncLangViews = false;	// BEWARE!	while setting the [view object] into the [lang view] 
								//			STOP the [lang independent] components value sync-ing
		for (final V langView : this.langViewIterable()) {
			// get the lang view object
			final Language lang = langView.getLanguage();
			final VIL viewObjInLang = viewObject.getViewObjectFor(lang);
			
			if (viewObjInLang == null) {
				log.warn("NO [view object] for lang={}: cannot create the corresponding LangTabbedView!",lang);
				continue;
			}
			
			// maybe it has to be transformed to be binded: VIL -> D
			D bindedViewObjInLang = this.transformViewObjInLangToBindedObj(viewObjInLang); 
			
			// ... and bind it into the view
			langView.bindViewTo(bindedViewObjInLang);
		}
		_syncLangViews = true;	// the [lang view] can now be sync-ed
	}
	@Override
	public void readBean(final VBL viewObject) {
		// Set the [ui control] values from [view object properties]
		// (the [ui controls] are NOT binded to [view object properties]
		//  so when an [ui control] changes, the change is NOT reflected
		//  at the [view object property]
		
		// just "tell" all language-dependent view to bind to the corresponding
		// language-dependent view object
		_syncLangViews = false;	// BEWARE!	while setting the [view object] into the [lang view] 
								//			STOP the [lang independent] components value sync-ing
		for (final V langView : this.langViewIterable()) {
			final Language lang = langView.getLanguage();
			final VIL viewObjInLang = viewObject.getViewObjectFor(lang);
			
			// maybe it has to be transformed to be binded: VIL -> D
			D bindedViewObjInLang = this.transformViewObjInLangToBindedObj(viewObjInLang);
			
			// ... and read the data
			langView.readBean(bindedViewObjInLang);
		}
		_syncLangViews = true;	// the [lang view] can now be sync-ed
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[UI-CONTROLS] > [VIEW OBJECT]                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public VBL getViewObject() {
		if (_viewObject == null) throw new IllegalStateException(String.format("If using %s#getViewObject(), the view object MUST have been previously set using %s#bindViewTo(viewObj)",
																 			   VaadinViewHasVaadinViewObjectBinder.class.getSimpleName(),VaadinViewHasVaadinViewObjectBinder.class.getSimpleName()));
		// force each lang-dep view object to be readed
		// just "tell" all language-dependent view to get to the corresponding
		// language-dependent view object
		for (final V langView : this.langViewIterable()) {
			// get the binded object
			Language lang = langView.getLanguage();
			D bindedViewObjInLang = langView.getViewObject();
			
			// get the view object in that language
			final VIL viewObjInLang = _viewObject.getViewObjectFor(lang);
			
			// and copy the data back to this object						
			this.copyBindedObjDataToViewObjectInLang(bindedViewObjInLang,viewObjInLang);
		}
		return _viewObject;
	}
	@Override
	public boolean writeBeanIfValid(final VBL viewObj) {
		// just "tell" all language-dependent view to write the corresponding
		// language-dependent view object
		boolean allTabsHasValidData = true;
		for (final V langView : this.langViewIterable()) {
			if (langView instanceof VaadinViewTracksChanges) {
				VaadinViewTracksChanges tracksChanges = (VaadinViewTracksChanges)langView;
				if (!tracksChanges.hasViewDataChanged()) continue;	// no changes
			}
			
			// get the view object for the lang
			Language lang = langView.getLanguage();
			VIL viewObjInLang = viewObj.getViewObjectFor(lang);
			
			// maybe the binded object has another type: VIL -> D
			D bindedViewObjInLang = this.transformViewObjInLangToBindedObj(viewObjInLang); 
			
			// UI -> object
			boolean langViewBeanWritten = langView.writeBeanIfValid(bindedViewObjInLang);

			// and now back from D to VIL
			this.copyBindedObjDataToViewObjectInLang(bindedViewObjInLang,viewObjInLang);		
			
			if (!langViewBeanWritten) allTabsHasValidData = false;
		}
		return allTabsHasValidData;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isValid() {
		boolean allValid = true;
		for (final V langView : this.langViewIterable()) {
			final boolean langViewValid = langView.isValid();
			if (!langViewValid) {
				allValid = false;
				break;
			}
		}
		return allValid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UPDATE I18N MESSAGES                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		for (final R01UITabbedLangView view : _langViews) {
			// update the tab caption
			final String newCaptionKey = Strings.customized("tab.caption.{}",
													  Languages.languageLowerCase(view.getLang()));
			view.getView()
				.setCaption(_i18n.getMessage(newCaptionKey));
			
			// update the tab view i18n messages
			if (view.getView() instanceof VaadinViewI18NMessagesCanBeUpdated) {
				VaadinViewI18NMessagesCanBeUpdated i18nUpdatable = (VaadinViewI18NMessagesCanBeUpdated)view.getView();
				i18nUpdatable.updateI18NMessages(i18n);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return an {@link Iterable} over the lang views
	 */
	public Iterable<V> langViewIterable() {
		return _langViews.viewIterable();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	private class R01UITabbedLangView {
		@Getter private final Language _lang;
		@Getter private final V _view;
		@Getter @Setter private boolean _current;
	}
	@RequiredArgsConstructor
	private class R01UITabbedLangViews
	   implements Iterable<R01UITabbedLangView> {
		
		private final Collection<R01UITabbedLangView> _langViews;
		
		@Override
		public Iterator<R01UITabbedLangView> iterator() {
			return _langViews.iterator();
		}
		public Iterable<V> viewIterable() {
			return Iterables.transform(this,
									   (v) -> v.getView());
		}
		public Collection<R01UITabbedLangView> allTabsExcept(final R01UITabbedLangView view) {
			return FluentIterable.from(_langViews)
								 .filter((otherView) -> view == otherView ? false : true)	// not the given view
								 .toList();
		}
		public Optional<R01UITabbedLangView> tabFormFor(final Language lang) {
			return Iterables.tryFind(_langViews,
								 	 (v) -> v.getLang().is(lang));	
		}
		@SuppressWarnings("unused")
		public Optional<R01UITabbedLangView> tabFormFor(final V view) {
			return Iterables.tryFind(_langViews,
									 (v) -> v.getView() == view);
		}
		public R01UITabbedLangView currentTab() {
			final R01UITabbedLangView langView = Iterables.tryFind(_langViews,
																 (v) -> v.isCurrent())
														  .orNull();
			if (langView == null) log.warn("NOT currently selected tab view!");
			return langView;
		}
		private R01UITabbedLangView changeCurrentTabTo(final V view) {
			final R01UITabbedLangView langView = Iterables.tryFind(_langViews,
															 	 (v) -> v.getView() == view)
														  .orNull();
			if (langView == null) throw new IllegalStateException("Could NOT find required tab view!");
			return langView;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private Iterator<V> _languageViewIterator() {
		// AbstractComponent implements ComponentContainer which in turn implements HasComponents 
		// ... HasComponent implements Iterable<Component>
		// so since this object extends TabSheet (which in turn extends AbstractComponent), 
		// all TabSheet components can be iterated over
		// ... and since we're sure that the ONLY component added to the TabSheet are the 
		// 	   language-dependent views (forms), we're sure that all components are of type F
		final Iterator<Component> componentIt = this.iterator();
		return Iterators.transform(componentIt,
								   new Function<Component,V>() {
											@Override @SuppressWarnings("unchecked")
											public V apply(final Component comp) {
												return (V)comp;
											}
								   });
	}
}
