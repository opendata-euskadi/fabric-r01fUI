/**
 * Created on 16/3/2015
 *
 * @author iayerbeg - I�aki Ayerbe
 * (c) 2015 EJIE: Eusko Jaurlaritzako Informatika Elkartea
 */
package r01f.ui.i18n;

import java.util.Locale;

import r01f.guids.OID;
import r01f.locale.I18NBundleAccess;
import r01f.locale.Language;

/**
 * This interface allows the message translation. It can be injected
 * using the guice framework. 
 */
public interface UII18NService 
		 extends I18NBundleAccess {
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
	 * Returns a message in the given locale
	 * @param locale
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(final Language locale,
						 	 final OID key, final Object... args);
	/**
	 * Returns a message in the given locale
	 * @param locale
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(final Language locale,
							 final String key, final Object... args);
	/**
	 * @return the current language
	 */
	public Language getCurrentLanguage();
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Returns a message in the given locale
	 * @param locale
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(final Locale locale,
						 	 final OID key, final Object... args);
	/**
	 * Returns a message in the given locale
	 * @param locale
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(final Locale locale,
							 final String key, final Object... args);
	/**
	 * @return the current locale
	 */
	public Locale getCurrentLocale();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public I18NBundleAccess getCurrentI18NBundleAccess();
	public I18NBundleAccess getI18NBundleAccessFor(final Locale locale);
}
