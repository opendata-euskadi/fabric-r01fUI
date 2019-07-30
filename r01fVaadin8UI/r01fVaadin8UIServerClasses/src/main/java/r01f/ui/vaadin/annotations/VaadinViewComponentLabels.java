package r01f.ui.vaadin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * view component's [caption] and [place holder] can be set using one of
 * these two methods:
 *		a) use VaadinViews util that "scans" view's @VaadinViewComponentLabels components fields
 *		   and "automatically" sets the caption and place-holder of the component
 *		   ie:
 *		        a view component field declared like:
 *					@VaadinViewComponentLabels(captionI18NKey="object.name",useCaptionI18NKeyAsPlaceHolderKey=true)
 *					private final TextField _txtName = new TextField();
 *				will look after the i18n key [object.name] and set the caption
 *				... since useCaptionI18NKeyAsPlaceHolderKey=true, the same text will be used as placeholder
 *				alternatively, a different i18n key can be set for the place-holder
 *
 *		   To use this annotation-based method, just:
 *				VaadinViews.using(_i18n)
 *							 .setI18NLabelsOf({theView instance})
 *
 *		b) Manually set the caption and place-holder:
 *				_txtName.setCaption(_i18n.getMessage("object.name"));
 *				_txtName.setPlaceholder(_i18n.getMessage("object.name"));
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VaadinViewComponentLabels {
    public String captionI18NKey();
    public String placeholderI18NKey() default "";
    public boolean useCaptionI18NKeyAsPlaceHolderKey() default false;
}