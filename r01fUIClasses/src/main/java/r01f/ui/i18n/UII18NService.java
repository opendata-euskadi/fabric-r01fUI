/**
 * Created on 16/3/2015
 *
 * @author iayerbeg - I�aki Ayerbe
 * (c) 2015 EJIE: Eusko Jaurlaritzako Informatika Elkartea
 */
package r01f.ui.i18n;

import java.io.Serializable;
import java.util.Locale;

import r01f.guids.OID;
import r01f.locale.Language;

/**
 * This interface allows the message translation. It can be injected
 * using the guice framework. 
 */
public interface UII18NService 
         extends Serializable {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The messages bundle basename suffix
	 */
	public final String I18N_MESSAGES = ".i18n.messages";
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the I18N message.
	 * @param key the key for the desired string
	 * @param args the arguments used to format the message
	 * @return the string for the given key
	 */
	String getMessage(String key,Object... args);
	/**
	 * Returns the I18N message
	 * @param key
	 * @param args
	 * @return
	 */
	String getMessage(OID key,Object... args);
	
	/**
	 * Returns the I18N message.
	 * @param key the key for the desired string
	 * @param locale the locale for which a resource bundle is desired
	 * @param args the arguments used to format the message
	 * @return the string for the given key
	 */
	String getMessage(Locale locale, 
					  String key, Object... args);
	/**
	 * Returns the I18N message.
	 * @param key the key for the desired string
	 * @param locale the locale for which a resource bundle is desired
	 * @param args the arguments used to format the message
	 * @return the string for the given key
	 */
	String getMessage(Locale locale,
					  OID key,Object... args);
	
    /**
     * @return the current locale
     */
    public Locale getCurrentLocale();
    
    /**
     * @return the current language
     */
    public Language getCurrentLanguage();
}
