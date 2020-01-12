
package r01f.ui.i18n;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.guids.OID;
import r01f.locale.Language;
import r01f.reflection.scanner.AnnotatedWithScanner;
import r01f.types.JavaPackage;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.locale.Languages;


@Slf4j
@Accessors(prefix="_")
public abstract class UII18NManagerBase
		   implements UII18NService {

	private static final long serialVersionUID = -6836336412769588264L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter List<String> _i18nBundleNames = new LinkedList<String>();
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public UII18NManagerBase(final Collection<JavaPackage> pckgNames) {
		this(Thread.currentThread().getContextClassLoader(),
			 pckgNames);
	}
	public UII18NManagerBase(final ClassLoader classLoader,
							 final Collection<JavaPackage> pckgNames) {
		try {
			// find all types annotated with R01UIMessageBundle at package r01ui
			final Set<Class<?>> annotatedTypes = AnnotatedWithScanner.findTypesAnnotatedWitAt(UIMessageBundle.class,
																							  pckgNames,
																							  classLoader);
			// create a list of all bundles like:
			//		@R01UIMessageBundle( {"mybundle1","mybundle2"} )
			// ... creates a i18n bulde list like
			//			{mybundle1.i18n.messages,mybundle2.i18n.messages}
			for (final Class<?> annotatedType : annotatedTypes) {
				final String[] bundleId = _findMessageBundleAnnotation(annotatedType)
														.basename();
				for (int i = 0; i < bundleId.length; i++) {
					final String i18nBundleName = Strings.customized("{}" + I18N_MESSAGES,
																	 bundleId[i]);
					if ( !_i18nBundleNames.contains(i18nBundleName) ) _i18nBundleNames.add(i18nBundleName);
				}
			}
		} catch(final Exception e) {
			log.warn("Error while finding i18n bundle names at @{}-annotated types: {}",
					 UIMessageBundle.class.getSimpleName(),
					 e.getMessage(),e);
		}
		if (log.isDebugEnabled()) log.debug("i18n message bundles: {}",
											_i18nBundleNames);
	}
	/**
	 * Finds the annotation inside the class and superclass.
	 * @param type the <code>Class</code> object
	 *
	 * @return the message bundle
	 */
	private final UIMessageBundle _findMessageBundleAnnotation(final Class<?> type) {
		if ( type == Object.class ) throw new IllegalArgumentException( "Could NOT find @" + UIMessageBundle.class.getSimpleName() + " annotation" );
		final UIMessageBundle mBundle = type.getAnnotation( UIMessageBundle.class );
		if ( mBundle != null ) return mBundle;

		return _findMessageBundleAnnotation( type.getSuperclass() );	// RECURSE!
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasKey(final OID key) {
		return this.hasKey(key.asString());
	}
	@Override
	public boolean hasKey(final String key) {
		boolean containsKey = false;
		for (String bundleName : _i18nBundleNames) {
			ResourceBundle bundle = _retrieveBundle(bundleName,
													this.getCurrentLocale());
			containsKey = bundle.containsKey(key);
			if (containsKey) break;
		} 
		return containsKey;
	}
	@Override
	public List<String> keys() {
		List<String> outKeys = new LinkedList<String>();
		for (String bundleName : _i18nBundleNames) {
			ResourceBundle bundle = _retrieveBundle(bundleName,
													this.getCurrentLocale());
			Enumeration<String> en = bundle.getKeys();
			while (en.hasMoreElements()) outKeys.add(en.nextElement());
		}
		return outKeys;
	}
	@Override
	public String getMessage(final OID key,final Object... args) {
		return this.getMessage(key.asString(),args);
	}
	@Override
	public String getMessage(final String key,final Object... args) {
		// if the key starts with $! just return the key
		if (key.startsWith("$!")) return key.substring(2);
		
		String outMessage = null;
		for (final String i18nBundleName : _i18nBundleNames) {
			try {
				outMessage = _retrieveMessage(i18nBundleName,
											  key,args );
				if (Strings.isNOTNullOrEmpty(outMessage)) break;
			} catch(final MissingResourceException e) {
				// do nothing
			}
		}
		return Strings.isNOTNullOrEmpty(outMessage) ? outMessage : key;
	}
	@Override
	public String getMessage(final Locale locale,
							 final OID key, final Object... args) {
		return this.getMessage(key.asString(),args);
	}
	@Override
	public String getMessage(final Locale locale,
							 final String key, final Object... args) {
		// if the key starts with $! just return the key
		if (key.startsWith("$!")) return key.substring(2);
		
		String outMessage = null;
		for (final String i18nBundleName : _i18nBundleNames) {
			try {
				outMessage = _retrieveMessage(i18nBundleName,
											  locale,
											  key,args);
				if (Strings.isNOTNullOrEmpty(outMessage)) break;
			} catch (final MissingResourceException e) {
				// do nothing
			}
		}
		return Strings.isNOTNullOrEmpty(outMessage) ? outMessage : key;
	}
	@Override
	public Map<String, String> getMessagesWithKeysStartingWith(final String keyPrefix) {
		if (keyPrefix == null) throw new IllegalArgumentException("Cannot load bundle key: Missing key!");  
		Map<String,String> outMessages = new HashMap<String,String>();
		for (String bundleName : _i18nBundleNames) {
			// Load the resourceBundle and iterate for every key 
			ResourceBundle bundle = _retrieveBundle(bundleName,
													this.getCurrentLocale());
			Enumeration<String> keys = bundle.getKeys();
			if (keys != null && keys.hasMoreElements()) {
				do {
					String key = keys.nextElement();
					String msg = key.startsWith(keyPrefix) ? bundle.getString(key)
														   : null;
					if (msg != null) outMessages.put(key,msg);
				} while (keys.hasMoreElements());
			}
		}
		return outMessages;
	}
	@Override
	public Map<String, String> getMessagesMap() {
		Map<String,String> outMessages = new HashMap<String,String>();
		for (String bundleName : _i18nBundleNames) {
			// Load the resourceBundle and iterate
			ResourceBundle bundle = _retrieveBundle(bundleName,
													this.getCurrentLocale());
			Enumeration<String> keys = bundle.getKeys();
			if (keys != null && keys.hasMoreElements()) {
				do {
					String key = keys.nextElement();
					String msg = bundle.getString(key);
					if (msg != null) outMessages.put(key,msg);
				} while (keys.hasMoreElements());
			}
		}
		return outMessages;
	}
	@Override
	public Language getCurrentLanguage() {
		Locale loc = this.getCurrentLocale();
		if (loc == null) log.warn("NO current locale!!! default to {}",Language.DEFAULT);
		return loc != null ? Languages.of(loc)
						   : Language.DEFAULT;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets a string for the given key from this resource bundle or one of its parents.
	 * Calling this method is equivalent to calling
	 * <blockquote>
	 * <code>(String) {@link #getObject(java.lang.String) getObject}(key)</code>.
	 * </blockquote>
	 * @param i18nBundleName the name of the resource bundle
	 * @param key the key for the desired string
	 * @param args the arguments used to format the message
	 * @return
	 */
	private String _retrieveMessage(final String i18nBundleName,
									final String key,final Object... args) {
		return _retrieveMessage(i18nBundleName,
								this.getCurrentLocale(),
								key,args);
	}

	/**
	 * Gets a string for the given key from this resource bundle or one of its parents.
	 * Calling this method is equivalent to calling
	 * <blockquote>
	 * <code>(String) {@link #getObject(java.lang.String) getObject}(key)</code>.
	 * </blockquote>
	 *
	 * @param i18nBundleName the name of the resource bundle
	 * @param locale the locale
	 * @param key the key for the desired string
	 * @param args the arguments used to format the message
	 * @return
	 */
	private String _retrieveMessage(final String i18nBundleName,
									final Locale locale,
									final String key,final Object... args) {
		final ResourceBundle bundle = _retrieveBundle(i18nBundleName,
													  locale);
		String outMessage = bundle.getString(key); 
		if (Strings.isNOTNullOrEmpty(outMessage)
		 && CollectionUtils.hasData(args)) {
			final MessageFormat msg = new MessageFormat(outMessage);
			outMessage  = msg.format(args);
		}
		return outMessage;
	}
	private ResourceBundle _retrieveBundle(final String i18nBundleName,
										   final Locale locale) {
		final ResourceBundle bundle = ResourceBundle.getBundle(i18nBundleName,
															   locale);
		return bundle;
	}
}
