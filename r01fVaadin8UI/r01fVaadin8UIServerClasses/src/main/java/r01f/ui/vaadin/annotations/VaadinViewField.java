package r01f.ui.vaadin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.data.Validator;

/**
 * Annotation used in vaadin fields to automate the field-to-bean Field binding
 * View fiend-type components (implementing HasValue) can be binded to view object's fields using one of
 * these two methods:
 *		a) use VaadinViews util that "scans" view's @VaadinViewField-annotated components fields
 *		   and "automatically" binds each component to a view's field
 *		   ie:
 *		        a view component field declared like:
 *					@VaadinViewField(bindToViewObjectFieldNamed="name")
 *					private final TextField _txtName = new TextField();
 *				will be binded to a view object field named "name"
 *					@Getter @Setter private String _name;
 *
 *	   	   The binder can be instructed to "require" a value in the component
 *		   if the view component is annotated like:
 *					@VaadinViewField(bindToViewObjectFieldNamed="name",required=true)
 *					private final TextField _txtName = new TextField();
 *
 *		   To use this annotation-based method, just:
 *				VaadinViews.using(_binder,_i18n)
 *						   .bindComponentsOf({theView instance})
 *						   .toViewObjectOfType({the view object type});
 *
 *		b) Manually bind EVERY Field like:
 *				_binder.forField(_txtName)
 *			  			.asRequired(_i18n.getMessage("required"))
 *			  			.bind( "name" );
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface VaadinViewField {
	public String bindToViewObjectFieldNamed();
    public boolean required() default false;
    public String i18nKeyForRequiredMessage() default "validation.field.required.default";
	public Class<? extends Validator> useValidatorType() default VaadinVoidViewFieldValidator.class;
	public boolean bindStringConverter() default true;		// use this in combos
//	public Class<?> converter() default Void.class;
}