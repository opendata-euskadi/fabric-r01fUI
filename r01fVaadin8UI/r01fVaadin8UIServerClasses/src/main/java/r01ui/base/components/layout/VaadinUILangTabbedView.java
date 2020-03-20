package r01ui.base.components.layout;

import java.util.Collection;

import com.vaadin.ui.Component;

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
 * Base type for lang-dependent tabbed views
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
@Deprecated // see VaadinUILangHTabbedView
public abstract class VaadinUILangTabbedView<// the data being binded at the view; usually D=VIL but it does NOT have to be that way
											 D extends UIViewObject,
											 // the component used to edit / show the [lang-dependent] view object (VIL)
										  	 V extends Component & VaadinComponent & HasLanguage
										 	         & VaadinFormHasVaadinUIBinder<D>, 		// the view uses vaadin ui binder
										 	 // the [view obj] that contains [lang dependent view objs] (VIL)
										 	 VBL extends UIViewObjectByLanguage<VIL>,				// the view obj that contains lang dependent view objs
										 	 // the [lang dependent view obj]
										 	 VIL extends UIViewObjectInLanguage> 					// the lang dependent view obj										 	 
	          extends VaadinUILangHTabbedView<D,V,
	          								  VBL,VIL> {

	private static final long serialVersionUID = -6533304106434670974L;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinUILangTabbedView(final UII18NService i18n,
						       	  final Collection<Language> langs,
						       	  final VaadinViewFactoryFrom<Language,V> viewByLangFactory) {
		super(i18n,
			  langs,
			  viewByLangFactory);
	}
}
