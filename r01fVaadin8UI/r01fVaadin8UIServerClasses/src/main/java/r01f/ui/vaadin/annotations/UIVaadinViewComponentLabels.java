package r01f.ui.vaadin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UIVaadinViewComponentLabels {
    public String captionI18NKey();
    public String placeholderI18NKey() default ""; 
    public boolean useCaptionI18NKeyAsPlaceHolderKey() default false;
}