package r01f.ui.vaadin.view;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToBigIntegerConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.util.BeanUtil;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractDateField;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractLocalDateField;
import com.vaadin.ui.AbstractLocalDateTimeField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.guids.OID;
import r01f.locale.Language;
import r01f.patterns.FactoryFrom;
import r01f.reflection.ReflectionUtils;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.annotations.UIVaadinViewComponentLabels;
import r01f.ui.vaadin.annotations.UIVaadinViewComponentValueCannotBeNull;
import r01f.ui.vaadin.viewobject.VaadinViewObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * Utils for vaadin views
 * 
 * Bind view components annotated with @PropertyId 
 *
 */
@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinViews {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Creates a Vaadin {@link Navigator} url params from the view id and params
	 * @param viewId
	 * @param navParams
	 * @return
	 */
	public static String vaadinViewUrlPathParamOf(final VaadinViewID viewId,final Map<String,String> navParams) {
		String outUrlPathParam = null;
		if (CollectionUtils.hasData(navParams)) {
			StringBuilder paramsStr = new StringBuilder();
			paramsStr.append(viewId)
					 .append("/");
			for (Iterator<Map.Entry<String,String>> meIt = navParams.entrySet().iterator(); meIt.hasNext(); ) {
				Map.Entry<String,String> me = meIt.next();
				paramsStr.append(me.getKey())
				   		 .append("=")
						 .append(me.getValue());
				if (meIt.hasNext()) paramsStr.append("/");
			}
			outUrlPathParam = paramsStr.toString();
		} else {
			outUrlPathParam = viewId.asString();
		}
		return outUrlPathParam;
	}
	/**
	 * Creates a Vaadin {@link Navigator} url params from the view id and params
	 * @param viewId
	 * @param navParams
	 * @return
	 */
	public static String vaadinViewUrlPathParamOf(final VaadinViewID viewId,final Collection<String> navParams) {
		String outUrlPathParam = null;
		if (CollectionUtils.hasData(navParams)) {
			StringBuilder paramsStr = new StringBuilder();
			paramsStr.append(viewId)
					 .append("/");
			for (Iterator<String> meIt = navParams.iterator(); meIt.hasNext(); ) {
				String param = meIt.next();
				paramsStr.append(param);
				if (meIt.hasNext()) paramsStr.append("/");
			}
			outUrlPathParam = paramsStr.toString();
		} else {
			outUrlPathParam = viewId.asString();
		}
		return outUrlPathParam;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BINDING                                                                        
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * View components can be binded to view object's fields using one of 
	 * these two methods:
	 *		a) use UIVaadinViews util that "scans" view's @PropertyId-annotated components fields
	 *		   and "automatically" binds each component to a view's field 
	 *		   ie: 	
	 *		        a view component field declared like: 
	 *					@PropertyId("name") 
	 *					private final TextField _txtName = new TextField();
	 *				will be binded to a view object field named "name"
	 *					@Getter @Setter private String _name;
	 *
	 *		   If the field is also annotated with @R01UIViewComponentValueCannotBeNull
	 *		   the binder is instructed to "require" a value in the component 
	 *
	 *		   To use this annotation-based method, just:
	 *				UIVaadinViews.using(_binder,_i18n)
	 *								.bindComponentsOf({theView instance})
	 *								.toViewObjectOfType({the view object type});
	 *		 
	 *		b) Manually bind EVERY property like:
	 *				_binder.forField(_txtName)
	 *			  			.asRequired(_i18n.getMessage("required"))
	 *			  			.bind( "name" );
	 *
	 * Invoke automatic binding based upon @PropertyId-annotated fields 
	 * <pre class='brush:java'>
	 * 		UIVaadinViews.using(binder,i18n)
	 * 						.bindComponentsOf(view)
	 * 						.toViewObjectOfType(viewObjType);
	 * </pre>
	 * @param binder
	 * @param i18n
	 * @return
	 */
	public static <M extends VaadinViewObject> UIVaadinViewBinderBuilderViewStep<M> using(final Binder<M> binder,final UII18NService i18n) {
		return new VaadinViews() { /* nothing */ }
						.new UIVaadinViewBinderBuilderViewStep<M>(binder,i18n);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UIVaadinViewBinderBuilderViewStep<M extends VaadinViewObject> {
		private final Binder<M> _binder;
		private final UII18NService _i18n;
		
		public <V extends VaadinView> UIVaadinViewBinderBuilderViewObjStep<V,M> bindComponentsOf(final V view) {
			return new UIVaadinViewBinderBuilderViewObjStep<V,M>(view,
															   	    _binder,_i18n);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UIVaadinViewBinderBuilderViewObjStep<V extends VaadinView,M extends VaadinViewObject> {
		private final V _view;
		private final Binder<M> _binder;
		private final UII18NService _i18n;
		
		public void toViewObjectOfType(final Class<M> viewObjectType) {
			_bind(_view,viewObjectType,
				  _binder,
				  _i18n);
		}
	}
	private static <V extends VaadinView,
				    M extends VaadinViewObject> void _bind(final V view,final Class<M> viewObjectType,
														  final Binder<M> binder,
														  final UII18NService i18n)  {
		final Class<?> viewType = view.getClass();
		final Field[] viewFields = ReflectionUtils.allFields(viewType);
		for (final Field viewCompField : viewFields) {
			try {
				if (!ReflectionUtils.isImplementing(viewCompField.getType(),
													Component.class)) continue;		// do NOT process fields that are NOT vaadin component
				
				final PropertyId propertyIdAnnotation = viewCompField.getAnnotation(PropertyId.class);
				if (propertyIdAnnotation == null) continue;
				
				boolean canBeBinded = _canBeBinded(viewCompField);
					
				// [0] - Get the @PropertyId annotation which contains the viewObject's field data
				final String viewObjFieldName = propertyIdAnnotation.value();
				Class<?> viewObjFieldType = BeanUtil.getPropertyType(viewObjectType,
																	 viewObjFieldName);
				
				if (!canBeBinded) log.warn("{} view's component field {} of type {} cannot be 'automatically' binded to {}'s {} property of type {} since the component does NOT implement {}: it should be binded MANUALLY",
										   view.getClass(),viewCompField.getName(),viewCompField.getType(),
										   viewObjectType,viewObjFieldName,viewObjFieldType,
										   HasValue.class);
				
				// [1] - Get the component field and the corresponding view obj type
				HasValue<?> hasValueComp = (HasValue<?>)ReflectTools.getJavaFieldValue(view,
																					   viewCompField,HasValue.class);
				if (hasValueComp == null) continue;

				// [2] - Get the binding builder
				Binder.BindingBuilder<M,?> bindingBuilder = binder.forField(hasValueComp);
				
				// [3] - Bind
				// BEWARE!!! Order matters
				// [a] - Get the @R01UIViewComponentValueCannotBeNull annotation: cannot be null
				UIVaadinViewComponentValueCannotBeNull valueCannotBeNullAnnot = viewCompField.getAnnotation(UIVaadinViewComponentValueCannotBeNull.class);
				_bindRequiredFor(bindingBuilder,
								 hasValueComp,viewObjFieldType,
								 valueCannotBeNullAnnot,
								 i18n);
				// [b] - Null representation
				_bindNullRepresentationFor(bindingBuilder,
										   hasValueComp);
				// [c] - Converter
				_bindConverterFor(bindingBuilder,
								  viewObjFieldType);
				
				// [4] - Bind to the view object property
				bindingBuilder.bind(viewObjFieldName);
			} catch (Throwable th) {
				log.error("Could NOT bind one of the view components to {} view object: {}",
						  view.getClass(),viewObjectType,
						  th.getMessage(),th);
			}
		} // for
		
		// attach an event handler to the binder so when the binder fires a ValueChangeEvent, 
		// the view is "marked" as changed
		if (view instanceof VaadinViewTracksChanges) {
			final VaadinViewTracksChanges viewTrackChanges = (VaadinViewTracksChanges)view;
			binder.addValueChangeListener((e) -> viewTrackChanges.setViewDataChanged(true));
		}
	}
	private static <T> boolean _canBeBinded(final Field viewCompField) {
		if (!ReflectionUtils.isImplementing(viewCompField.getType(),
											HasValue.class)) return false;
		return true;
	}
	@SuppressWarnings({ "unchecked","rawtypes" })
	private static <M extends VaadinViewObject,T> void _bindConverterFor(final Binder.BindingBuilder<M,?> bindingBuilder,
											  							final Class<T> viewObjPropertyType) {	// view object property
		// Converters
		Binder.BindingBuilder<M,String> strBindingBuilder = (Binder.BindingBuilder<M,String>)bindingBuilder;
		
		if (Integer.class.isAssignableFrom(viewObjPropertyType)
		 || int.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToIntegerConverter(null,
														 		  	  	 "Must be a number"));
		} else if (Float.class.isAssignableFrom(viewObjPropertyType)
				|| float.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToFloatConverter(null, 
																	   "Must be a number"));
		} else if (Double.class.isAssignableFrom(viewObjPropertyType) 
				|| double.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToDoubleConverter(null,
																		"Must be a number"));
		} else if (Long.class.isAssignableFrom(viewObjPropertyType)
				|| long.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToLongConverter(null, 
																	  "Must be a number"));
		} else if (BigDecimal.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToBigDecimalConverter(null, 
																		    "Must be a number"));
		} else if (BigInteger.class.isAssignableFrom(viewObjPropertyType)) {
			strBindingBuilder.withConverter(new StringToBigIntegerConverter(null, 
																			"Must be a number"));
		} else if (OID.class.isAssignableFrom(viewObjPropertyType)) {
			FactoryFrom<String,T> oidFactory = new FactoryFrom<String,T>() {
														@Override
														public T from(final String oidAsStr) {
															return ReflectionUtils.invokeStaticMethod(viewObjPropertyType,
																									  "forId",new Class<?>[] {String.class},new String[] {oidAsStr});
														}
											  };
			Converter<String,T> converter = new VaadinOIDConverter(oidFactory);
			strBindingBuilder.withConverter(converter);
		} else if (Date.class.isAssignableFrom(viewObjPropertyType)) {
			Binder.BindingBuilder<M,LocalDate> dateBindingBuilder = (Binder.BindingBuilder<M,LocalDate>)bindingBuilder;
			
			dateBindingBuilder.withConverter(new LocalDateToDateConverter());
		}
	}
	private static <M extends VaadinViewObject,T> void _bindRequiredFor(final Binder.BindingBuilder<M,?> bindingBuilder,
													  				   final HasValue<?> viewComp,
													  				   final Class<T> viewObjPropertyType,
													  				   final UIVaadinViewComponentValueCannotBeNull valueCannotBeNullAnnot,
													  				   final UII18NService i18n) {
		if (valueCannotBeNullAnnot != null) {
			if (AbstractTextField.class.isAssignableFrom(viewComp.getClass())) {
				bindingBuilder.asRequired((value,context) -> {
																	String strVal = value != null ? value.toString() : null;
																	if (Strings.isNOTNullOrEmpty(strVal)) return ValidationResult.ok();
																	return ValidationResult.error(i18n.getMessage(valueCannotBeNullAnnot.value()));
															  });
				
			} else {
				bindingBuilder.asRequired((value,context) -> {
																	if (value != null) return ValidationResult.ok();
																	return ValidationResult.error(i18n.getMessage(valueCannotBeNullAnnot.value()));
															  });			
			}
		}
		if (AbstractDateField.class.isAssignableFrom(viewComp.getClass())) {
			AbstractDateField<?,?> dateComp = (AbstractDateField<?,?>)viewComp;
			
			dateComp.setDateOutOfRangeMessage(i18n.getMessage("validation.datefield.outOfRange"));
			dateComp.setParseErrorMessage(i18n.getMessage("validation.datefield.parseError"));
			
			if (AbstractLocalDateField.class.isAssignableFrom(viewComp.getClass())) {
				dateComp.setDateFormat("yyyy/MM/dd");
				if (i18n.getCurrentLanguage().isIn(Language.SPANISH)) {
					dateComp.setDateFormat("dd/MM/yyyy");
				}	
			} else if (AbstractLocalDateTimeField.class.isAssignableFrom(viewComp.getClass())) {
				// ... time format
			}
		}
	}
	@SuppressWarnings({ "unchecked" })
	private static <M extends VaadinViewObject,T> void _bindNullRepresentationFor(final Binder.BindingBuilder<M,?> bindingBuilder,
																			   	 final HasValue<?> viewComp) {
		// TextField: null representation
		if (AbstractTextField.class.isAssignableFrom(viewComp.getClass())) {
			((Binder.BindingBuilder<M,String>)bindingBuilder).withNullRepresentation("");
		} 
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTION & PLACE-HOLDER                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * view component's [caption] and [place holder] can be set using one of 
	 * these two methods:
	 *		a) use UIVaadinViews util that "scans" view's @R01UIViewComponentLabels components fields
	 *		   and "automatically" sets the caption and place-holder of the component
	 *		   ie: 	
	 *		        a view component field declared like: 
	 *					@R01UIViewComponentLabels(captionI18NKey="object.name",useCaptionI18NKeyAsPlaceHolderKey=true)
	 *					private final TextField _txtName = new TextField();
	 *				will look after the i18n key [object.name] and set the caption
	 *				... since useCaptionI18NKeyAsPlaceHolderKey=true, the same text will be used as placeholder
	 *				alternatively, a different i18n key can be set for the place-holder
	 *
	 *		   To use this annotation-based method, just:
	 *				UIVaadinViews.using(_i18n)
	 *								.setI18NLabelsOf({theView instance})
	 *		 
	 *		b) Manually set the caption and place-holder:
	 *				_txtName.setCaption(_i18n.getMessage("object.name"));				
	 *				_txtName.setPlaceholder(_i18n.getMessage("object.name"));
	 * <pre class='brush:java'>
	 * 		UIVaadinViews.using(i18n)
	 * 						.setI18NLabelsOf(view);
	 * </pre>
	 * @param binder
	 * @param i18n
	 * @return
	 */
	public static UIVaadinViewI18NLabelsStep using(final UII18NService i18n) {
		return new VaadinViews() { /* nothing */ }
						.new UIVaadinViewI18NLabelsStep(i18n);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UIVaadinViewI18NLabelsStep {
		private final UII18NService _i18n;
		
		public <V extends VaadinView> void setI18NLabelsOf(final V view) {
			_setLabels(view,
					   _i18n);
		}
	}

	private static <V extends VaadinView,
				    M extends VaadinViewObject> void _setLabels(final V view,
														  	   final UII18NService i18n)  {
		final Class<?> viewType = view.getClass();
		final Field[] viewFields = ReflectionUtils.allFields(viewType);
		for (final Field viewCompField : viewFields) {
			try {
				if (!ReflectionUtils.isImplementing(viewCompField.getType(),Component.class)) continue;	
	
				// [1] - Get the component field and the corresponding view obj type
				Component vaadinComponent = (Component)ReflectTools.getJavaFieldValue(view,
																					  viewCompField,Component.class);
				if (vaadinComponent == null) continue;		// parent field on AbstractComponent
				
				// [2] - Get the @R01UIViewComponentCaption annotation which contains the view component's caption (label)
				//		 and place holder values
				final UIVaadinViewComponentLabels labelsAnnot = viewCompField.getAnnotation(UIVaadinViewComponentLabels.class);
				if (labelsAnnot != null) {
					String viewCompCaption =  i18n.getMessage(labelsAnnot.captionI18NKey());
					String viewCompPlaceHolder = labelsAnnot.useCaptionI18NKeyAsPlaceHolderKey()
																? viewCompCaption 
																: Strings.isNOTNullOrEmpty(labelsAnnot.placeholderI18NKey()) ? labelsAnnot.placeholderI18NKey()
																															 : null;
		
					_setViewComponentCaption(i18n,
											 vaadinComponent, 
								   			 viewCompCaption,labelsAnnot.captionI18NKey());	// caption & default value
					_setViewComponentPlaceHolder(i18n,
												 vaadinComponent,
												 viewCompPlaceHolder);
				}
				// [3] - Style
				_styleViewComponent(vaadinComponent);
				
			} catch (Throwable th) {
				log.error("Could NOT set caption or placeholder of one of the view components of {}",
						  view.getClass(),
						  th.getMessage(),th);
			}
		} // for
	}	
	private static void _styleViewComponent(final Component vaadinComponent) {
		vaadinComponent.setWidth("100%");
	}
	private static void _setViewComponentCaption(final UII18NService i18n,
								  				 final Component vaadinComponent,
								  				 final String viewCompCaption,final String viewCompCaptionDefaultValue) {
		String caption = Strings.isNOTNullOrEmpty(viewCompCaption) ? viewCompCaption 
																   : viewCompCaptionDefaultValue;
		vaadinComponent.setCaption(caption);
	}
	private static void _setViewComponentPlaceHolder(final UII18NService i18n,
													 final Component vaadinComponent,
													 final String viewCompPlaceHolder) {
		if (vaadinComponent instanceof AbstractField) {
			if (Strings.isNOTNullOrEmpty(viewCompPlaceHolder)) {
				if (vaadinComponent instanceof AbstractTextField) {
					AbstractTextField absTxtField = (AbstractTextField)vaadinComponent;
					absTxtField.setPlaceholder(i18n.getMessage(viewCompPlaceHolder));
				} 
			}					
		}
		if (vaadinComponent instanceof ComboBox) {
			if (Strings.isNOTNullOrEmpty(viewCompPlaceHolder)) {
				ComboBox<?> cmb = (ComboBox<?>) vaadinComponent;
				cmb.setPlaceholder(i18n.getMessage(viewCompPlaceHolder));
			}					
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
}

