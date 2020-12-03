/**
 * Created on 15/07/2014
 *
 * @author iayerbeg - Iaki Ayerbe
 * (c) 2014 EJIE: Eusko Jaurlaritzako Informatika Elkartea
 */
package r01f.ui.i18n;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation defines the message manager for a specific message bundle. 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UIMessageBundle {

	/**
	 * The bundle basename.
	 */
    String[] basename() default "r01ui.components";

}
